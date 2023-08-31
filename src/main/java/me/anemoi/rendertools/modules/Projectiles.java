package me.anemoi.rendertools.modules;

import cc.polyfrost.oneconfig.events.event.ReceivePacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import me.anemoi.rendertools.config.modules.ProjectilesConfig;
import me.anemoi.rendertools.utils.Point3dD;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Projectiles {
    //draw a trail behind projectiles, use ProjectilesConfig for settings fade away the trail over time
    //use the same color as the projectile itself
    public static Map<Integer, Projectile> projectiles = new HashMap<>();

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (!ProjectilesConfig.toggled) return;
        tickProjectiles();
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!ProjectilesConfig.toggled) return;
        renderProjectiles(event.partialTicks);
    }

    @Subscribe
    public void onEntitySpawn(ReceivePacketEvent event) {
        if (!ProjectilesConfig.toggled) return;
        if (event.packet instanceof S0EPacketSpawnObject) {
            S0EPacketSpawnObject packet = (S0EPacketSpawnObject) event.packet;
            //if (packet.) {
            addProjectile(packet.getEntityID(), new Point3dD(packet.getX(), packet.getY(), packet.getZ()), new Point3dD(packet.getSpeedX(), packet.getSpeedY(), packet.getSpeedZ()), packet.func_149009_m());
            //}
        }
    }

    public static void addProjectile(int id, Point3dD pos, Point3dD motion, int color) {
        projectiles.put(id, new Projectile(pos, motion, color));
    }

    public static void removeProjectile(int id) {
        projectiles.remove(id);
    }

    public static void tickProjectiles() {
        List<Integer> toRemove = new java.util.ArrayList<>();
        for (Map.Entry<Integer, Projectile> entry : projectiles.entrySet()) {
            Projectile projectile = entry.getValue();
            projectile.tick();
            if (projectile.isDead()) {
                toRemove.add(entry.getKey());
            }
        }
        for (Integer i : toRemove) {
            projectiles.remove(i);
        }
    }

    public static void renderProjectiles(float partialTicks) {
        for (Projectile projectile : projectiles.values()) {
            projectile.render(partialTicks);
        }
    }

    public static class Projectile {
        private Point3dD pos;
        private Point3dD motion;
        private int color;
        private int age = 0;

        public Projectile(Point3dD pos, Point3dD motion, int color) {
            this.pos = pos;
            this.motion = motion;
            this.color = color;
        }

        public void tick() {
            this.pos = this.pos.add(this.motion.getX(), this.motion.getY(), this.motion.getZ());
            this.age++;
        }

        public void render(float partialTicks) {
            float r = (float) (this.color >> 16 & 255) / 255.0F;
            float g = (float) (this.color >> 8 & 255) / 255.0F;
            float b = (float) (this.color & 255) / 255.0F;
            float a = (float) (this.color >> 24 & 255) / 255.0F;
            float fade = (float) Math.max(0, 1 - (age / 100.0));
            r *= fade;
            g *= fade;
            b *= fade;
            a *= fade;
            me.anemoi.rendertools.utils.RenderUtilsNew.drawLine(pos.getVec3d(), pos.subtract(motion.getX(), motion.getY(), motion.getZ()).getVec3d(), ProjectilesConfig.color.toJavaColor(), partialTicks, false);
        }

        public boolean isDead() {
            return age > 100;
        }
    }

}
