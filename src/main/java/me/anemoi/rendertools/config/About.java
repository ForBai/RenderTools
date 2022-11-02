package me.anemoi.rendertools.config;

import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.data.InfoType;

public class About {
    @Info(
            text = "Hello ForBai here, this is the config for RenderTools, a mod that adds some cool features to Minecraft.",
            type = InfoType.INFO
    )
    public static boolean ignored;

    @Info(
            text = "But i couldn't have done it without the help of Polyfrost, he made the config system and the command system.",
            type = InfoType.INFO
    )
    public static boolean ignored2;

    @Info(
            text = "But also Big thanks to Oringo for much of the code of Many Modules.",
            type = InfoType.INFO
    )
    public static boolean ignored3;

    @Info(
            text = "Thanks For Reading, and have a nice day.",
            type = InfoType.INFO
    )
    public static boolean ignored4;
}
