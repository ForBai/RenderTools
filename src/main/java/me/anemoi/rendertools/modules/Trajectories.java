package me.anemoi.rendertools.modules;

import me.anemoi.rendertools.config.MainConfig;
import me.anemoi.rendertools.config.modules.TrajectoriesConfig;
import me.anemoi.rendertools.utils.MathUtil;
import me.anemoi.rendertools.utils.RenderUtilsNew;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL32;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static me.anemoi.rendertools.RenderTools.mc;
import static org.lwjgl.opengl.GL11.*;

/*
 Thanks to ionar2 for spidermod
 https://github.com/ionar2/spidermod/blob/master/LICENSE.md
 */

public class Trajectories {

    private final Queue<Vec3> flightPoint = new ConcurrentLinkedQueue<>();


    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent renderEvent) {
        if (mc.thePlayer == null || mc.theWorld == null || !TrajectoriesConfig.toggled || !MainConfig.anemoi) return;
        Color color = TrajectoriesConfig.color.toJavaColor();

        ThrowableType throwingType = this.getTypeFromCurrentItem(mc.thePlayer);

        if (throwingType == ThrowableType.NONE) {
            return;
        }

        FlightPath flightPath = new FlightPath(mc.thePlayer, throwingType);

        while (!flightPath.isCollided()) {
            flightPath.onUpdate();

            flightPoint.offer(new Vec3(flightPath.position.xCoord - mc.getRenderManager().viewerPosX, flightPath.position.yCoord - mc.getRenderManager().viewerPosY,
                    flightPath.position.zCoord - mc.getRenderManager().viewerPosZ));
        }

        final boolean bobbing = mc.gameSettings.viewBobbing;
        mc.gameSettings.viewBobbing = false;
        //mc.entityRenderer.setupCameraTransform(p_Event.getPartialTicks(), 0);
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(GL_SMOOTH);
        glLineWidth(TrajectoriesConfig.width);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GlStateManager.disableDepth();
        glEnable(GL32.GL_DEPTH_CLAMP);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer bufferbuilder = tessellator.getWorldRenderer();

        while (!flightPoint.isEmpty()) {
            bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            Vec3 head = flightPoint.poll();
            bufferbuilder.pos(head.xCoord, head.yCoord, head.zCoord).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f).endVertex();

            if (flightPoint.peek() != null) {
                Vec3 point = flightPoint.peek();
                bufferbuilder.pos(point.xCoord, point.yCoord, point.zCoord).color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f).endVertex();
            }

            tessellator.draw();
        }

        GlStateManager.shadeModel(GL_FLAT);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        glDisable(GL32.GL_DEPTH_CLAMP);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();

        mc.gameSettings.viewBobbing = bobbing;
        //mc.entityRenderer.setupCameraTransform(p_Event.getPartialTicks(), 0);

        if (flightPath.collided) {
            final MovingObjectPosition hit = flightPath.target;
            AxisAlignedBB bb = null;

            if (hit == null)
                return;

            if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                final BlockPos blockpos = hit.getBlockPos();
                final IBlockState iblockstate = mc.theWorld.getBlockState(blockpos);

                if (iblockstate.getBlock() != Blocks.air && mc.theWorld.getWorldBorder().contains(blockpos)) {
                    final Vec3 interp = MathUtil.interpolateEntity(mc.thePlayer, renderEvent.partialTicks);
                    bb = iblockstate.getBlock().getSelectedBoundingBox(mc.theWorld, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-interp.xCoord, -interp.yCoord, -interp.zCoord);
                }
            } else if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && hit.entityHit != null) {
                final AxisAlignedBB entityBB = hit.entityHit.getEntityBoundingBox();
                if (entityBB != null) {
                    bb = new AxisAlignedBB(entityBB.minX - mc.getRenderManager().viewerPosX, entityBB.minY - mc.getRenderManager().viewerPosY, entityBB.minZ - mc.getRenderManager().viewerPosZ,
                            entityBB.maxX - mc.getRenderManager().viewerPosX, entityBB.maxY - mc.getRenderManager().viewerPosY, entityBB.maxZ - mc.getRenderManager().viewerPosZ);
                }
            }

            if (bb != null) {
                RenderUtilsNew.drawBoundingBox(bb, TrajectoriesConfig.width, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            }
        }
    }


    private ThrowableType getTypeFromCurrentItem(EntityPlayerSP player) {
        // Check if we're holding an item first
        if (player.getHeldItem() == null) {
            return ThrowableType.NONE;
        }

        final ItemStack itemStack = player.getHeldItem();
        // Check what type of item this is
        switch (Item.getIdFromItem(itemStack.getItem())) {
            case 261: // ItemBow
                return ThrowableType.ARROW;
            case 346: // ItemFishingRod
                return ThrowableType.FISHING_ROD;
            case 438: // splash potion
                return ThrowableType.POTION;
            case 441: // splash potion linger
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
        /**
         * Represents a non-throwable object.
         */
        NONE(0.0f, 0.0f),

        /**
         * Arrows fired from a bow.
         */
        ARROW(1.5f, 0.05f),

        /**
         * Splash potion entities
         */
        POTION(0.5f, 0.05f),

        /**
         * Experience bottles.
         */
        EXPERIENCE(0.7F, 0.07f),

        /**
         * The fishhook entity with a fishing rod.
         */
        FISHING_ROD(1.5f, 0.04f),

        /**
         * Any throwable entity that doesn't have unique world velocity/gravity constants.
         */
        NORMAL(1.5f, 0.03f);

        private final float velocity;
        private final float gravity;

        ThrowableType(float velocity, float gravity) {
            this.velocity = velocity;
            this.gravity = gravity;
        }

        /**
         * The initial velocity of the entity.
         *
         * @return entity velocity
         */

        public float getVelocity() {
            return velocity;
        }

        /**
         * The constant gravity applied to the entity.
         *
         * @return constant world gravity
         */
        public float getGravity() {
            return gravity;
        }
    }

    /**
     * A class used to mimic the flight of an entity. Actual implementation resides in multiple classes but the parent of all of them is {@link net.minecraft.entity.projectile.EntityThrowable}
     */
    final class FlightPath {
        private EntityPlayerSP shooter;
        private Vec3 position;
        private Vec3 motion;
        private float yaw;
        private float pitch;
        private AxisAlignedBB boundingBox;
        private boolean collided;
        private MovingObjectPosition target;
        private ThrowableType throwableType;

        FlightPath(EntityPlayerSP player, ThrowableType throwableType) {
            this.shooter = player;
            this.throwableType = throwableType;

            // Set the starting angles of the entity
            this.setLocationAndAngles(this.shooter.posX, this.shooter.posY + this.shooter.getEyeHeight(), this.shooter.posZ, this.shooter.rotationYaw, this.shooter.rotationPitch);

            Vec3 startingOffset = new Vec3(MathHelper.cos(this.yaw / 180.0F * (float) Math.PI) * 0.16F, 0.1d, MathHelper.sin(this.yaw / 180.0F * (float) Math.PI) * 0.16F);

            this.position = this.position.subtract(startingOffset);
            // Update the entity's bounding box
            this.setPosition(this.position);

            // Set the entity's motion based on the shooter's rotations
            this.motion = new Vec3(-MathHelper.sin(this.yaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.pitch / 180.0F * (float) Math.PI),
                    -MathHelper.sin(this.pitch / 180.0F * (float) Math.PI), MathHelper.cos(this.yaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.pitch / 180.0F * (float) Math.PI));

            this.setThrowableHeading(this.motion, this.getInitialVelocity());
        }

        public void onUpdate() {
            // Get the predicted positions in the world
            Vec3 prediction = this.position.add(this.motion);
            // Check if we've collided with a block in the same time
            MovingObjectPosition blockCollision = this.shooter.getEntityWorld().rayTraceBlocks(this.position, prediction, this.throwableType == ThrowableType.FISHING_ROD, !this.collidesWithNoBoundingBox(),
                    false);
            // Check if we got a block collision
            if (blockCollision != null) {
                prediction = blockCollision.hitVec;
            }

            // Check entity collision
            this.onCollideWithEntity(prediction, blockCollision);

            // Check if we had a collision
            if (this.target != null) {
                this.collided = true;
                // Update position
                this.setPosition(this.target.hitVec);
                return;
            }

            // Sanity check to see if we've gone below the world (if we have we will never collide)
            if (this.position.yCoord <= 0.0d) {
                // Force this to true even though we haven't collided with anything
                this.collided = true;
                return;
            }

            // Update the entity's position based on velocity
            this.position = this.position.add(this.motion);
            float motionModifier = 0.99F;
            // Check if our path will collide with water
            if (this.shooter.getEntityWorld().isMaterialInBB(this.boundingBox, Material.water)) {
                // Arrows move slower in water than normal throwables
                motionModifier = this.throwableType == ThrowableType.ARROW ? 0.6F : 0.8F;
            }

            // Apply the fishing rod specific motion modifier
            if (this.throwableType == ThrowableType.FISHING_ROD) {
                motionModifier = 0.92f;
            }

            // Slowly decay the velocity of the path
            this.motion = MathUtil.mult(this.motion, motionModifier);
            // Drop the motionY by the constant gravity
            this.motion = this.motion.subtract(0.0d, this.getGravityVelocity(), 0.0d);
            // Update the position and bounding box
            this.setPosition(this.position);
        }


        /**
         * Checks if a specific item type will collide with a block that has no collision bounding box.
         *
         * @return true if type collides
         */
        private boolean collidesWithNoBoundingBox() {
            switch (this.throwableType) {
                case FISHING_ROD:
                case NORMAL:
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Check if our path collides with an entity.
         *
         * @param prediction     the predicted position
         * @param blockCollision block collision if we had one
         */
        private void onCollideWithEntity(Vec3 prediction, MovingObjectPosition blockCollision) {
            Entity collidingEntity = null;
            MovingObjectPosition collidingPosition = null;

            double currentDistance = 0.0d;
            // Get all possible collision entities disregarding the local player
            List<Entity> collisionEntities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABBExcludingEntity(this.shooter,
                    this.boundingBox.expand(this.motion.xCoord, this.motion.yCoord, this.motion.zCoord).expand(1.0D, 1.0D, 1.0D));

            // Loop through every loaded entity in the world
            for (Entity entity : collisionEntities) {
                // Check if we can collide with the entity or it's ourself
                if (!entity.canBeCollidedWith()) {
                    continue;
                }

                // Check if we collide with our bounding box
                float collisionSize = entity.getCollisionBorderSize();
                AxisAlignedBB expandedBox = entity.getEntityBoundingBox().expand(collisionSize, collisionSize, collisionSize);
                MovingObjectPosition objectPosition = expandedBox.calculateIntercept(this.position, prediction);

                // Check if we have a collision
                if (objectPosition != null) {
                    double distanceTo = this.position.distanceTo(objectPosition.hitVec);

                    // Check if we've gotten a closer entity
                    if (distanceTo < currentDistance || currentDistance == 0.0D) {
                        collidingEntity = entity;
                        collidingPosition = objectPosition;
                        currentDistance = distanceTo;
                    }
                }
            }

            // Check if we had an entity
            if (collidingEntity != null) {
                // Set our target to the result
                this.target = new MovingObjectPosition(collidingEntity, collidingPosition.hitVec);
            } else {
                // Fallback to the block collision
                this.target = blockCollision;
            }
        }

        /**
         * Return the initial velocity of the entity at it's exact starting moment in flight.
         *
         * @return entity velocity in flight
         */
        private float getInitialVelocity() {
            switch (this.throwableType) {
                // Arrows use the current use duration as a velocity multplier
                case ARROW:
                    // Check how long we've been using the bow
                    int useDuration = this.shooter.getHeldItem().getItem().getMaxItemUseDuration(this.shooter.getHeldItem()) - this.shooter.getItemInUseCount();
                    float velocity = (float) useDuration / 20.0F;
                    velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;
                    if (velocity > 1.0F) {
                        velocity = 1.0F;
                    }

                    // When the arrow is spawned inside of ItemBow, they multiply it by 2
                    return (velocity * 2.0f) * throwableType.getVelocity();
                default:
                    return throwableType.getVelocity();
            }
        }

        /**
         * Get the constant gravity of the item in use.
         *
         * @return gravity relating to item
         */
        private float getGravityVelocity() {
            return throwableType.getGravity();
        }

        /**
         * Set the position and rotation of the entity in the world.
         *
         * @param x     x position in world
         * @param y     y position in world
         * @param z     z position in world
         * @param yaw   yaw rotation axis
         * @param pitch pitch rotation axis
         */
        private void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
            this.position = new Vec3(x, y, z);
            this.yaw = yaw;
            this.pitch = pitch;
        }

        /**
         * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
         *
         * @param position position in world
         */
        private void setPosition(Vec3 position) {
            this.position = new Vec3(position.xCoord, position.yCoord, position.zCoord);
            // Usually this is this.width / 2.0f but throwables change
            double entitySize = (this.throwableType == ThrowableType.ARROW ? 0.5d : 0.25d) / 2.0d;
            // Update the path's current bounding box
            this.boundingBox = new AxisAlignedBB(position.xCoord - entitySize, position.yCoord - entitySize, position.zCoord - entitySize, position.xCoord + entitySize, position.yCoord + entitySize, position.zCoord + entitySize);
        }

        /**
         * Set the entity's velocity and position in the world.
         *
         * @param motion   velocity in world
         * @param velocity starting velocity
         */
        private void setThrowableHeading(Vec3 motion, float velocity) {
            // Divide the current motion by the length of the vector
            this.motion = MathUtil.div(motion, (float) motion.lengthVector());
            // Multiply by the velocity
            this.motion = MathUtil.mult(this.motion, velocity);
        }

        /**
         * Check if the path has collided with an object.
         *
         * @return path collides with ground
         */
        public boolean isCollided() {
            return collided;
        }

        /**
         * Get the target we've collided with if it exists.
         *
         * @return moving object target
         */
        public MovingObjectPosition getCollidingTarget() {
            return target;
        }
    }


}
