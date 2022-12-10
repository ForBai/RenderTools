package me.anemoi.rendertools.modules;

import me.anemoi.rendertools.config.modules.JumpCircleConfig;
import me.anemoi.rendertools.utils.RenderUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.anemoi.rendertools.RenderTools.mc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class JumpCircles {
    public static JumpCircles instance;

    private final byte MAX_TIME = 20;
    private List<Circle> circles = new ArrayList<>();

    public JumpCircles() {
        instance = this;
    }

    @SubscribeEvent
    public void update(TickEvent.ClientTickEvent event) {
        try {
            circles.removeIf(Circle::update);
        } catch (Exception ignored) {
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        EntityPlayerSP client = mc.thePlayer;
        double ix = -(client.lastTickPosX + (client.posX - client.lastTickPosX) * event.partialTicks);
        double iy = -(client.lastTickPosY + (client.posY - client.lastTickPosY) * event.partialTicks);
        double iz = -(client.lastTickPosZ + (client.posZ - client.lastTickPosZ) * event.partialTicks);
        GL11.glPushMatrix();
        GL11.glTranslated(ix, iy, iz);
        glDisable(GL11.GL_CULL_FACE);
        glEnable(GL11.GL_BLEND);
        glDisable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_ALPHA_TEST);
        glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


        glDisable(GL11.GL_LIGHTING);

        GL11.glShadeModel(GL11.GL_SMOOTH);
        Collections.reverse(circles);
        try {
            for (Circle c : circles) {
                float k = (float) c.existed / MAX_TIME;
                double x = c.position().xCoord;
                double y = c.position().yCoord - k * 0.5;
                double z = c.position().zCoord;
                float end = k + 1f - k;
                GL11.glBegin(GL11.GL_QUAD_STRIP);
                RenderUtils.glColor(c.color);
                for (int i = 0; i <= 360; i = i + 5) {
                    GL11.glColor4f(c.color().getRed(), c.color().getGreen(), c.color().getBlue(), 0.2f * (1 - ((float) c.existed / MAX_TIME)));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 4)) * k, y, z + Math.sin(Math.toRadians(i * 4)) * k);
                    GL11.glColor4f(c.color().getRed(), c.color().getGreen(), c.color().getBlue(), 0.01f * (1 - ((float) c.existed / MAX_TIME)));
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y + Math.sin(k * 8) * 0.5, z + Math.sin(Math.toRadians(i) * end));
                }
                GL11.glEnd();
            }
        } catch (Exception ignored) {
        }
        Collections.reverse(circles);

        glEnable(GL11.GL_LIGHTING);

        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_BLEND);
        GL11.glShadeModel(GL11.GL_FLAT);
        glEnable(GL11.GL_CULL_FACE);
        glEnable(GL11.GL_DEPTH_TEST);
        glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopMatrix();
    }

    public void handleEntityJump(Entity entity) {
        circles.add(new Circle(entity.getPositionVector(), JumpCircleConfig.color.toJavaColor()));
    }

    public class Circle {
        private final Vec3 vec;
        private final Color color;
        byte existed;

        Circle(Vec3 vec, Color color) {
            this.vec = vec;
            this.color = color;
        }

        Vec3 position() {
            return this.vec;
        }

        Color color() {
            return this.color;
        }

        boolean update() {
            return ++existed > MAX_TIME;
        }
    }
}
