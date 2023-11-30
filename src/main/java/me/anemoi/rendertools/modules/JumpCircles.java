package me.anemoi.rendertools.modules;

import me.anemoi.rendertools.config.modules.JumpCircleConfig;
import me.anemoi.rendertools.utils.render.RenderUtils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static me.anemoi.rendertools.RenderTools.mc;

/*
 * Thanks to SkidderMC for https://github.com/SkidderMC/FDPClient/ for the original code.
 * I've modified it to work with my config system and to be a bit more readable.
 * I also ported it to Java.
 * modifed by forbai
 */
public class JumpCircles {
    private List<Circle> circles = new ArrayList<Circle>();
    private boolean lastOnGround = false;

    @SubscribeEvent
    public void onUpdate(TickEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !JumpCircleConfig.toggled) return;
        if (mc.thePlayer.onGround && !lastOnGround) {
            circles.add(new Circle(System.currentTimeMillis(), mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
        }
        lastOnGround = mc.thePlayer.onGround;
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !JumpCircleConfig.toggled) return;
        circles.removeIf(circle -> System.currentTimeMillis() > circle.time + JumpCircleConfig.time);

        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        circles.forEach(Circle::draw);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_FLAT);

        GL11.glPopMatrix();
    }

    public static class Circle {
        private Long time;
        private Double x;
        private Double y;
        private Double z;

        public Circle(Long time, Double x, Double y, Double z) {
            this.time = time;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void draw() {
            long dif = (System.currentTimeMillis() - time);
            int c = (int) (255 - (dif / JumpCircleConfig.time) * 255);

            GL11.glPushMatrix();

            GL11.glTranslated(
                    x - mc.getRenderManager().viewerPosX,
                    y - mc.getRenderManager().viewerPosY,
                    z - mc.getRenderManager().viewerPosZ
            );

            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            for (int i = 0; i < 361; i++) {
                Color color;
                if (JumpCircleConfig.rainbow) color = Color.getHSBColor(i / 360f, 1f, 1f);
                else color = JumpCircleConfig.color.toJavaColor();

                double x = (dif * JumpCircleConfig.radius * 0.001 * Math.sin(Math.toRadians(i)));
                double z = (dif * JumpCircleConfig.radius * 0.001 * Math.cos(Math.toRadians(i)));

                RenderUtils.Helper.glColor(color.getRed(), color.getGreen(), color.getBlue(), 0);
                GL11.glVertex3d(x / 2, 0.0, z / 2);

                RenderUtils.Helper.glColor(color.getRed(), color.getGreen(), color.getBlue(), c);
                GL11.glVertex3d(x, 0.0, z);
            }
            GL11.glEnd();

            GL11.glPopMatrix();
        }
    }

}
