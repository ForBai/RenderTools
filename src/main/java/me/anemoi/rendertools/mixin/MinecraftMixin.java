package me.anemoi.rendertools.mixin;

import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {Minecraft.class}, priority = 20000)
public class MinecraftMixin {

    @Inject(method = "startGame", at = @At(value = "HEAD"))
    private void onStartGame(CallbackInfo ci) {
        //for (int i = 0; i < 20; i++) {
        System.out.println("Hello, world!");
        //}
    }

    long lastFrame = getTime();

    long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    @Inject(method = "runGameLoop", at = @At(value = "HEAD"))
    private void onRunGameLoop(CallbackInfo ci) {
        //--Delta--//
        /*
        long currentTime = getTime();
        int deltaTime = (int) (currentTime - lastFrame);
        lastFrame = currentTime;
        Delta.DELTATIME = deltaTime;
        System.out.println(Delta.DELTATIME);
         */
        //--Delta--//
    }

}
