package me.anemoi.rendertools.utils;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class BetterVec3 {
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public BetterVec3(double x, double y, double z) {
        if (x == -0.0) {
            x = 0.0;
        }

        if (y == -0.0) {
            y = 0.0;
        }

        if (z == -0.0) {
            z = 0.0;
        }

        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public BetterVec3(Vec3i vec3i) {
        this((double) vec3i.getX(), (double) vec3i.getY(), (double) vec3i.getZ());
    }

    public BetterVec3(Vec3 vec3) {
        this((double) vec3.xCoord, (double) vec3.yCoord, (double) vec3.zCoord);
    }

    public BetterVec3 subtractReverse(BetterVec3 vec) {
        return new BetterVec3(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }

    public BetterVec3 normalize() {
        double d0 = (double) MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return d0 < 1.0E-4 ? new BetterVec3(0.0, 0.0, 0.0) : new BetterVec3(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
    }

    public double dotProduct(BetterVec3 vec) {
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }

    public BetterVec3 crossProduct(BetterVec3 vec) {
        return new BetterVec3(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }

    public BetterVec3 subtract(BetterVec3 vec) {
        return this.subtract(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public BetterVec3 subtract(double x, double y, double z) {
        return this.addVector(-x, -y, -z);
    }

    public BetterVec3 add(BetterVec3 vec) {
        return this.addVector(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public BetterVec3 addVector(double x, double y, double z) {
        return new BetterVec3(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    public double distanceTo(BetterVec3 vec) {
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        return (double) MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }

    public double squareDistanceTo(BetterVec3 vec) {
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double lengthVector() {
        return (double) MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public BetterVec3 rotatePitch(float pitch) {
        double d0 = this.xCoord;
        double d1 = this.yCoord * (double) MathHelper.cos(pitch) + this.zCoord * (double) MathHelper.sin(pitch);
        double d2 = this.zCoord * (double) MathHelper.cos(pitch) - this.yCoord * (double) MathHelper.sin(pitch);
        return new BetterVec3(d0, d1, d2);
    }

    public BetterVec3 rotateYaw(float yaw) {
        double d0 = this.xCoord * (double) MathHelper.cos(yaw) + this.zCoord * (double) MathHelper.sin(yaw);
        double d1 = this.yCoord;
        double d2 = this.zCoord * (double) MathHelper.cos(yaw) - this.xCoord * (double) MathHelper.sin(yaw);
        return new BetterVec3(d0, d1, d2);
    }

}
