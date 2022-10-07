package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;

public class HitAnimationConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = true;

    @Slider(name = "Swing Speed", min = 0.5F, max = 5.0F)
    public static float swingSpeed = 1.0F;

}
