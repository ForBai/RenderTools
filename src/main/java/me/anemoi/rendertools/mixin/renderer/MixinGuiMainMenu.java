package me.anemoi.rendertools.mixin.renderer;

import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.font.Font;
import cc.polyfrost.oneconfig.renderer.font.Fonts;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

import static me.anemoi.rendertools.RenderTools.mc;
import static org.lwjgl.opengl.GL11.*;

//@Mixin(value = {GuiMainMenu.class}, priority = 20000)
public class MixinGuiMainMenu {
/*    @Shadow
    private String splashText;


    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawMainMenu();
    }


    @Overwrite
    public void initGui() {
        //none
    }

    private static void drawMainMenu(){
        int screenHeight = mc.displayHeight;
        int screenWidth = mc.displayWidth;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        NanoVGHelper.INSTANCE.setupAndDraw(vg -> {
            //backround
            NanoVGHelper.INSTANCE.drawGradientRect(vg,0,0,screenWidth,screenHeight,new Color(92, 255, 193).getRGB(),new Color(47, 151, 255).getRGB());
            //title
            NanoVGHelper.INSTANCE.drawText(vg,"Minecraft",10,55,Color.black.getRGB(), 100, Fonts.REGULAR);
            NanoVGHelper.INSTANCE.drawDropShadow(vg,0,0,475,100,5,5,5);
        });
//        RenderUtilsNew.drawRect(25,25,100,100,Color.red.getRGB());
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
    }
*/

}
