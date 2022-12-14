package me.anemoi.rendertools.mixin.item;

import net.minecraft.item.ItemFood;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = {ItemFood.class}, priority = 20000)
public interface ItemFoodAccessor {
    @Accessor(value = "alwaysEdible")
    public boolean getAlwaysEdible();
}
