package me.anemoi.rendertools.mixin.entity;

import me.anemoi.rendertools.config.modules.HitAnimationConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = {EntityLivingBase.class}, priority = 20000)
public abstract class MixinEntityLivingBase extends EntitiyMixin {

    @Shadow
    public abstract PotionEffect getActivePotionEffect(Potion potionIn);

    @Shadow
    public abstract boolean isPotionActive(Potion potionIn);


    /**
     * @author Liuli
     */
    @Overwrite
    private int getArmSwingAnimationEnd() {
        int speed = HitAnimationConfig.ignoreHaste ? 6 : this.isPotionActive(Potion.digSpeed) ? 6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) : (this.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);

        //if (this.equals(Minecraft.getMinecraft().thePlayer)) {
        speed = (int) (speed * HitAnimationConfig.swingSpeed);
        //}

        return speed;
    }


}