package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;

public class GiantsConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = false;

    //size
    @Slider(name = "Size", min = 0.1F, max = 5.0F)
    public static float size = 1.0F;

}
