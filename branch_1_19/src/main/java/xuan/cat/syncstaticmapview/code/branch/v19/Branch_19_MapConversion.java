package xuan.cat.syncstaticmapview.code.branch.v19;

import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.craftbukkit.v1_19_R3.map.CraftMapView;
import org.bukkit.map.MapView;
import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.data.MapData;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

public final class Branch_19_MapConversion implements BranchMapConversion {
    private final BranchMapColor branchMapColor;
    private final BranchMapConversion branchMapConversion;
    private final Field field_CraftMapView_WorldMap;


    public Branch_19_MapConversion(BranchMapColor branchMapColor, BranchMapConversion branchMapConversion) throws NoSuchFieldException {
        this.branchMapColor = branchMapColor;
        this.branchMapConversion = branchMapConversion;

        this.field_CraftMapView_WorldMap = CraftMapView.class.getDeclaredField("worldMap");
        this.field_CraftMapView_WorldMap.setAccessible(true);
    }



    public MapData ofBukkit(MapView mapView) {
        try {
            CraftMapView craftMapView = (CraftMapView) mapView;
            MapItemSavedData worldMap = (MapItemSavedData) field_CraftMapView_WorldMap.get(craftMapView);
            return new MapData(branchMapColor, branchMapConversion, worldMap.colors.clone());

        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
            return new MapData(branchMapColor, branchMapConversion);
        }
    }


    public BufferedImage toImage(MapData mapData) {
        BufferedImage bufferedImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < 128; x++)
            for (int y = 0; y < 128; y++)
                bufferedImage.setRGB(x, y, mapData.getColor(x, y).getRGB());
        return bufferedImage;
    }


}
