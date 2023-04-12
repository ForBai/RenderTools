package me.anemoi.rendertools.mixin.item;

import me.anemoi.rendertools.config.modules.AnimationsConfig;
import me.anemoi.rendertools.config.modules.GlintColorConfig;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {RenderItem.class}, priority = 20000)
public abstract class MixinRenderItem {
    private static final ResourceLocation RESOURCE =
            new ResourceLocation("textures/rainbow.png");

    @Redirect(
            method = "renderEffect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V", ordinal = 0)
    )
    public void bindTexture(TextureManager textureManager,
                            ResourceLocation resource) {
        if (AnimationsConfig.rainbowEnchant) {
            textureManager.bindTexture(RESOURCE);
        } else {
            textureManager.bindTexture(resource);
        }
    }

    @ModifyConstant(method = "renderEffect", constant = @Constant(intValue = -8372020))
    private int modifyGlint(int constant) {
        if (GlintColorConfig.toggled) return GlintColorConfig.color.getRGB();
        else return constant;
    }

}
