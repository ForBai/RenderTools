package me.anemoi.rendertools.modules

import me.anemoi.rendertools.RenderTools.mc
import me.anemoi.rendertools.config.modules.BreadCrumbsConfig
import me.anemoi.rendertools.utils.RenderUtils
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.GLU
import org.lwjgl.util.glu.Sphere

class BreadCrumbsNew {
    private val points = mutableMapOf<Int, MutableList<BreadcrumbPoint>>()

    private val sphereList = GL11.glGenLists(1)

    init {
        GL11.glNewList(sphereList, GL11.GL_COMPILE)

        val shaft = Sphere()
        shaft.drawStyle = GLU.GLU_FILL
        shaft.draw(0.3f, 25, 10)

        GL11.glEndList()
    }

    @SubscribeEvent
    fun onRender3D(event: RenderWorldLastEvent) {
        if (mc.thePlayer == null || mc.theWorld == null || !BreadCrumbsConfig.toggled) return
        if (BreadCrumbsConfig.only3dPerson && mc.gameSettings.thirdPersonView == 0) return

        val fTime = BreadCrumbsConfig.fadeTime * 1000
        val fadeSec = System.currentTimeMillis() - fTime
        val colorAlpha = BreadCrumbsConfig.alpha / 255.0f

        GL11.glPushMatrix()
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        mc.entityRenderer.disableLightmap()
        val renderPosX = mc.renderManager.viewerPosX
        val renderPosY = mc.renderManager.viewerPosY
        val renderPosZ = mc.renderManager.viewerPosZ
        points.forEach { (_, mutableList) ->
            var lastPosX = 114514.0
            var lastPosY = 114514.0
            var lastPosZ = 114514.0
            when (BreadCrumbsConfig.mode) {
                0 -> {
                    GL11.glLineWidth(BreadCrumbsConfig.lineWidth.toFloat())
                    GL11.glEnable(GL11.GL_LINE_SMOOTH)
                    GL11.glBegin(GL11.GL_LINE_STRIP)
                }

                1 -> {
                    GL11.glDisable(GL11.GL_CULL_FACE)
                }
            }
            for (point in mutableList.reversed()) {
                val alpha = if (BreadCrumbsConfig.fadeValue) {
                    val pct = (point.time - fadeSec).toFloat() / fTime
                    if (pct < 0 || pct > 1) {
                        mutableList.remove(point)
                        continue
                    }
                    pct
                } else {
                    1f
                } * colorAlpha
                if (BreadCrumbsConfig.mode != 3) {
                    RenderUtils.glColor(point.color, alpha)
                }
                when (BreadCrumbsConfig.mode) {
                    0 -> GL11.glVertex3d(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ)
                    1 -> {
                        if (!(lastPosX == 114514.0 && lastPosY == 114514.0 && lastPosZ == 114514.0)) {
                            GL11.glBegin(GL11.GL_QUADS)
                            GL11.glVertex3d(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ)
                            GL11.glVertex3d(lastPosX, lastPosY, lastPosZ)
                            GL11.glVertex3d(lastPosX, lastPosY + mc.thePlayer.height, lastPosZ)
                            GL11.glVertex3d(
                                point.x - renderPosX,
                                point.y - renderPosY + mc.thePlayer.height,
                                point.z - renderPosZ
                            )
                            GL11.glEnd()
                        }
                        lastPosX = point.x - renderPosX
                        lastPosY = point.y - renderPosY
                        lastPosZ = point.z - renderPosZ
                    }

                    2 -> {
                        GL11.glPushMatrix()
                        GL11.glTranslated(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ)
                        GL11.glScalef(
                            BreadCrumbsConfig.sphereScale,
                            BreadCrumbsConfig.sphereScale,
                            BreadCrumbsConfig.sphereScale
                        )
                        GL11.glCallList(sphereList)
                        GL11.glPopMatrix()
                    }

                    3 -> {

                        val circleScale = BreadCrumbsConfig.sphereScale
                        RenderUtils.glColor(point.color, 38)
                        GL11.glPushMatrix()
                        GL11.glTranslated(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ)
                        GL11.glScalef(circleScale * 2.3f, circleScale * 2.3f, circleScale * 2.3f)
                        GL11.glCallList(sphereList)
                        GL11.glPopMatrix()

                        RenderUtils.glColor(point.color, 63)
                        GL11.glPushMatrix()
                        GL11.glTranslated(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ)
                        GL11.glScalef(circleScale * 1.4f, circleScale * 1.4f, circleScale * 1.4f)
                        GL11.glCallList(sphereList)
                        GL11.glPopMatrix()

                        RenderUtils.glColor(point.color, 153)
                        GL11.glPushMatrix()
                        GL11.glTranslated(point.x - renderPosX, point.y - renderPosY, point.z - renderPosZ)
                        GL11.glScalef(circleScale * 0.7f, circleScale * 0.7f, circleScale * 0.7f)
                        GL11.glCallList(sphereList)
                        GL11.glPopMatrix()

                    }
                }
            }
            when (BreadCrumbsConfig.mode) {
                0 -> {
                    GL11.glEnd()
                    GL11.glDisable(GL11.GL_LINE_SMOOTH)
                }

                1 -> {
                    GL11.glEnable(GL11.GL_CULL_FACE)
                }
            }
        }
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glPopMatrix()
    }

    @SubscribeEvent
    fun onUpdate(event: TickEvent.ClientTickEvent) {
        if (mc.thePlayer == null || mc.theWorld == null || !BreadCrumbsConfig.toggled) return
        // clear points for entities not exist
        points.forEach { (id, _) ->
            if (mc.theWorld.getEntityByID(id) == null) {
                points.remove(id)
            }
        }
        // add new points
        if (mc.thePlayer.ticksExisted % BreadCrumbsConfig.precision != 0) {
            return // skip if not on tick
        }
        if (BreadCrumbsConfig.drawOthers) {
            mc.theWorld.loadedEntityList.forEach {
                if (it is EntityPlayer && it !== mc.thePlayer) {
                    updatePoints(it as EntityLivingBase)
                }
            }
        }
        if (BreadCrumbsConfig.drawOwn) {
            updatePoints(mc.thePlayer)
        }
    }

    private fun updatePoints(entity: EntityLivingBase) {
        (points[entity.entityId] ?: mutableListOf<BreadcrumbPoint>().also { points[entity.entityId] = it })
            .add(
                BreadcrumbPoint(
                    entity.posX,
                    entity.entityBoundingBox.minY,
                    entity.posZ,
                    System.currentTimeMillis(),
                    BreadCrumbsConfig.color.rgb
                )
            )
    }

    @SubscribeEvent
    fun onWorld(event: WorldEvent.Load) {
        points.clear()
    }

    @SubscribeEvent
    fun onWorld(event: WorldEvent.Unload) {
        points.clear()
    }

    class BreadcrumbPoint(val x: Double, val y: Double, val z: Double, val time: Long, val color: Int)
}