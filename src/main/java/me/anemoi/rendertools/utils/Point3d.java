package me.anemoi.rendertools.utils;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class Point3d {
    private float x;
    private float y;
    private float z;

    public Point3d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3d(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
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
    public Point3d add(float x, float y, float z) {
        return new Point3d(this.x + x, this.y + y, this.z + z);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
