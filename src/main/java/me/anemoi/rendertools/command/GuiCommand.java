package me.anemoi.rendertools.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import me.anemoi.rendertools.RenderTools;

@Command(value = RenderTools.MODID, description = "Access the " + RenderTools.NAME + " GUI.")
public class GuiCommand {

    @Main
    private static void main() {
        RenderTools.INSTANCE.config.openGui();
    }
}