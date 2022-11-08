package me.anemoi.rendertools.mixin.entity;

import me.anemoi.rendertools.config.MainConfig;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {EntityPlayerSP.class}, priority = 20000)
public class MixinEntityPlayerSP {
    @Inject(method = {"pushOutOfBlocks"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void pushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        //this is a feature from a newer version of minecraft so it cant get you banned
        cir.setReturnValue(MainConfig.pushOutOfBlocks);
    }

}
