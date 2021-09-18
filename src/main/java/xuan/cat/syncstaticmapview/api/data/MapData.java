package xuan.cat.syncstaticmapview.api.data;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface MapData {
    byte[] getPixels();

    byte getPixel(int x, int y);
    void setPixel(int x, int y, byte color);

    Color getColor(int x, int y);
    void setColor(int x, int y, Color color);
    void setColorTransparent(int x, int y);

    void setRGB(int x, int y, int rgb);
    void setRGB(int x, int y, int r, int g, int b);

    BufferedImage toImage();
}
