package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;

public class AnimationsConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = true;

    @Slider(name="x", min=0.01F,max = 3.0F)
    public static float x = 1.0F;

    @Slider(name="y", min=0.01F,max = 3.0F)
    public static float y = 1.0F;

    @Slider(name="z", min=0.01F,max = 3.0F)
    public static float z = 1.0F;

    @Slider(name="size", min=0.01F,max = 3.0F)
    public static float size = 1.0F;

    @Dropdown(name = "Mode", options = {"1.7", "chill", "push", "spin", "vertical spin", "helicopter","custom","long hit"})
    public static int mode = 0;

    @Switch(name = "Swing progress")
    public static boolean swingProgress = false;
}
