package me.anemoi.rendertools.utils;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class Point3dD {
    private double x;
    private double y;
    private double z;

    public Point3dD(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3dD(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public Point3dD(Vec3 vec) {
        this.x = vec.xCoord;
        this.y = vec.yCoord;
        this.z = vec.zCoord;
    }

    //get as blockpos
    public BlockPos getBlockPos() {
        return new BlockPos(x, y, z);
    }

    //get as vec3d
    public Vec3 getVec3d() {
        return new Vec3(x, y, z);
    }

    //add
    public Point3dD add(double x, double y, double z) {
        return new Point3dD(this.x + x, this.y + y, this.z + z);
    }

    //subtract
    public Point3dD subtract(double x, double y, double z) {
        return new Point3dD(this.x - x, this.y - y, this.z - z);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
