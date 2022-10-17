package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class TrajectoriesConfig {
    @Switch(name = "Toggled", description = "Toggles the Trajectories module")
    public static boolean toggled = false;


    @Color(
            name = "Color"
    )
    public static OneColor color = new OneColor(new java.awt.Color(255, 0, 0, 255));


    @Switch(name = "Render Others")
    public static boolean renderOther = false;

    @Slider(name = "Width",min = 1,max = 20)
    public static float width = 1;
/*
    @Slider(name = "Inner Size",min = -5,max = 5)
    public static float innerSize = 1;
*/
    @Slider(name = "Slices",min = 2,max = 50)
    public static float slices = 3;

}
