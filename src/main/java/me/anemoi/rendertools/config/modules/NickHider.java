package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;

public class NickHider {
    @Switch(name = "Toggled")
    public static boolean toggled = false;

    @Text(
            name = "Name",
            placeholder = "Name?",
            secure = true,
            multiline = false
    )
    public static String name = "Troll";
}
