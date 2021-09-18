package xuan.cat.syncstaticmapview.code.data;

import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.data.MapData;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class CodeMapData implements MapData {
    private final BranchMapColor branchMapColor;
    private final BranchMapConversion branchMapConversion;
    private final byte[] pixels;


    public CodeMapData(BranchMapColor branchMapColor, BranchMapConversion branchMapConversion) {
        this(branchMapColor, branchMapConversion, new byte[16384]);
    }
    public CodeMapData(BranchMapColor branchMapColor, BranchMapConversion branchMapConversion, byte[] pixels) {
        this.branchMapColor = branchMapColor;
        this.branchMapConversion = branchMapConversion;
        this.pixels = pixels;
    }


    public byte[] getPixels() {
        return pixels;
    }


    public byte getPixel(int x, int y) {
        return pixels[x + (y << 7)];
    }
    public void setPixel(int x, int y, byte color) {
        pixels[x + (y << 7)] = color;
    }


    public Color getColor(int x, int y) {
        return branchMapColor.getColorFromColorIndex(pixels[x + (y << 7)]);
    }
    public void setColor(int x, int y, Color color) {
        if (color.getAlpha() >= 200) {
            pixels[x + (y << 7)] = branchMapColor.matchColor(color);
        } else {
            setColorTransparent(x, y);
        }
    }
    public void setColorTransparent(int x, int y) {
        pixels[x + (y << 7)] = 0;
    }


    public void setRGB(int x, int y, int rgb) {
        pixels[x + (y << 7)] = branchMapColor.matchColor(rgb);
    }
    public void setRGB(int x, int y, int r, int g, int b) {
        pixels[x + (y << 7)] = branchMapColor.matchColor(r, g, b);
    }


    public BufferedImage toImage() {
        return branchMapConversion.toImage(this);
    }
}
