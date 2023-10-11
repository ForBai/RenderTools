package me.anemoi.rendertools.modules;

import me.anemoi.rendertools.mixin.entity.IEntityRenderer;
import me.anemoi.rendertools.mixin.renderer.IRenderManager;
import me.anemoi.rendertools.utils.helper.FaceMasks;
import me.anemoi.rendertools.utils.render.RenderUtilsNew;
import me.anemoi.rendertools.utils.render.TessellatorUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static me.anemoi.rendertools.RenderTools.mc;
import static me.anemoi.rendertools.config.modules.TrajectoriesConfig.*;
import static org.lwjgl.opengl.GL11.*;

public class Trajectories {
    private final CopyOnWriteArrayList<Vec3> flightPoints = new CopyOnWriteArrayList<>();

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (!toggled || mc.thePlayer == null) return;
        mc.theWorld.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityLivingBase)
                .map(entity -> (EntityLivingBase) entity)
                .forEach(entity -> {
                    this.renderEntityTrajectory(entity, event.partialTicks);
                });
    }

    private ThrowableType getTypeFromCurrentItem(EntityLivingBase player) {
        if (player.getHeldItem() == null) {
            return ThrowableType.NONE;
        }

        final ItemStack itemStack = player.getHeldItem();
        switch (Item.getIdFromItem(itemStack.getItem())) {
            case 261: // ItemBow
                return ThrowableType.ARROW;
            case 346: // ItemFishingRod
                return ThrowableType.FISHING_ROD;
            case 438: //splash potion
            case 441: //splash potion linger
                return ThrowableType.POTION;
            case 384: // ItemExpBottle
                return ThrowableType.EXPERIENCE;
            case 332: // ItemSnowball
            case 344: // ItemEgg
            case 368: // ItemEnderPearl
                return ThrowableType.NORMAL;
            default:
                break;
        }

        return ThrowableType.NONE;
    }

    enum ThrowableType {
        NONE(0.0f, 0.0f),
        ARROW(1.5f, 0.05f),
        POTION(0.5f, 0.05f),
        EXPERIENCE(0.7F, 0.07f),
        FISHING_ROD(1.5f, 0.04f),
        NORMAL(1.5f, 0.03f);

        private final float velocity;
        private final float gravity;

        ThrowableType(float velocity, float gravity) {
            this.velocity = velocity;
            this.gravity = gravity;
        }

        public float getVelocity() {
            return velocity;
        }

        public float getGravity() {
            return gravity;
        }
    }

    public static boolean shouldSpoofAim(EntityLivingBase shooter) {
        return false;
    }


    final class FlightPath {
        private EntityLivingBase shooter;
        private Vec3 position;
        private Vec3 motion;
        private float yaw;
        private float pitch;
        private final float pitchOffset;
        private AxisAlignedBB boundingBox;
        private boolean collided;
        private MovingObjectPosition target;
        private ThrowableType throwableType;

        FlightPath(EntityLivingBase player, ThrowableType throwableType) {
            this.shooter = player;
            this.throwableType = throwableType;

            this.setLocationAndAngles(this.shooter.posX, this.shooter.posY + this.shooter.getEyeHeight(), this.shooter.posZ,
                    shouldSpoofAim(this.shooter) ? (float) 0 : this.shooter.rotationYaw, shouldSpoofAim(this.shooter) ? (float) 0 : this.shooter.rotationPitch);

            if (throwableType == ThrowableType.EXPERIENCE) {
                this.pitchOffset = -20F;
            } else {
                this.pitchOffset = 0F;
            }

            Vec3 startingOffset = new Vec3(MathHelper.cos(this.yaw / 180.0F * (float) Math.PI) * 0.16F, 0.1d,
                    MathHelper.sin(this.yaw / 180.0F * (float) Math.PI) * 0.16F);

            this.position = this.position.subtract(startingOffset);
            this.setPosition(this.position);

            this.motion = new Vec3(-MathHelper.sin(this.yaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.pitch / 180.0F * (float) Math.PI),
                    -MathHelper.sin((this.pitch + pitchOffset) / 180.0F * (float) Math.PI),
                    MathHelper.cos(this.yaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.pitch / 180.0F * (float) Math.PI));

            this.setThrowableHeading(this.motion, this.getInitialVelocity());
        }

        public void onUpdate() {
            Vec3 prediction = this.position.add(this.motion);
            MovingObjectPosition blockCollision = this.shooter.getEntityWorld().rayTraceBlocks(this.position, prediction,
                    this.throwableType == ThrowableType.FISHING_ROD, !this.collidesWithNoBoundingBox(), false);

            if (blockCollision != null) {
                prediction = blockCollision.hitVec;
            }

            this.onCollideWithEntity(prediction, blockCollision);

            if (this.target != null) {
                this.collided = true;
                this.setPosition(this.target.hitVec);
                return;
            }

            if (this.position.yCoord <= 0.0d) {
                this.collided = true;
                return;
            }

            this.position = this.position.add(this.motion);
            float motionModifier = 0.99F;
            if (this.shooter.getEntityWorld().isMaterialInBB(this.boundingBox, Material.water)) {
                motionModifier = this.throwableType == ThrowableType.ARROW ? 0.6F : 0.8F;
            }

            if (this.throwableType == ThrowableType.FISHING_ROD) {
                motionModifier = 0.92f;
            }

            this.motion = new Vec3(this.motion.xCoord * motionModifier, this.motion.yCoord * motionModifier, this.motion.zCoord * motionModifier);
            this.motion = this.motion.subtract(0.0d, this.getGravityVelocity(), 0.0d);
            this.setPosition(this.position);
        }

        private boolean collidesWithNoBoundingBox() {
            switch (this.throwableType) {
                case FISHING_ROD:
                case NORMAL:
                    return true;
                default:
                    return false;
            }
        }

        private void onCollideWithEntity(Vec3 prediction, MovingObjectPosition blockCollision) {
            Entity collidingEntity = null;
            MovingObjectPosition collidingPosition = null;

            double currentDistance = 0.0d;
            ArrayList<Entity> collisionEntities = (ArrayList<Entity>) mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.shooter, this.boundingBox.expand(this.motion.xCoord, this.motion.yCoord, this.motion.zCoord).expand(1.0D, 1.0D, 1.0D));

            for (Entity entity : collisionEntities) {
                if (!entity.canBeCollidedWith()) {
                    continue;
                }

                float collisionSize = entity.getCollisionBorderSize();
                AxisAlignedBB expandedBox = entity.getEntityBoundingBox().expand(collisionSize, collisionSize, collisionSize);
                MovingObjectPosition objectPosition = expandedBox.calculateIntercept(this.position, prediction);

                if (objectPosition != null) {
                    double distanceTo = this.position.distanceTo(objectPosition.hitVec);

                    if (distanceTo < currentDistance || currentDistance == 0.0D) {
                        collidingEntity = entity;
                        collidingPosition = objectPosition;
                        currentDistance = distanceTo;
                    }
                }
            }

            if (collidingEntity != null) {
                this.target = new MovingObjectPosition(collidingEntity, collidingPosition.hitVec);
            } else {
                this.target = blockCollision;
            }
        }


        private float getInitialVelocity() {
            switch (this.throwableType) {
                case ARROW:
                    int useDuration = this.shooter.getHeldItem().getItem().getMaxItemUseDuration(this.shooter.getHeldItem()) - mc.thePlayer.getItemInUseCount();
                    float velocity = (float) useDuration / 20.0F;
                    velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;
                    if (velocity > 1.0F) {
                        velocity = 1.0F;
                    }

                    return (velocity * 2.0f) * throwableType.getVelocity();
                default:
                    return throwableType.getVelocity();
            }
        }

        private float getGravityVelocity() {
            return throwableType.getGravity();
        }

        private void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
            this.position = new Vec3(x, y, z);
            this.yaw = yaw;
            this.pitch = pitch;
        }

        private void setPosition(Vec3 position) {
            this.position = new Vec3(position.xCoord, position.yCoord, position.zCoord);
            double entitySize = (this.throwableType == ThrowableType.ARROW ? 0.5d : 0.25d) / 2.0d;
            this.boundingBox = new AxisAlignedBB(position.xCoord - entitySize,
                    position.yCoord - entitySize,
                    position.zCoord - entitySize,
                    position.xCoord + entitySize,
                    position.yCoord + entitySize,
                    position.zCoord + entitySize);
        }

        private void setThrowableHeading(Vec3 motion, float velocity) {
            this.motion = new Vec3(motion.xCoord * (1 / motion.lengthVector()), motion.yCoord * (1 / motion.lengthVector()), motion.zCoord * (1 / motion.lengthVector()));
            this.motion = new Vec3(this.motion.xCoord * velocity, this.motion.yCoord * velocity, this.motion.zCoord * velocity);
        }

        public boolean isCollided() {
            return collided;
        }

        public MovingObjectPosition getCollidingTarget() {
            return target;
        }
    }

    private void renderEntityTrajectory(EntityLivingBase entity, float partialTicks) {
        ThrowableType throwingType = this.getTypeFromCurrentItem(entity);

        if (throwingType == ThrowableType.NONE) {
            return;
        }

        FlightPath flightPath = new FlightPath(entity, throwingType);

        this.flightPoints.clear();

        while (!flightPath.isCollided()) {
            flightPath.onUpdate();

            this.flightPoints.add(new Vec3(flightPath.position.xCoord - mc.getRenderManager().viewerPosX,
                    flightPath.position.yCoord - mc.getRenderManager().viewerPosY,
                    flightPath.position.zCoord - mc.getRenderManager().viewerPosZ));
        }

        renderLine(entity, partialTicks);

        if (flightPath.collided) {
            final MovingObjectPosition hit = flightPath.target;
            AxisAlignedBB bb = null;

            if (hit == null) return;

            if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos blockpos = hit.getBlockPos();
                final IBlockState iblockstate = mc.theWorld.getBlockState(blockpos);

                if (iblockstate.getBlock() != Blocks.air && mc.theWorld.getWorldBorder().contains(blockpos)) {
                    if (vector) renderVector(entity, hit);
                    bb = iblockstate.getBlock().getSelectedBoundingBox(mc.theWorld, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D);
                }
            } else if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && hit.entityHit != null && hit.entityHit != mc.thePlayer) {
                bb = hit.entityHit.getEntityBoundingBox();
            }

            if (bb != null && highlightBlock) {
                if (facing && hit.sideHit != null) {
                    switch (hit.sideHit) {
                        case DOWN:
                            bb = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.minY, bb.maxZ);
                            break;
                        case UP:
                            bb = new AxisAlignedBB(bb.minX, bb.maxY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
                            break;
                        case NORTH:
                            bb = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.minZ);
                            break;
                        case SOUTH:
                            bb = new AxisAlignedBB(bb.minX, bb.minY, bb.maxZ, bb.maxX, bb.maxY, bb.maxZ);
                            break;
                        case EAST:
                            bb = new AxisAlignedBB(bb.maxX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
                            break;
                        case WEST:
                            bb = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.minX, bb.maxY, bb.maxZ);
                            break;
                    }
                }
                TessellatorUtil.prepare();
                TessellatorUtil.drawBox(bb, true, 1, entity == mc.thePlayer ? selfFillColor.toJavaColor() : fillColor.toJavaColor(), entity == mc.thePlayer ? selfFillColor.toJavaColor().getAlpha() : fillColor.toJavaColor().getAlpha(), FaceMasks.Quad.ALL);
                TessellatorUtil.drawBoundingBox(bb, outlineWidth, entity == mc.thePlayer ? selfOutlineColor.toJavaColor() : outlineColor.toJavaColor());
                TessellatorUtil.release();
            }
        }
    }

    public void renderLine(EntityLivingBase entity, float partialTicks) {

        final boolean bobbing = mc.gameSettings.viewBobbing;
        mc.gameSettings.viewBobbing = false;
        ((IEntityRenderer) mc.entityRenderer).iSetupCameraTransform(partialTicks, 0);

        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glPushMatrix();
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDepthMask(false);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(lineWidth);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer buffer = tessellator.getWorldRenderer();

        Vec3 lastPos = this.flightPoints.get(0);

        Color c = entity == mc.thePlayer ? selfLineColor.toJavaColor() : lineColor.toJavaColor();

        for (Vec3 pos : this.flightPoints) {
            buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(lastPos.xCoord, lastPos.yCoord, lastPos.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
            buffer.pos(pos.xCoord, pos.yCoord, pos.zCoord).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
            lastPos = pos;
            tessellator.draw();
        }

        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glEnable(GL_LIGHTING);
        glPopMatrix();
        glPopAttrib();

        mc.gameSettings.viewBobbing = bobbing;
        ((IEntityRenderer) mc.entityRenderer).iSetupCameraTransform(partialTicks, 0);

    }

    public void renderVector(EntityLivingBase entity, MovingObjectPosition result) {

        GlStateManager.pushMatrix();
        RenderUtilsNew.beginRender();
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();


        GL11.glLineWidth(vectorWidth);
        if (entity == mc.thePlayer) {
            GL11.glColor4f(selfVectorColor.toJavaColor().getRed() / 255F, selfVectorColor.toJavaColor().getGreen() / 255F, selfVectorColor.toJavaColor().getBlue() / 255F, selfVectorColor.toJavaColor().getAlpha() / 255F);
        } else {
            GL11.glColor4f(vectorColor.toJavaColor().getRed() / 255F, vectorColor.toJavaColor().getGreen() / 255F, vectorColor.toJavaColor().getBlue() / 255F, vectorColor.toJavaColor().getAlpha() / 255F);
        }
        GlStateManager.translate(result.hitVec.xCoord - ((IRenderManager) mc.getRenderManager()).getRenderPosX(), result.hitVec.yCoord - ((IRenderManager) mc.getRenderManager()).getRenderPosY(), result.hitVec.zCoord - ((IRenderManager) mc.getRenderManager()).getRenderPosZ());

        EnumFacing side = result.sideHit;

        switch (side) {
            case NORTH:
            case SOUTH:
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                break;
            case WEST:
            case EAST:
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                break;
        }

        Cylinder c = new Cylinder();
        GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
        c.setDrawStyle(GLU.GLU_LINE);

        c.draw(radius * 2F, radius, 0.0f, slices, 1);

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        RenderUtilsNew.endRender();
        GlStateManager.popMatrix();

    }


}
