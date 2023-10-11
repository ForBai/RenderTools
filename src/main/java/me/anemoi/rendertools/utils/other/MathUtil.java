package me.anemoi.rendertools.utils.other;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.math.BigDecimal;
import java.math.RoundingMode;


/*
 Thanks to ionar2 for spidermod
 https://github.com/ionar2/spidermod/blob/master/LICENSE.md
 */
public class MathUtil {
    public static Vec3 interpolateEntity(Entity entity, float time) {
        return new Vec3(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time,
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time,
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }

    public static double radToDeg(double rad) {
        return rad * (float) (180.0f / Math.PI);
    }

    public static double degToRad(double deg) {
        return deg * (float) (Math.PI / 180.0f);
    }

    public static Vec3 direction(float yaw) {
        return new Vec3(Math.cos(degToRad(yaw + 90f)), 0, Math.sin(degToRad(yaw + 90f)));
    }

    public static float[] calcAngle(Vec3 from, Vec3 to) {
        final double difX = to.xCoord - from.xCoord;
        final double difY = (to.yCoord - from.yCoord) * -1.0F;
        final double difZ = to.zCoord - from.zCoord;

        final double dist = MathHelper.sqrt_double(difX * difX + difZ * difZ);

        return new float[]
                {(float) MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0f),
                        (float) MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static double[] directionSpeed(double speed, float partialTicks) {
        final Minecraft mc = Minecraft.getMinecraft();
        float forward = mc.thePlayer.movementInput.moveForward;
        float side = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.prevRotationYaw
                + (mc.thePlayer.rotationYaw - mc.thePlayer.prevRotationYaw) * partialTicks;

        if (forward != 0) {
            if (side > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (side < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            side = 0;

            // forward = clamp(forward, 0, 1);
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }

        final double sin = Math.sin(Math.toRadians(yaw + 90));
        final double cos = Math.cos(Math.toRadians(yaw + 90));
        final double posX = (forward * speed * cos + side * speed * sin);
        final double posZ = (forward * speed * sin - side * speed * cos);
        return new double[]
                {posX, posZ};
    }

    public static double[] directionSpeedNoForward(double speed, float partialTicks) {
        final Minecraft mc = Minecraft.getMinecraft();
        float forward = 1f;

        if (mc.gameSettings.keyBindLeft.isPressed() || mc.gameSettings.keyBindRight.isPressed() || mc.gameSettings.keyBindBack.isPressed() || mc.gameSettings.keyBindForward.isPressed())
            forward = mc.thePlayer.movementInput.moveForward;

        float side = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.prevRotationYaw
                + (mc.thePlayer.rotationYaw - mc.thePlayer.prevRotationYaw) * partialTicks;

        if (forward != 0) {
            if (side > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (side < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            side = 0;

            // forward = clamp(forward, 0, 1);
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }

        final double sin = Math.sin(Math.toRadians(yaw + 90));
        final double cos = Math.cos(Math.toRadians(yaw + 90));
        final double posX = (forward * speed * cos + side * speed * sin);
        final double posZ = (forward * speed * sin - side * speed * cos);
        return new double[]
                {posX, posZ};
    }

    public static Vec3 mult(Vec3 factor, Vec3 multiplier) {
        return new Vec3(factor.xCoord * multiplier.xCoord, factor.yCoord * multiplier.yCoord, factor.zCoord * multiplier.zCoord);
    }

    public static Vec3 mult(Vec3 factor, float multiplier) {
        return new Vec3(factor.xCoord * multiplier, factor.yCoord * multiplier, factor.zCoord * multiplier);
    }

    public static Vec3 div(Vec3 factor, Vec3 divisor) {
        return new Vec3(factor.xCoord / divisor.xCoord, factor.yCoord / divisor.yCoord, factor.zCoord / divisor.zCoord);
    }

    public static Vec3 div(Vec3 factor, float divisor) {
        return new Vec3(factor.xCoord / divisor, factor.yCoord / divisor, factor.zCoord / divisor);
    }

    public static double round(double value, int places) {
        if (places < 0) {
            return value;
        }
        return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }

    public static float wrap(float val) {
        val = val % 360.0f;
        if (val >= 180.0f)
            val -= 360.0f;
        if (val < -180.0f)
            val += 360.0f;
        return val;
    }

    // linearly maps value from the range (a..b) to (c..d)
    public static double map(double value, double a, double b, double c, double d) {
        // first map value from (a..b) to (0..1)
        value = (value - a) / (b - a);
        // then map it from (0..1) to (c..d) and return it
        return c + value * (d - c);
    }

    public static double linear(double from, double to, double incline) {
        return (from < to - incline) ? (from + incline) : ((from > to + incline) ? (from - incline) : to);
    }

    public static double parabolic(double from, double to, double incline) {
        return from + (to - from) / incline;
    }

    public static double getDistance(Vec3 pos, double x, double y, double z) {
        final double deltaX = pos.xCoord - x;
        final double deltaY = pos.yCoord - y;
        final double deltaZ = pos.zCoord - z;
        return (double) MathHelper.sqrt_double(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    public static double[] calcIntersection(double[] line, double[] line2) {
        final double a1 = line[3] - line[1];
        final double b1 = line[0] - line[2];
        final double c1 = a1 * line[0] + b1 * line[1];

        final double a2 = line2[3] - line2[1];
        final double b2 = line2[0] - line2[2];
        final double c2 = a2 * line2[0] + b2 * line2[1];

        final double delta = a1 * b2 - a2 * b1;

        return new double[]
                {(b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta};
    }

    public static double calculateAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil(-angle / 360) * 360;

        return angle;
    }
}
