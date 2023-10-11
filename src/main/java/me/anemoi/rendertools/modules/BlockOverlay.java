package me.anemoi.rendertools.modules;

import me.anemoi.rendertools.config.modules.BlockOverlayConfig;
import me.anemoi.rendertools.utils.animation.Animator;
import me.anemoi.rendertools.utils.render.RenderBlockOverlay;
import me.anemoi.rendertools.utils.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static me.anemoi.rendertools.RenderTools.mc;

/*
Thanks to https://hypixel.net/threads/forge-1-8-9-block-overlay-v4-0-3.1417995/
for some parts of the code
 */

public class BlockOverlay {

    private final Animator blockAnimator = new Animator(350.0);
    private final List<Block> plants = Arrays.asList(Blocks.deadbush, Blocks.double_plant, Blocks.red_flower, Blocks.tallgrass, Blocks.yellow_flower);
    private final double padding;
    private boolean blockShrinking;

    public BlockOverlay() {
        this.padding = 0.002f;
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(DrawBlockHighlightEvent event) {
        if (!BlockOverlayConfig.toggled) return;
        switch (BlockOverlayConfig.mode) {
            case 0: {
                event.setCanceled(true);
            }
            case 1: {
                this.renderBlockBreakOverlay(event.player, event.partialTicks);
                break;
            }
            case 2:
            case 3: {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderWorldLastEvent event) {
        if (!BlockOverlayConfig.toggled) return;
        Entity entity = mc.getRenderViewEntity();
        if (entity == null) {
            return;
        }
        if (mc.gameSettings.hideGUI) {
            return;
        }
        int renderMode = BlockOverlayConfig.mode;
        if (renderMode != 2 && renderMode != 3) {
            return;
        }
        Block block = this.getFocusedBlock();
        if (block == null) {
            return;
        }
        if (mc.playerController.getCurrentGameType().isAdventure()) {
            if (BlockOverlayConfig.persistent || this.canRenderBlockOverlay()) {
                this.renderBlockOverlay(block, entity, event.partialTicks);
            }
        } else {
            this.renderBlockOverlay(block, entity, event.partialTicks);
        }
    }

    private void renderBlockOverlay(Block block, Entity entity, float partialTicks) {
        double entityX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
        double entityY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
        double entityZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
        double thickness = BlockOverlayConfig.lineWidth;
        boolean overlay = BlockOverlayConfig.overlayColor;
        boolean outline = BlockOverlayConfig.outlineColor;
        int overlayStartColor = BlockOverlayConfig.colorFO.toJavaColor().getRGB();
        int overlayEndColor = BlockOverlayConfig.gradient ? BlockOverlayConfig.colorSO.toJavaColor().getRGB() : overlayStartColor;
        int outlineStartColor = BlockOverlayConfig.colorFU.toJavaColor().getRGB();
        int outlineEndColor = BlockOverlayConfig.gradient ? BlockOverlayConfig.colorSU.toJavaColor().getRGB() : outlineStartColor;
        MovingObjectPosition mouseOver = mc.objectMouseOver;
        BlockPos blockPos = mouseOver.getBlockPos();
        AxisAlignedBB boundingBox = block.getSelectedBoundingBox(mc.theWorld, blockPos).expand(this.padding, this.padding, this.padding);
        EnumFacing side = BlockOverlayConfig.mode == 2 ? mouseOver.sideHit : null;
        GL11.glPushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int) GL11.GL_SRC_ALPHA, (int) GL11.GL_ONE_MINUS_SRC_ALPHA, (int) GL11.GL_ONE, (int) GL11.GL_ZERO);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean) false);
        if (BlockOverlayConfig.ignoreDepth) {
            GlStateManager.disableDepth();
        }
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        if (outline) {
            GL11.glLineWidth((float) thickness);
        }
        GL11.glShadeModel(GL11.GL_SMOOTH);
        if (BlockOverlayConfig.stairsFix) {
            if (block instanceof BlockStairs) {
                RenderBlockOverlay.drawStairs(blockPos, mc.theWorld.getBlockState(blockPos), boundingBox.expand(this.padding, this.padding, this.padding), side, entityX, entityY, entityZ, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
            } else {
                RenderBlockOverlay.drawBlock(boundingBox.offset(-entityX, -entityY, -entityZ), side, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
            }
        } else {
            RenderBlockOverlay.drawBlock(boundingBox.offset(-entityX, -entityY, -entityZ), side, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
        }
        GL11.glLineWidth(2.0f);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        GlStateManager.depthMask((boolean) true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
        this.renderBlockBreakOverlay(entity, partialTicks);
    }

    private void renderBlockOverlay(Entity entity, float partialTicks) {
        double lookX = entity.getLook((float) partialTicks).xCoord * 2.0;
        double lookY = entity.getLook((float) partialTicks).yCoord * 2.0;
        double lookZ = entity.getLook((float) partialTicks).zCoord * 2.0;
        long time = System.currentTimeMillis();
        double rotation = ((double) time / 20.0 + (double) partialTicks) % 360.0;
        double height = (double) MathHelper.sin((float) (((float) ((double) time / 20.0 % 157.0) + partialTicks) / 25.0f)) * 0.2;
        double distance = mc.gameSettings.thirdPersonView == 2 ? 0.5 : (mc.gameSettings.thirdPersonView == 1 ? -0.5 : 1.5);
        distance = this.blockAnimator.getValue(distance, Math.abs(distance * 20.0), this.blockShrinking, false);
        Block block = Blocks.glass;
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        AxisAlignedBB boundingBox = block.getSelectedBoundingBox(mc.theWorld, blockPos).expand(this.padding, this.padding, this.padding);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        BlockRendererDispatcher blockRenderer = mc.getBlockRendererDispatcher();
        int renderMode = BlockOverlayConfig.mode;
        EnumFacing side = entity.rotationPitch >= 50.0f ? EnumFacing.UP : (entity.rotationPitch >= -50.0f ? EnumFacing.NORTH : EnumFacing.DOWN);
        GL11.glPushMatrix();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int) 770, (int) 771, (int) 1, (int) 0);
        GL11.glTranslated(0.0, height, 0.0);
        GL11.glTranslated(lookX * distance, lookY * distance + 1.11, lookZ * distance);
        GL11.glRotated(rotation, 0.0, 1.0, 0.0);
        GL11.glTranslated(-0.5, 0.0, -0.5);
        worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
        blockRenderer.getBlockModelRenderer().renderModel(mc.theWorld, blockRenderer.getModelFromBlockState(block.getDefaultState(), mc.theWorld, blockPos), block.getDefaultState(), blockPos, worldRenderer, false);
        tessellator.draw();
        GlStateManager.depthMask((boolean) true);
        GlStateManager.disableTexture2D();
        if (renderMode == 1) {
            GL11.glLineWidth(1.0f);
            RenderBlockOverlay.drawBlock(boundingBox, null, -1, -1, Color.BLACK.getRGB(), Color.BLACK.getRGB(), false, true);
        } else if (renderMode != 0) {
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            if (BlockOverlayConfig.lineWidth > 1.0 && BlockOverlayConfig.outlineColor) {
                GL11.glLineWidth((float) BlockOverlayConfig.lineWidth);
            }
            GL11.glShadeModel(7425);
            RenderBlockOverlay.drawBlock(boundingBox, renderMode == 2 ? side : null, BlockOverlayConfig.colorFO.toJavaColor().getRGB(), BlockOverlayConfig.colorSO.toJavaColor().getRGB(), BlockOverlayConfig.colorFU.toJavaColor().getRGB(), BlockOverlayConfig.colorSU.toJavaColor().getRGB(), BlockOverlayConfig.overlayColor, BlockOverlayConfig.outlineColor);
            GL11.glShadeModel(7424);
            GL11.glLineWidth(1.0f);
            GL11.glDisable(2848);
        }
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        RenderUtils.glColor(Color.WHITE.getRGB());
        GL11.glPopMatrix();
    }

    private Block getFocusedBlock() {
        MovingObjectPosition mouseOver = mc.objectMouseOver;
        if (mouseOver == null || mouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return null;
        }
        BlockPos blockPos = mouseOver.getBlockPos();
        if (!mc.theWorld.getWorldBorder().contains(blockPos)) {
            return null;
        }
        Block block = mc.theWorld.getBlockState(blockPos).getBlock();
        if (block.getMaterial() == Material.air) {
            return null;
        }
        if (BlockOverlayConfig.hidePlants && this.plants.contains(block)) {
            return null;
        }
        if (!BlockOverlayConfig.barriers && block == Blocks.barrier) {
            return null;
        }
        block.setBlockBoundsBasedOnState(mc.theWorld, blockPos);
        return block;
    }

    private boolean canRenderBlockOverlay() {
        Entity entity = mc.getRenderViewEntity();
        boolean flag = entity instanceof EntityPlayer;
        if (flag && !((EntityPlayer) entity).capabilities.allowEdit) {
            ItemStack heldItem = ((EntityPlayer) entity).getCurrentEquippedItem();
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                flag = mc.playerController.isSpectator() ? block.hasTileEntity(mc.theWorld.getBlockState(blockPos)) && mc.theWorld.getTileEntity(blockPos) instanceof IInventory : heldItem != null && (heldItem.canDestroy(block) || heldItem.canPlaceOn(block));
            }
        }
        return flag;
    }

    private void renderBlockBreakOverlay(Entity entity, float partialTicks) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int) 770, (int) 1, (int) 1, (int) 0);
        mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        mc.renderGlobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), entity, partialTicks);
        mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        GlStateManager.disableBlend();
    }

    public void resetAnimation(boolean blockShrinking) {
        this.blockShrinking = blockShrinking;
        this.blockAnimator.reset();
    }
}
