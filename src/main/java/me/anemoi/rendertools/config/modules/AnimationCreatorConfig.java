package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;

public class AnimationCreatorConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = false;

    @Switch(name = "Swing progress")
    public static boolean swingProgress = false;

    @Switch(name = "Block progress")
    public static boolean blockProgress = true;

    @Slider(name = "angle1", min = -180.0F, max = 180.0F)
    public static float angle1 = 30.0F;
    @Slider(name = "angle2", min = -180.0F, max = 180.0F)
    public static float angle2 = -80.0F;
    @Slider(name = "angle3", min = -180.0F, max = 180.0F)
    public static float angle3 = 60.0F;

    @Slider(name = "translateX", min = -5.0F, max = 5.0F)
    public static float translateX = -0.5F;
    @Slider(name = "translateY", min = -5.0F, max = 5.0F)
    public static float translateY = 0.2F;
    @Slider(name = "translateZ", min = -5.0F, max = 5.0F)
    public static float translateZ = 0.0F;

    @Slider(name = "rotateX1", min = -5.0F, max = 5.0F)
    public static float rotation1x = 0.0F;
    @Slider(name = "rotateY1", min = -5.0F, max = 5.0F)
    public static float rotation1y = 1.0F;
    @Slider(name = "rotateZ1", min = -5.0F, max = 5.0F)
    public static float rotation1z = 0.0F;

    @Slider(name = "rotateX2", min = -5.0F, max = 5.0F)
    public static float rotation2x = 1.0F;
    @Slider(name = "rotateY2", min = -5.0F, max = 5.0F)
    public static float rotation2y = 0.0F;
    @Slider(name = "rotateZ2", min = -5.0F, max = 5.0F)
    public static float rotation2z = 0.0F;
}
