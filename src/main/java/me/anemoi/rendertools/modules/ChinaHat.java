package me.anemoi.rendertools.modules;

import me.anemoi.rendertools.RenderTools;
import me.anemoi.rendertools.config.modules.ChinaHatConfig;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ChinaHat {
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (ChinaHatConfig.toggled && (RenderTools.mc.gameSettings.thirdPersonView != 0 || ChinaHatConfig.firstPerson)) {
            this.drawChinaHat(RenderTools.mc.thePlayer, event.partialTicks);
        }
    }

    private void drawChinaHat(EntityLivingBase entity, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GL11.glTranslated(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - RenderTools.mc.getRenderManager().viewerPosX, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - RenderTools.mc.getRenderManager().viewerPosY + (double) entity.height + (entity.isSneaking() ? ChinaHatConfig.pos - 0.2 : ChinaHatConfig.pos), entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - RenderTools.mc.getRenderManager().viewerPosZ);
        GL11.glRotatef((float) ((double) ((float) entity.ticksExisted + partialTicks) * ChinaHatConfig.rotation), 0.0f, 1.0f, 0.0f);
        double radius = ChinaHatConfig.radius;
        GL11.glBegin(3);
        int i = 0;
        while ((double) i <= ChinaHatConfig.angles) {
            Color color = this.getColor(i, (int) ChinaHatConfig.angles, false);
            GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, 1.0f);
            GL11.glVertex3d(Math.cos((double) i * Math.PI / (ChinaHatConfig.angles / 2.0)) * radius, 0.0, Math.sin((double) i * Math.PI / (ChinaHatConfig.angles / 2.0)) * radius);
            ++i;
        }
        GL11.glEnd();
        GL11.glBegin(6);
        Color c1 = this.getColor(0.0, ChinaHatConfig.angles, true);
        GL11.glColor4f((float) c1.getRed() / 255.0f, (float) c1.getGreen() / 255.0f, (float) c1.getBlue() / 255.0f, 0.5f);
        GL11.glVertex3d(0.0, ChinaHatConfig.height, 0.0);
        int i2 = 0;
        while ((double) i2 <= ChinaHatConfig.angles) {
            Color color = this.getColor(i2, (int) ChinaHatConfig.angles, false);
            GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, 0.5f);
            GL11.glVertex3d(Math.cos((double) i2 * Math.PI / (ChinaHatConfig.angles / 2.0)) * radius, 0.0, Math.sin((double) i2 * Math.PI / (ChinaHatConfig.angles / 2.0)) * radius);
            ++i2;
        }
        GL11.glVertex3d(0.0, ChinaHatConfig.height, 0.0);
        GL11.glEnd();
        GL11.glLineWidth(2.0f);
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GlStateManager.resetColor();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public Color getColor(double i, double max, boolean first) {
        switch (ChinaHatConfig.mode) {
            case 2: {
                return Color.getHSBColor((float) (i / max), 1.0f, 1.0f);
            }
            case 1: {
                if (first) {
                    return new Color((int) ChinaHatConfig.color.getRed(), (int) ChinaHatConfig.color.getGreen(), (int) ChinaHatConfig.color.getBlue());
                }
                return new Color((int) ChinaHatConfig.color2.getRed(), (int) ChinaHatConfig.color2.getGreen(), (int) ChinaHatConfig.color2.getBlue());
            }
        }
        if (first) {
            return new Color((int) ChinaHatConfig.color.getRed(), (int) ChinaHatConfig.color.getGreen(), (int) ChinaHatConfig.color.getBlue()).darker().darker();
        }
        return new Color((int) ChinaHatConfig.color.getRed(), (int) ChinaHatConfig.color.getGreen(), (int) ChinaHatConfig.color.getBlue());
    }
}
