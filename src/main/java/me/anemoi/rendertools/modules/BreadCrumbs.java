package me.anemoi.rendertools.modules;

import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import me.anemoi.rendertools.config.modules.BreadCrumbsConfig;
import me.anemoi.rendertools.events.Render3DEvent;
import me.anemoi.rendertools.utils.RenderUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.anemoi.rendertools.RenderTools.mc;

/*
Credits to FDPClient for the original code
https://github.com/SkidderMC/FDPClient/blob/main/LICENSE
if there is any issue with this code please contact me on discord: Anemoi#7990
 */
public class BreadCrumbs {
    private HashMap<Integer, List<BreadCrumbPoint>> points = new HashMap<>();

    private static int sphereList = GL11.glGenLists(1);

    static {
        GL11.glNewList(sphereList, GL11.GL_COMPILE);

        Sphere shaft = new Sphere();
        shaft.setDrawStyle(GLU.GLU_FILL);
        shaft.draw(0.3f, 25, 10);

        GL11.glEndList();
    }


    @Subscribe
    public void onRender(Render3DEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (!BreadCrumbsConfig.toggled) return;
        if (BreadCrumbsConfig.only3dPerson && mc.gameSettings.thirdPersonView == 0) return;

        long fTime = BreadCrumbsConfig.fadeTime * 1000L;
        long fadeSec = System.currentTimeMillis() - fTime;
        float colorAlpha = BreadCrumbsConfig.color.toJavaColor().getAlpha() / 255f;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        mc.entityRenderer.disableLightmap();
        double renderPosX = mc.getRenderManager().viewerPosX;
        double renderPosY = mc.getRenderManager().viewerPosY;
        double renderPosZ = mc.getRenderManager().viewerPosZ;

        for (int j = 0; j < points.size(); j++) {
            List<BreadCrumbPoint> list = points.get(j);
            int id = (int) points.keySet().toArray()[j];
            float lastPosX = 114514.0F;
            float lastPosY = 114514.0F;
            float lastPosZ = 114514.0F;

            switch (BreadCrumbsConfig.mode) {
                case 0:
                    GL11.glLineWidth(BreadCrumbsConfig.width);
                    GL11.glEnable(GL11.GL_LINE_SMOOTH);
                    GL11.glBegin(GL11.GL_LINE_STRIP);
                    break;
                case 1:
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    break;
            }
            for (int i = 0; i < list.size(); i++) {
                BreadCrumbPoint point = list.get(i);
                if (point == null) continue;
                //start
                float alpha;
                if (BreadCrumbsConfig.fadeValue) {
                    float pct = ((float) (point.getTime() - fadeSec)) / fTime;
                    if (pct < 0 || pct > 1) {
                        list.remove(point);
                        continue;
                    }
                    alpha = pct;
                } else {
                    alpha = 1f;
                }
                alpha *= colorAlpha;
                //end
                if (BreadCrumbsConfig.mode != 3) {
                    RenderUtils.glColor(point.getColor(), alpha);
                }
                switch (BreadCrumbsConfig.mode) {
                    case 0:
                        GL11.glPushMatrix();
                        GL11.glVertex3d(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ);
                        GL11.glPopMatrix();
                        break;
                    case 1:
                        if (!(lastPosX == 114514.0 && lastPosY == 114514.0 && lastPosZ == 114514.0)) {
                            GL11.glBegin(GL11.GL_QUADS);
                            GL11.glVertex3d(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ);
                            GL11.glVertex3d(lastPosX, lastPosY, lastPosZ);
                            GL11.glVertex3d(lastPosX, lastPosY + mc.thePlayer.height, lastPosZ);
                            GL11.glVertex3d(point.x - renderPosX, point.y - renderPosY + mc.thePlayer.height, point.z - renderPosZ);
                            GL11.glEnd();
                        }
                        lastPosX = (float) (point.x - renderPosX);
                        lastPosY = (float) (point.y - renderPosY);
                        lastPosZ = (float) (point.z - renderPosZ);
                        break;
                    case 2:
                        GL11.glPushMatrix();
                        GL11.glTranslated(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ);
                        GL11.glScalef(BreadCrumbsConfig.sphereScale, BreadCrumbsConfig.sphereScale, BreadCrumbsConfig.sphereScale);
                        GL11.glCallList(sphereList);
                        GL11.glPopMatrix();
                        break;
                    case 3:
                        float circleScale = BreadCrumbsConfig.sphereScale;

                        RenderUtils.glColor(point.color, 30);
                        GL11.glPushMatrix();
                        GL11.glTranslated(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ);
                        GL11.glScalef(circleScale * 1.3f, circleScale * 1.3f, circleScale * 1.3f);
                        GL11.glCallList(sphereList);
                        GL11.glPopMatrix();

                        RenderUtils.glColor(point.color, 50);
                        GL11.glPushMatrix();
                        GL11.glTranslated(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ);
                        GL11.glScalef(circleScale * 0.8f, circleScale * 0.8f, circleScale * 0.8f);
                        GL11.glCallList(sphereList);
                        GL11.glPopMatrix();

                        RenderUtils.glColor(point.color, alpha);
                        GL11.glPushMatrix();
                        GL11.glTranslated(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ);
                        GL11.glScalef(circleScale * 0.4f, circleScale * 0.4f, circleScale * 0.4f);
                        GL11.glCallList(sphereList);
                        GL11.glPopMatrix();
                        break;
                }

                switch (BreadCrumbsConfig.mode) {
                    case 0:
                        GL11.glEnd();
                        GL11.glDisable(GL11.GL_LINE_SMOOTH);
                        break;
                    case 1:
                        GL11.glEnable(GL11.GL_CULL_FACE);
                        break;
                }
            }
        }

        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !BreadCrumbsConfig.toggled) return;
        for (int i = 0; i < points.size(); i++) {
            int id = (int) points.keySet().toArray()[i];
            if (mc.theWorld.getEntityByID(id) == null){
                points.remove(id);
            }
        }
        /*
        points.forEach((id, list) -> {
            if (mc.theWorld.getEntityByID(id) == null){
                points.remove(id);
            }
        });
         */

        if (mc.thePlayer.ticksExisted % BreadCrumbsConfig.precision != 0) {
            return;
        }
        if (BreadCrumbsConfig.drawOthers) {
            mc.theWorld.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityLivingBase && entity != mc.thePlayer) {
                    updatePoints((EntityLivingBase) entity);
                }
            });
        }
        if (BreadCrumbsConfig.drawOwn) {
            updatePoints(mc.thePlayer);
        }
    }

    private void updatePoints(EntityLivingBase entity) {
        //make following code from kotlin to java (points[entity.entityId] ?: mutableListOf<BreadcrumbPoint>().also { points[entity.entityId] = it }).add(BreadcrumbPoint(entity.posX, entity.entityBoundingBox.minY, entity.posZ, System.currentTimeMillis(), color.rgb))
        /*
        points.computeIfAbsent(entity.getEntityId(), k -> new ArrayList<BreadCrumbPoint>());
        try {
            points.get(entity.getEntityId()).add(new BreadCrumbPoint(entity.posX, entity.posY, entity.posZ, System.currentTimeMillis(), BreadCrumbsConfig.color.toJavaColor().getRGB()));
        }catch (Exception e){
            e.printStackTrace();
        }*/
        //boolean ? true : false
        if (points.get(entity.getEntityId()) == null) {
            points.put(entity.getEntityId(), new ArrayList<BreadCrumbPoint>());
        }
        points.get(entity.getEntityId()).add(new BreadCrumbPoint(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ, System.currentTimeMillis(), BreadCrumbsConfig.color.toJavaColor().getRGB()));
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        points.clear();
    }

    public static class BreadCrumbPoint {
        private double x;
        private double y;
        private double z;
        private long time;
        private int color;

        public BreadCrumbPoint(double x, double y, double z, long time, int color) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.time = time;
            this.color = color;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
