package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;

public class BlockOverlayConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = false;

    //@Switch(name = "Show Block Name")
    //public static boolean showBlockName = false;

    //@Switch(name = "Infinite distance", description = "Shows the block overlay even if you are far away from the block")
    //public static boolean infiniteDistance = false;

    @Slider(name = "Line Width", min = 0.1F, max = 5.0F)
    public static float lineWidth = 1.0F;

    @Dropdown(
            name = "Mode",        // name of the component
            options = {"Hidden", "Normal", "Side", "Full"} // options
    )
    public static int mode = 1;
/*
    @Switch(name = "Animate",description = "Animate the box going from on block to another. Try it yourself!")
    public static boolean animate = false;

 */

    @Switch(name = "Hide plants")
    public static boolean hidePlants = false;

    @Switch(name = "Barriers")
    public static boolean barriers = true;

    @Switch(name = "Ignore depth", description = "Ignore depth when rendering the block overlay")
    public static boolean ignoreDepth = false;

    @Switch(name = "Persistent")
    public static boolean persistent = false;

    @Switch(name = "Gradient")
    public static boolean gradient = false;

    @Switch(name = "Overlay Color")
    public static boolean overlayColor = false;

    @Switch(name = "Outline Color")
    public static boolean outlineColor = true;

    @Color(name = "First Color Overlay")
    public static OneColor colorFO = new OneColor(new java.awt.Color(255, 255, 255, 255));

    @Color(name = "Second Color Overlay")
    public static OneColor colorSO = new OneColor(new java.awt.Color(255, 255, 255, 255));

    @Color(name = "First Color Outline")
    public static OneColor colorFU = new OneColor(new java.awt.Color(255, 255, 255, 255));

    @Color(name = "Second Color Outline")
    public static OneColor colorSU = new OneColor(new java.awt.Color(255, 255, 255, 255));

}
