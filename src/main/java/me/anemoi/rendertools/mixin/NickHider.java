package me.anemoi.rendertools.mixin;

import me.anemoi.rendertools.RenderTools;
import me.anemoi.rendertools.config.modules.NickHiderConfig;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public abstract class NickHider {
    @Shadow
    protected abstract void renderStringAtPos(String var1, boolean var2);

    @Shadow
    public abstract int getStringWidth(String var1);

    @Shadow
    public abstract int getCharWidth(char var1);

    @Inject(method= "renderStringAtPos", at=@At(value="HEAD"), cancellable=true)
    private void renderString(String text, boolean shadow, CallbackInfo ci) {
        if (NickHiderConfig.toggled && text.contains(RenderTools.mc.getSession().getUsername())) {
            ci.cancel();
            this.renderStringAtPos(text.replaceAll(RenderTools.mc.getSession().getUsername(), NickHiderConfig.name), shadow);
        }
    }

    @Inject(method= "getStringWidth", at=@At(value="RETURN"), cancellable=true)
    private void getStringWidth(String text, CallbackInfoReturnable<Integer> cir) {
        if (text != null && NickHiderConfig.toggled && text.contains(RenderTools.mc.getSession().getUsername())) {
            cir.setReturnValue(this.getStringWidth(text.replaceAll(RenderTools.mc.getSession().getUsername(), NickHiderConfig.name)));
        }
    }
}