package me.anemoi.rendertools.command;

import me.anemoi.rendertools.RenderTools;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

@Command(value = RenderTools.MODID, description = "Access the " + RenderTools.NAME + " GUI.")
public class ExampleCommand {

    @Main
    private static void main() {
        RenderTools.INSTANCE.config.openGui();
    }
}