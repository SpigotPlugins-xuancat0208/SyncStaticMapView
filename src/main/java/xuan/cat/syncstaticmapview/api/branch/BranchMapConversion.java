package xuan.cat.syncstaticmapview.api.branch;

import org.bukkit.map.MapView;
import xuan.cat.syncstaticmapview.api.data.MapData;

import java.awt.image.BufferedImage;

/**
 * 可分支功能, 地圖
 */
public interface BranchMapConversion {
    MapData ofBukkit(MapView mapView);

    MapData ofImage(BufferedImage image);
    BufferedImage toImage(MapData mapData);
}
