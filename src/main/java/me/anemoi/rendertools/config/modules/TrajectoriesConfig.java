package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.InfoType;

public class TrajectoriesConfig {
    @Switch(name = "Toggled", description = "Toggles the Trajectories module")
    public static boolean toggled = false;


    @Color(
            name = "Color"
    )
    public static OneColor color = new OneColor(new java.awt.Color(255, 0, 0, 255));


    @Switch(name = "Render Others")
    public static boolean renderOther = false;

    @Slider(name = "Width", min = 1, max = 20)
    public static float width = 1;
    /*
        @Slider(name = "Inner Size",min = -5,max = 5)
        public static float innerSize = 1;
    */
    @Slider(name = "Slices", min = 2, max = 50)
    public static float slices = 3;

    @Info(
            text = "Warning: This module could cause to get you banned on some servers.",
            type = InfoType.WARNING
    )
    public static boolean ignored;

    @Info(
            text = "If you want to use this dm Anemoi#7990 ",
            type = InfoType.WARNING
    )
    public static boolean ignored2;

}
