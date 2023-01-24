package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.libs.checker.units.qual.Angle;

public class HitAnimationConfig {
    //@Switch(name = "Toggled")
    //public static boolean toggled = true;

    @Slider(name = "Swing Speed", min = 0.5F, max = 5.0F)
    public static float swingSpeed = 1.0F;

    @Switch(
            name = "Ignore Haste/Mining Fatigue",
            description = "Ignore haste and mining fatigue effects when calculating the swing speed."
    )
    public static boolean ignoreHaste = false;

    @Dropdown(
            name = "Swing Type",
            description = "The type of swing animation to use.",
            options = {
                    "Vanilla",//0
                    "Custom",//1
                    "Slide",//2
                    "Custom 2",//3
                    "Custom 3",//4
                    "Custom 4"//5
            }
    )
    public static int swingType = 0;

}
