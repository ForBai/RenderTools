package me.anemoi.rendertools.modules;

import me.anemoi.rendertools.config.modules.HitParticlesConfig;
import me.anemoi.rendertools.events.AttackEvent;
import me.anemoi.rendertools.events.MotionEvent;
import me.anemoi.rendertools.mixin.renderer.IRenderManager;
import me.anemoi.rendertools.utils.helper.BetterVec3;
import me.anemoi.rendertools.utils.render.RenderUtilsNew;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

import static me.anemoi.rendertools.RenderTools.mc;

public class HitParticles {
    private final List<Particle> particles = new MaxSizeList<>(100);
    private final List<Particle> particlesToRemoveNextIt = new LinkedList<>();
    private final Timer timer = new Timer();
    private EntityLivingBase target;

    @SubscribeEvent
    public void onAttack(AttackEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !HitParticlesConfig.toggled) return;
        if (event.getTarget() instanceof EntityLivingBase)
            target = (EntityLivingBase) event.getTarget();
    }

    @SubscribeEvent
    public void onMotion(MotionEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !HitParticlesConfig.toggled) return;
        if (HitParticlesConfig.onHitPlayer && target != null && target.hurtTime >= 9 && mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) < 10) {
            for (int i = 0; i < HitParticlesConfig.amount; i++)
                particles.add(new Particle(
                        new BetterVec3(
                                target.posX + (Math.random() - 0.5) * 0.5,
                                target.posY + Math.random() * 1 + 0.5,
                                target.posZ + (Math.random() - 0.5) * 0.5
                        )
                ));
            target = null;
        }
    }

    @SubscribeEvent
    public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !HitParticlesConfig.toggled) return;
        if (HitParticlesConfig.onBreakBlock && event.getPlayer().equals(mc.thePlayer)) {
            for (int i = 0; i < HitParticlesConfig.amount; i++)
                particles.add(new Particle(
                        new BetterVec3(
                                event.pos.getX() + 0.5 + (Math.random() - 0.5) * 0.5,
                                event.pos.getY() + Math.random() * 1 + 0.5,
                                event.pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.5
                        )
                ));
        }
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !HitParticlesConfig.toggled) return;
        if (particles.isEmpty())
            return;

        if (particlesToRemoveNextIt.size() > 0) {
            particles.removeAll(particlesToRemoveNextIt);
            particlesToRemoveNextIt.clear();
        }

        for (int i = 0; i <= timer.getElapsedTime() / 1E+11; i++) {
            if (HitParticlesConfig.physics) particles.forEach(Particle::update);
            else particles.forEach(Particle::updateWithoutPhysics);
        }

        particles.removeIf(particle -> mc.thePlayer.getDistanceSq(particle.pos.xCoord, particle.pos.yCoord, particle.pos.zCoord) > 50 * 10);
        timer.reset();

        List<Particle> particlesToRemove = renderParticles(particles);
        if (!particlesToRemove.isEmpty())
            particlesToRemoveNextIt.addAll(particlesToRemove);
        ((MaxSizeList<Particle>) particles).setMaxSize(HitParticlesConfig.maxParticles);
    }

    @SubscribeEvent
    public void onLoadWorld(WorldEvent.Load event) {
        particles.clear();
    }

    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        particles.clear();
    }

    public static List<Particle> renderParticles(final List<Particle> particles) {
        final List<Particle> particlesToRemove = new LinkedList<>();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int i = 0;
        try {
            for (final Particle particle : particles) {
                i++;
                final BetterVec3 v = particle.pos;
                boolean draw = true;

                final double x = v.xCoord - ((IRenderManager) (mc.getRenderManager())).getRenderPosX();
                final double y = v.yCoord - ((IRenderManager) (mc.getRenderManager())).getRenderPosY();
                final double z = v.zCoord - ((IRenderManager) (mc.getRenderManager())).getRenderPosZ();

                final double distanceFromPlayer = mc.thePlayer.getDistance(v.xCoord, v.yCoord - 1, v.zCoord);
                int quality = (int) (distanceFromPlayer * 4 + 10);

                if (quality > 350)
                    quality = 350;

                if (!RenderUtilsNew.isInViewFrustrum(new EntityEgg(mc.theWorld, v.xCoord, v.yCoord, v.zCoord)))
                    draw = false;

                if (i % 10 != 0 && distanceFromPlayer > 25)
                    draw = false;

                if (i % 3 == 0 && distanceFromPlayer > 15)
                    draw = false;

                if (draw) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y, z);

                    final float scale = 0.04F;
                    GL11.glScalef(-scale, -scale, -scale);

                    GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                    GL11.glRotated(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0D : 1.0D, 0.0D, 0.0D);
                    Color c;
                    if (HitParticlesConfig.betterRainbow) {
                        float speed = 4500f;
                        float hue = System.currentTimeMillis() % ((int) speed) / speed;
                        c = new Color(Color.HSBtoRGB(hue - -(1 + 5 * 1.7f) / 54, 0.7f, 1));
                    } else {
                        c = HitParticlesConfig.color.toJavaColor();
                    }
                    Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 50);
                    Color c3 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 30);
                    //change the alpha of the color so that the particles fade out over time, use HitParticlesConfig.time to get the max time but the take into account that the max alpha is 255 and the min alpha is 0 and if we hit a alpha of 0 we add the particle to a list to be deleted
                    System.out.println(Math.min((int) (255 - (particle.removeTimer.getElapsedTime() / (float) HitParticlesConfig.time) * 255), 255));
                    c = new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(Math.min((int) (255 - (particle.removeTimer.getElapsedTime() / (float) HitParticlesConfig.time) * 255), 255), 0));
                    //for c2 the same except that c2s alpha starts at 50
                    c2 = new Color(c2.getRed(), c2.getGreen(), c2.getBlue(), Math.max(Math.min((int) (50 - (particle.removeTimer.getElapsedTime() / (float) HitParticlesConfig.time) * 50), 50), 0));
                    //for c3 the same except that c3s alpha starts at 30
                    c3 = new Color(c3.getRed(), c3.getGreen(), c3.getBlue(), Math.max(Math.min((int) (30 - (particle.removeTimer.getElapsedTime() / (float) HitParticlesConfig.time) * 30), 30), 0));
                    if (c.getAlpha() == 0) {
                        particlesToRemove.add(particle);
                        continue;
                    }
                    if (c.getAlpha() >= 0)
                        RenderUtilsNew.drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);

                    if (distanceFromPlayer < HitParticlesConfig.showCircle2Distance && c2.getAlpha() >= 0)
                        RenderUtilsNew.drawFilledCircleNoGL(0, 0, 1.4, c2.hashCode(), quality);

                    if (distanceFromPlayer < HitParticlesConfig.showCircle3Distance && c3.getAlpha() >= 0)
                        RenderUtilsNew.drawFilledCircleNoGL(0, 0, 2.3, c3.hashCode(), quality);

                    GL11.glScalef(0.8F, 0.8F, 0.8F);
                    GL11.glPopMatrix();
                }
            }
        } catch (final ConcurrentModificationException ignored) {
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glColor3d(255, 255, 255);
        return particlesToRemove;
    }

    public static Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }

    public static class Timer {
        public long lastMS;

        private long getCurrentMS() {
            return System.nanoTime() / 1000000L;
        }

        public final long getElapsedTime() {
            return this.getCurrentMS() - this.lastMS;
        }

        public void reset() {
            this.lastMS = this.getCurrentMS();
        }

    }


    public static final class MaxSizeList<T> extends LinkedList<T> {

        private int maxSize;

        public MaxSizeList(final int maxSize) {
            this.maxSize = maxSize;
        }

        public int getMaxSize() {
            return this.maxSize;
        }

        public void setMaxSize(final int maxSize) {
            this.maxSize = maxSize;
        }

        @Override
        public boolean add(final T t) {
            if (size() >= maxSize) removeFirst();
            return super.add(t);
        }

    }

    public static class Particle {

        private final Timer removeTimer = new Timer();
        private Long age = 0L;

        public final BetterVec3 pos;
        private final BetterVec3 delta;

        public Particle(final BetterVec3 position) {
            this.pos = position;
            this.delta = new BetterVec3((Math.random() * 2.5 - 1.25) * 0.04, (Math.random() * 0.5 - 0.2) * 0.04, (Math.random() * 2.5 - 1.25) * 0.04);
            this.removeTimer.reset();
        }

        public void update() {
            final Block block1 = getBlock(this.pos.xCoord, this.pos.yCoord, this.pos.zCoord + this.delta.zCoord);
            if (!(block1 instanceof BlockAir || block1 instanceof BlockBush || block1 instanceof BlockLiquid))
                this.delta.zCoord *= -0.8;

            final Block block2 = getBlock(this.pos.xCoord, this.pos.yCoord + this.delta.yCoord, this.pos.zCoord);
            if (!(block2 instanceof BlockAir || block2 instanceof BlockBush || block2 instanceof BlockLiquid)) {
                this.delta.xCoord *= 0.99F;
                this.delta.zCoord *= 0.99F;

                this.delta.yCoord *= -0.5;
            }

            final Block block3 = getBlock(this.pos.xCoord + this.delta.xCoord, this.pos.yCoord, this.pos.zCoord);
            if (!(block3 instanceof BlockAir || block3 instanceof BlockBush || block3 instanceof BlockLiquid))
                this.delta.xCoord *= -0.8;

            this.updateWithoutPhysics();
        }

        public void updateWithoutPhysics() {
            this.pos.xCoord += this.delta.xCoord;
            this.pos.yCoord += this.delta.yCoord;
            this.pos.zCoord += this.delta.zCoord;
            this.delta.xCoord *= 0.998F;
            this.delta.yCoord -= 0.000031;
            this.delta.zCoord *= 0.998F;
        }
    }
}
