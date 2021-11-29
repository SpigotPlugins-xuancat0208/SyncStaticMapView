package xuan.cat.syncstaticmapview.api.branch;

import java.awt.*;

public interface BranchMapColor {
    byte matchColor(Color color);
    byte matchColor(int rgb);
    byte matchColor(int r, int g, int b);

    Color getColorFromColorIndex(byte colorIndex);

    int getRegistryColorAmount();
}
