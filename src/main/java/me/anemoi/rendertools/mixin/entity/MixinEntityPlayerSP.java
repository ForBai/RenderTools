package me.anemoi.rendertools.mixin.entity;

import me.anemoi.rendertools.config.MainConfig;
import me.anemoi.rendertools.events.MotionEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {EntityPlayerSP.class}, priority = 20000)
public class MixinEntityPlayerSP {
    @Inject(method = {"pushOutOfBlocks"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void pushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        //this is a feature from a newer version of minecraft so it cant get you banned
        cir.setReturnValue(MainConfig.pushOutOfBlocks);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    public void onUpdateWalkingPlayerH(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new MotionEvent.Pre());
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("TAIL"))
    public void onUpdateWalkingPlayerT(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new MotionEvent.Post());
    }
}
