package me.anemoi.rendertools.mixin.item;

import me.anemoi.rendertools.config.modules.AnimationCreatorConfig;
import me.anemoi.rendertools.config.modules.AnimationsConfig;
import me.anemoi.rendertools.config.modules.HitAnimationConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow
    private float prevEquippedProgress;
    @Shadow
    private float equippedProgress;
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void rotateArroundXAndY(float var1, float var2);

    @Shadow
    protected abstract void setLightMapFromPlayer(AbstractClientPlayer var1);

    @Shadow
    protected abstract void rotateWithPlayerRotations(EntityPlayerSP var1, float var2);

    @Shadow
    protected abstract void renderItemMap(AbstractClientPlayer var1, float var2, float var3, float var4);

    @Shadow
    protected abstract void performDrinking(AbstractClientPlayer var1, float var2);

    @Shadow
    protected abstract void doItemUsedTransformations(float var1);

    @Shadow
    public abstract void renderItem(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3);

    @Shadow
    protected abstract void renderPlayerArm(AbstractClientPlayer var1, float var2, float var3);

    @Shadow
    protected abstract void doBowTransformations(float var1, AbstractClientPlayer var2);

    /**
     * @author a
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        float f = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        EntityPlayerSP abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations(abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        if (this.itemToRender != null) {
            if (this.itemToRender.getItem() instanceof ItemMap) {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            } else if (abstractclientplayer.getItemInUseCount() > 0) {
                EnumAction enumaction = this.itemToRender.getItemUseAction();
                //if (KillAura.target != null && !RenderTools.killAura.blockMode.getSelected().equals("None")) {
                //    enumaction = EnumAction.BLOCK;
                //}
                block0 : switch (enumaction) {
                    case NONE: {
                        this.transformFirstPersonItem(f, 0.0f);
                        break;
                    }
                    case EAT:
                    case DRINK: {
                        this.performDrinking(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f, 0.0f);
                        break;
                    }
                    case BLOCK: {
                        if (AnimationsConfig.toggled) {
                            switch (AnimationsConfig.mode) {
                                case 0: {
                                    this.transformFirstPersonItem(f, f1);
                                    this.doBlockTransformations();
                                    break block0;
                                }
                                case 3:
                                case 4: {
                                    this.transformFirstPersonItem(f, AnimationsConfig.swingProgress ? f1 : 0.0f);
                                    this.doBlockTransformations();
                                    break block0;
                                }
                                case 7: {
                                    this.transformFirstPersonItem(f, 0.0f);
                                    this.doBlockTransformations();
                                    float var19 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                                    GlStateManager.translate((float)-0.05f, (float)0.6f, (float)0.3f);
                                    GlStateManager.rotate((float)(-var19 * 70.0f / 2.0f), (float)-8.0f, (float)-0.0f, (float)9.0f);
                                    GlStateManager.rotate((float)(-var19 * 70.0f), (float)1.5f, (float)-0.4f, (float)-0.0f);
                                    break block0;
                                }
                                case 1: {
                                    float f16 = MathHelper.sin((float)(MathHelper.sqrt_float((float)f1) * (float)Math.PI));
                                    this.transformFirstPersonItem(f / 2.0f - 0.18f, 0.0f);
                                    GL11.glRotatef(f16 * 60.0f / 2.0f, -f16 / 2.0f, -0.0f, -16.0f);
                                    GL11.glRotatef(-f16 * 30.0f, 1.0f, f16 / 2.0f, -1.0f);
                                    this.doBlockTransformations();
                                    break block0;
                                }
                                case 2: {
                                    this.transformFirstPersonItem(f, -f1);
                                    this.doBlockTransformations();
                                    break block0;
                                }
                                case 6: {
                                    this.transformFirstPersonItem(AnimationCreatorConfig.blockProgress ? f : 0.0f, AnimationCreatorConfig.swingProgress ? f1 : 0.0f);
                                    this.doBlockTransformations();
                                    break block0;
                                }
                                case 5: {
                                    GlStateManager.rotate((float)(System.currentTimeMillis() / 3L % 360L), (float)0.0f, (float)0.0f, (float)-0.1f);
                                    this.transformFirstPersonItem(f / 1.6f, 0.0f);
                                    this.doBlockTransformations();
                                }
                            }
                            break;
                        }
                        this.transformFirstPersonItem(f, 0.0f);
                        this.doBlockTransformations();
                        break;
                    }
                    case BOW: {
                        this.transformFirstPersonItem(f, 0.0f);
                        this.doBowTransformations(partialTicks, abstractclientplayer);
                    }
                }
            } else {
                if (!HitAnimationConfig.toggled)this.doItemUsedTransformations(f1);
                this.transformFirstPersonItem(f, f1);
            }
            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!abstractclientplayer.isInvisible()) {
            this.renderPlayerArm(abstractclientplayer, f, f1);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    /**
     * @author a
     */
    @Overwrite
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        float size = (float)AnimationsConfig.size;
        float x = (float)AnimationsConfig.x;
        float y = (float)AnimationsConfig.y;
        float z = (float)AnimationsConfig.z;
        GlStateManager.translate((float)(0.56f * x), (float)(-0.52f * y), (float)(-0.71999997f * z));
        GlStateManager.translate((float)0.0f, (float)(equipProgress * -0.6f), (float)0.0f);
        GlStateManager.rotate((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float f1 = MathHelper.sin((float)(MathHelper.sqrt_float((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(f * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(f1 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)(f1 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)(0.4f * size), (float)(0.4f * size), (float)(0.4f * size));
    }

    /**
     * @author a
     */
    @Overwrite
    private void doBlockTransformations() {
        float angle1 = 30.0f;
        float angle2 = -80.0f;
        float angle3 = 60.0f;
        float translateX = -0.5f;
        float translateY = 0.2f;
        float translateZ = 0.0f;
        float rotation1x = 0.0f;
        float rotation1y = 1.0f;
        float rotation1z = 0.0f;
        float rotation2x = 1.0f;
        float rotation2y = 0.0f;
        float rotation2z = 0.0f;
        switch (AnimationsConfig.mode) {
            case 6: {
                angle1 = (float)AnimationCreatorConfig.angle1;
                angle2 = (float)AnimationCreatorConfig.angle2;
                angle3 = (float)AnimationCreatorConfig.angle3;
                translateX = (float)AnimationCreatorConfig.translateX;
                translateY = (float)AnimationCreatorConfig.translateY;
                translateZ = (float)AnimationCreatorConfig.translateZ;
                rotation1x = (float)AnimationCreatorConfig.rotation1x;
                rotation1y = (float)AnimationCreatorConfig.rotation1y;
                rotation1z = (float)AnimationCreatorConfig.rotation1z;
                rotation2x = (float)AnimationCreatorConfig.rotation2x;
                rotation2y = (float)AnimationCreatorConfig.rotation2y;
                rotation2z = (float)AnimationCreatorConfig.rotation2z;
                break;
            }
            case 4: {
                angle1 = System.currentTimeMillis() % 720L;
                angle1 /= 2.0f;
                rotation2x = 0.0f;
                angle2 = 0.0f;
                break;
            }
            case 3: {
                translateY = 0.8f;
                angle1 = 60.0f;
                angle2 = -System.currentTimeMillis() % 720L;
                angle2 /= 2.0f;
                rotation2z = 0.8f;
                angle3 = 30.0f;
            }
        }
        GlStateManager.translate((float)translateX, (float)translateY, (float)translateZ);
        GlStateManager.rotate((float)angle1, (float)rotation1x, (float)rotation1y, (float)rotation1z);
        GlStateManager.rotate((float)angle2, (float)rotation2x, (float)rotation2y, (float)rotation2z);
        GlStateManager.rotate((float)angle3, (float)0.0f, (float)1.0f, (float)0.0f);
    }
}