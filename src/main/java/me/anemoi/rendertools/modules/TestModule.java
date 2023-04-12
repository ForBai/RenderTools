package me.anemoi.rendertools.modules;

public class TestModule {
    //private BoundedAnimation anim = new BoundedAnimation(1, 500, 2000, false, Easing.CUBIC_IN_OUT);
/*
    @SubscribeEvent
    public void onRenderWordl(RenderWorldLastEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !TestConfig.toggled) return;
        //anim.setState(true);

        //RenderUtilsNew.renderCircle(new Point3dD(mc.thePlayer.getPositionVector()), (float) anim.getAnimationFactor(), new Color(255, 0, 0, 255), event.partialTicks);

    }

    @Subscribe
    public void onRenderScreen(HudRenderEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null || !TestConfig.toggled) return;
        /*RenderManager.setupAndDraw(l -> {
            anim.setState(true);
            RenderManager.drawRect(l, (float) anim.getAnimationFactor(), 10, 20, 20, new Color(255, 0, 0, 255).getRGB());
            if (Mouse.isButtonDown(0)) {
                anim.resetToDefault();
            }
        });
         */
    /*
    }
    */

}
