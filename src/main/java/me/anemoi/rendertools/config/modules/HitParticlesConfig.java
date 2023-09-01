package me.anemoi.rendertools.config.modules;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;


public class HitParticlesConfig {
    @Switch(name = "Toggled")
    public static boolean toggled = false;

    @Slider(name = "Amount", min = 1, max = 20)
    public static int amount = 10;

    @Slider(name = "Exist Time", min = 250, max = 20000, step = 250)
    public static int time = 1500;

    @Slider(name = "Max Particles", min = 25, max = 1000, step = 25)
    public static int maxParticles = 100;

    @Switch(name = "Physics")
    public static boolean physics = true;

    @Switch(name = "Better Rainbow")
    public static boolean betterRainbow = true;

    @Color(name = "Color")
    public static OneColor color = new OneColor(new java.awt.Color(255, 255, 0, 255));

    @Switch(name = "On Break Block")
    public static boolean onBreakBlock = true;

    @Switch(name = "On Hit Player")
    public static boolean onHitPlayer = true;

    @Slider(name = "Show Circle 2 Distance", description = "The max distance from the player to the Particle to show the second circle", min = 0, max = 50 * 10)
    public static int showCircle2Distance = 4;

    @Slider(name = "Show Circle 3 Distance", description = "The max distance from the player to the Particle to show the third circle", min = 0, max = 50 * 10)
    public static int showCircle3Distance = 20;
}
