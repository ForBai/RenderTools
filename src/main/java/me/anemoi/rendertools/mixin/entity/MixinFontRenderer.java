package me.anemoi.rendertools.mixin.entity;

import me.anemoi.rendertools.RenderTools;
import me.anemoi.rendertools.config.modules.NickHiderConfig;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {FontRenderer.class}, priority = 1000)
public abstract class MixinFontRenderer {
    @Shadow
    protected abstract void renderStringAtPos(String text, boolean shadow);

    @Shadow
    public abstract int getStringWidth(String text);

    @Shadow
    public abstract int getCharWidth(char character);

    @Inject(method = "renderStringAtPos", at = @At(value = "HEAD"), cancellable = true)
    private void renderStringAtPos(String text, boolean shadow, CallbackInfo ci) {
        //do noting else not work

        if (NickHiderConfig.toggled && text.contains(RenderTools.mc.getSession().getUsername())) {
            ci.cancel();
            this.renderStringAtPos(text.replaceAll(RenderTools.mc.getSession().getUsername(), NickHiderConfig.name), shadow);

        }
    }

    @Inject(method = "getStringWidth", at = @At(value = "RETURN"), cancellable = true)
    private void getStringWidth(String text, CallbackInfoReturnable<Integer> cir) {
        //do noting else not work

        if (text != null && NickHiderConfig.toggled && text.contains(RenderTools.mc.getSession().getUsername())) {
            cir.setReturnValue(this.getStringWidth(text.replaceAll(RenderTools.mc.getSession().getUsername(), NickHiderConfig.name)));
        }
    }
}