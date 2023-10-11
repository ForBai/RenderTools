package me.anemoi.rendertools.utils.render;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/*
Thanks to https://hypixel.net/threads/forge-1-8-9-block-overlay-v4-0-3.1417995/
for some parts of the code
 */
public class RenderBlockOverlay {
    private static final Tessellator TESSELLATOR = Tessellator.getInstance();
    private static final WorldRenderer WORLD_RENDERER = TESSELLATOR.getWorldRenderer();

    public static void drawBlock(AxisAlignedBB box, EnumFacing side, int overlayStartColor, int overlayEndColor, int outlineStartColor, int outlineEndColor, boolean overlay, boolean outline) {
        if (side == null) {
            drawBlockFull(box, new Color(overlayStartColor, true), new Color(overlayEndColor, true), new Color(outlineStartColor, true), new Color(outlineEndColor, true), overlay, outline);
        } else {
            drawBlockSide(box, side, new Color(overlayStartColor, true), new Color(overlayEndColor, true), new Color(outlineStartColor, true), new Color(outlineEndColor, true), overlay, outline);
        }
    }

    private static void drawBlockFull(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        if (overlay) {
            drawBlockTop(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, true, false);
            drawBlockBottom(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, true, false);
            drawBlockNorth(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, true, false);
            drawBlockEast(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, true, false);
            drawBlockSouth(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, true, false);
            drawBlockWest(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, true, false);
        }
        if (outline) {
            drawBlockTop(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, false, true);
            drawBlockBottom(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, false, true);
            drawBlockNorth(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, false, true);
            drawBlockEast(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, false, true);
            drawBlockSouth(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, false, true);
            drawBlockWest(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, false, true);
        }
    }

    public static void drawBlockSide(AxisAlignedBB box, EnumFacing side, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        switch (side) {
            case UP: {
                drawBlockTop(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
                break;
            }
            case DOWN: {
                drawBlockBottom(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
                break;
            }
            case NORTH: {
                drawBlockNorth(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
                break;
            }
            case EAST: {
                drawBlockEast(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
                break;
            }
            case SOUTH: {
                drawBlockSouth(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
                break;
            }
            case WEST: {
                drawBlockWest(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
            }
        }
    }

    private static void drawBlockTop(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        if (overlay) {
            WORLD_RENDERER.begin(7, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.minX, box.maxY, box.maxZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.maxZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.minZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.maxY, box.minZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
        if (outline) {
            WORLD_RENDERER.begin(2, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.minX, box.maxY, box.maxZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.maxZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.minZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.maxY, box.minZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
    }

    private static void drawBlockBottom(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        if (overlay) {
            WORLD_RENDERER.begin(7, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.maxX, box.minY, box.maxZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.maxZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.minZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.minY, box.minZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
        if (outline) {
            WORLD_RENDERER.begin(2, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.maxX, box.minY, box.maxZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.maxZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.minZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.minY, box.minZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
    }

    private static void drawBlockNorth(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        if (overlay) {
            WORLD_RENDERER.begin(7, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.minZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.minY, box.minZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.minZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.maxY, box.minZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
        if (outline) {
            WORLD_RENDERER.begin(2, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.minZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.minY, box.minZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.minZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.maxY, box.minZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.minZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
    }

    private static void drawBlockEast(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        if (overlay) {
            WORLD_RENDERER.begin(7, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.minZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.maxZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.minY, box.maxZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.minY, box.minZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
        if (outline) {
            WORLD_RENDERER.begin(2, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.minZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.maxZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.minY, box.maxZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.minY, box.minZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
    }

    private static void drawBlockSouth(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        if (overlay) {
            WORLD_RENDERER.begin(7, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.minX, box.maxY, box.maxZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.maxZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.minY, box.maxZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.maxZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
        if (outline) {
            WORLD_RENDERER.begin(2, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.maxZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.maxY, box.maxZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.maxZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.minY, box.maxZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.maxX, box.maxY, box.maxZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
    }

    private static void drawBlockWest(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        if (overlay) {
            WORLD_RENDERER.begin(7, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.minX, box.maxY, box.maxZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.maxY, box.minZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.minZ).color(overlayStartColor.getRed(), overlayStartColor.getGreen(), overlayStartColor.getBlue(), overlayStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.maxZ).color(overlayEndColor.getRed(), overlayEndColor.getGreen(), overlayEndColor.getBlue(), overlayEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
        if (outline) {
            WORLD_RENDERER.begin(2, DefaultVertexFormats.POSITION_COLOR);
            WORLD_RENDERER.pos(box.minX, box.maxY, box.maxZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.maxY, box.minZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.minZ).color(outlineStartColor.getRed(), outlineStartColor.getGreen(), outlineStartColor.getBlue(), outlineStartColor.getAlpha()).endVertex();
            WORLD_RENDERER.pos(box.minX, box.minY, box.maxZ).color(outlineEndColor.getRed(), outlineEndColor.getGreen(), outlineEndColor.getBlue(), outlineEndColor.getAlpha()).endVertex();
            TESSELLATOR.draw();
        }
    }

    public static void drawStairs(BlockPos blockPos, IBlockState blockState, AxisAlignedBB box, EnumFacing side, double playerX, double playerY, double playerZ, int overlayStartColor, int overlayEndColor, int outlineStartColor, int outlineEndColor, boolean overlay, boolean outline) {
        EnumFacing blockFacing = (EnumFacing) ((Object) blockState.getValue(BlockStairs.FACING));
        BlockStairs.EnumHalf blockHalf = (BlockStairs.EnumHalf) ((Object) blockState.getValue(BlockStairs.HALF));
        int blockX = blockPos.getX();
        int blockY = blockPos.getY();
        int blockZ = blockPos.getZ();
        int angleX = blockHalf == BlockStairs.EnumHalf.TOP ? 270 : 0;
        int angleY = 0;
        switch (blockFacing) {
            case NORTH: {
                angleY = 180;
                break;
            }
            case EAST: {
                angleY = 90;
                break;
            }
            case WEST: {
                angleY = 270;
            }
        }
        GL11.glPushMatrix();
        GL11.glTranslated(-playerX, -playerY, -playerZ);
        GL11.glTranslated((double) blockX + 0.5, blockY, (double) blockZ + 0.5);
        GL11.glRotated(angleY, 0.0, 1.0, 0.0);
        GL11.glTranslated(0.0, 0.5, 0.0);
        GL11.glRotated(angleX, 1.0, 0.0, 0.0);
        GL11.glTranslated((double) (-blockX) - 0.5, (double) (-blockY) - 0.5, (double) (-blockZ) - 0.5);
        if (side == null) {
            drawStairsFull(box, new Color(overlayStartColor, true), new Color(overlayEndColor, true), new Color(outlineStartColor, true), new Color(outlineEndColor, true), overlay, outline);
        } else {
            drawStairsSide(box, blockHalf, blockFacing, side, new Color(overlayStartColor, true), new Color(overlayEndColor, true), new Color(outlineStartColor, true), new Color(outlineEndColor, true), overlay, outline);
        }
        GL11.glPopMatrix();
    }

    private static void drawStairsFull(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        drawStairsTop(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
        drawStairsBottom(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
        drawStairsNorth(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
        drawStairsEast(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
        drawStairsSouth(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
        drawStairsWest(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
    }

    private static void drawStairsSide(AxisAlignedBB box, BlockStairs.EnumHalf blockHalf, EnumFacing blockFacing, EnumFacing side, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        switch (getSide(blockHalf, blockFacing, side)) {
            case UP: {
                drawStairsTop(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
                break;
            }
            case DOWN: {
                drawStairsBottom(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
                break;
            }
            case NORTH: {
                drawStairsNorth(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
                break;
            }
            case EAST: {
                drawStairsEast(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
                break;
            }
            case SOUTH: {
                drawStairsSouth(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
                break;
            }
            case WEST: {
                drawStairsWest(box, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
            }
        }
    }

    private static void drawStairsTop(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        drawBlockSide(box.contract(0.0, 0.0, 0.25).offset(0.0, 0.0, 0.25), EnumFacing.UP, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
        drawBlockSide(box.contract(0.0, 0.0, 0.25).offset(0.0, -0.5, -0.25), EnumFacing.UP, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
    }

    private static void drawStairsBottom(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        drawBlockSide(box, EnumFacing.DOWN, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
    }

    private static void drawStairsNorth(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        drawBlockSide(box.contract(0.0, 0.252, 0.0).offset(0.0, 0.252, 0.5), EnumFacing.NORTH, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
        drawBlockSide(box.contract(0.0, 0.25, 0.0).offset(0.0, -0.25, 0.0), EnumFacing.NORTH, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
    }

    private static void drawStairsEast(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        drawBlockSide(box.contract(0.0, 0.252, 0.25).offset(0.0, 0.252, 0.25), EnumFacing.EAST, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
        drawBlockSide(box.contract(0.0, 0.25, 0.0).offset(0.0, -0.25, 0.0), EnumFacing.EAST, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
    }

    private static void drawStairsSouth(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        drawBlockSide(box, EnumFacing.SOUTH, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
    }

    private static void drawStairsWest(AxisAlignedBB box, Color overlayStartColor, Color overlayEndColor, Color outlineStartColor, Color outlineEndColor, boolean overlay, boolean outline) {
        drawBlockSide(box.contract(0.0, 0.252, 0.25).offset(0.0, 0.252, 0.25), EnumFacing.WEST, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
        drawBlockSide(box.contract(0.0, 0.25, 0.0).offset(0.0, -0.25, 0.0), EnumFacing.WEST, overlayStartColor, overlayEndColor, outlineStartColor, outlineEndColor, overlay, outline);
    }

    private static EnumFacing getSide(BlockStairs.EnumHalf blockHalf, EnumFacing blockFacing, EnumFacing side) {
        if (blockHalf == BlockStairs.EnumHalf.TOP) {
            switch (blockFacing) {
                case NORTH: {
                    side = side.rotateAround(EnumFacing.Axis.X);
                    side = side.rotateAround(EnumFacing.Axis.Y);
                    side = side.rotateAround(EnumFacing.Axis.Y);
                    break;
                }
                case EAST: {
                    side = side.rotateAround(EnumFacing.Axis.Z);
                    side = side.rotateAround(EnumFacing.Axis.Y);
                    break;
                }
                case SOUTH: {
                    side = side.rotateAround(EnumFacing.Axis.X);
                    side = side.rotateAround(EnumFacing.Axis.X);
                    side = side.rotateAround(EnumFacing.Axis.X);
                    break;
                }
                case WEST: {
                    side = side.rotateAround(EnumFacing.Axis.Z);
                    side = side.rotateAround(EnumFacing.Axis.Y);
                    side = side.rotateAround(EnumFacing.Axis.Z);
                    side = side.rotateAround(EnumFacing.Axis.Z);
                }
            }
        } else if (side != EnumFacing.UP && side != EnumFacing.DOWN) {
            switch (blockFacing) {
                case NORTH: {
                    side = side.getOpposite();
                    break;
                }
                case EAST: {
                    side = side.rotateY();
                    break;
                }
                case WEST: {
                    side = side.rotateYCCW();
                }
            }
        }
        return side;
    }
}
