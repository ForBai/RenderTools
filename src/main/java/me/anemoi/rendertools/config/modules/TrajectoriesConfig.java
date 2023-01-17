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

    @Switch(name = "Highlight Block", subcategory = "Block", description = "Highlights the block the projectile will hit")
    public static boolean highlightBlock = true;
    @Switch(name = "Facing", subcategory = "Block", description = "Highlights the block the projectile will hit")
    public static boolean facing = true;
    @Switch(name = "Vector", subcategory = "Vector",description = "Draws a vector from the player to the block the projectile will hit")
    public static boolean vector = true;
    @Slider(name = "Radius", subcategory = "Vector",min = 0.1F, max = 1.0F)
    public static float radius = 0.1F;
    @Slider(name = "Slices", subcategory = "Vector",min = 3, max = 24, step = 1)
    public static int slices = 8;
    @Color(name = "Fill", subcategory = "Color's",description = "The color of the fill of the block")
    public static OneColor fillColor = new OneColor(0x22d81919);
    @Color(name = "Outline", subcategory = "Color's",description = "The color of the outline of the block")
    public static OneColor outlineColor = new OneColor(0xFFd81919);
    @Color(name = "Line", subcategory = "Color's",description = "The color of the line of the block")
    public static OneColor lineColor = new OneColor(0xFFd81919);
    @Color(name = "Vector", subcategory = "Color's",description = "The color of the vector of the block")
    public static OneColor vectorColor = new OneColor(0xFFd81919);
    @Color(name = "Self Fill", subcategory = "Color's",description = "The color of the fill of the block")
    public static OneColor selfFillColor = new OneColor(0x2250b4b4);
    @Color(name = "Self Outline", subcategory = "Color's",description = "The color of the outline of the block")
    public static OneColor selfOutlineColor = new OneColor(0xFF50b4b4);
    @Color(name = "Self Line", subcategory = "Color's",description = "The color of the line of the block")
    public static OneColor selfLineColor = new OneColor(0xFF50b4b4);
    @Color(name = "Self Vector", subcategory = "Color's",description = "The color of the vector of the block")
    public static OneColor selfVectorColor = new OneColor(0xFF50b4b4);
    @Slider(name = "Line Width", subcategory = "Width's",min = 0.1F, max = 10.0F)
    public static float lineWidth = 3f;
    @Slider(name = "Outline Width", subcategory = "Width's",min = 0.1F, max = 10.0F)
    public static float outlineWidth = 1.5f;
    @Slider(name = "Vector Width", subcategory = "Width's",min = 0.1F, max = 10.0F)
    public static float vectorWidth = 1.5F;



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
