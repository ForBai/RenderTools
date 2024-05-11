package me.anemoi.rendertools.mixin.item;


import me.anemoi.rendertools.config.modules.ItemPhysicsConfig;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {RenderEntityItem.class}, priority = 20000)
public abstract class MixinRenderEntityItem extends Render<EntityItem> {

    /*
    TODO: FIX item sizes
    if (ibakedmodel.isGui3d()) {
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
            }
     */

    @Shadow
    protected abstract int func_177078_a(final ItemStack p0);

    protected MixinRenderEntityItem(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method = "doRender*", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.pushMatrix()V", shift = At.Shift.AFTER, ordinal = 1))
    private void scaleItemDrop(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        GlStateManager.scale(ItemPhysicsConfig.itemSize, ItemPhysicsConfig.itemSize, ItemPhysicsConfig.itemSize);
    }

    /**
     * @author anemoi
     * @reason Item Physics
     */
    @Overwrite
    private int func_177077_a(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_) {
        ItemStack itemstack = itemIn.getEntityItem();
        Item item = itemstack.getItem();

        if (item == null) {
            return 0;
        } else {
            boolean flag = p_177077_9_.isGui3d();
            int i = this.func_177078_a(itemstack);
            float f = 0.25F;
            float f1 = MathHelper.sin(((float) itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F;
            if (ItemPhysicsConfig.itemPhysics) {
                f1 = 0.0f;
            }
            float f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.translate((float) p_177077_2_, (float) p_177077_4_ + f1 + 0.25F * f2, (float) p_177077_6_);

            if (flag || this.renderManager.options != null) {
                float f3 = (((float) itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * (180F / (float) Math.PI);
                if (ItemPhysicsConfig.itemPhysics) {
                    if (itemIn.onGround) {
                        GL11.glRotatef(itemIn.rotationYaw, 0.0f, 1.0f, 0.0f);
                        GL11.glRotatef(itemIn.rotationPitch + 90.0f, 1.0f, 0.0f, 0.0f);
                    } else {
                        for (int a = 0; a < 10; ++a) {
                            GL11.glRotatef(f3, ItemPhysicsConfig.strength / 10f, ItemPhysicsConfig.strength / 10f, 0.0f);
                        }
                    }
                } else {
                    GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
                }
            }

            if (!flag) {
                float f6 = -0.0F * (float) (i - 1) * 0.5F;
                float f4 = -0.0F * (float) (i - 1) * 0.5F;
                float f5 = -0.046875F * (float) (i - 1) * 0.5F;
                GlStateManager.translate(f6, f4, f5);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }

}
