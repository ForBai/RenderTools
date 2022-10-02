package me.anemoi.rendertools.mixin.entity;


import me.anemoi.rendertools.config.modules.GiantsConfig;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderPlayer.class})
public abstract class MixinRenderPlayer {
    @Inject(method={"preRenderCallback(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V"}, at={@At(value="HEAD")})
    public void onPreRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime, CallbackInfo ci) {
        if (GiantsConfig.toggled) {
            GlStateManager.scale(GiantsConfig.size, (double)GiantsConfig.size, GiantsConfig.size);
        }
    }
}
