package xuan.cat.syncstaticmapview.code.branch;

import net.minecraft.world.level.saveddata.maps.WorldMap;
import org.bukkit.craftbukkit.v1_17_R1.map.CraftMapView;
import org.bukkit.map.MapView;
import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.data.MapData;
import xuan.cat.syncstaticmapview.code.data.CodeMapData;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

public final class CodeBranchMapConversion implements BranchMapConversion {
    private final BranchMapColor branchMapColor;
    private final BranchMapConversion branchMapConversion;
    private final Field field_CraftMapView_WorldMap;


    public CodeBranchMapConversion(BranchMapColor branchMapColor, BranchMapConversion branchMapConversion) throws NoSuchFieldException {
        this.branchMapColor = branchMapColor;
        this.branchMapConversion = branchMapConversion;

        this.field_CraftMapView_WorldMap = CraftMapView.class.getDeclaredField("worldMap");
        this.field_CraftMapView_WorldMap.setAccessible(true);
    }




    public MapData ofBukkit(MapView mapView) {
        try {
            CraftMapView craftMapView = (CraftMapView) mapView;
            WorldMap worldMap = (WorldMap) field_CraftMapView_WorldMap.get(craftMapView);
            return new CodeMapData(branchMapColor, branchMapConversion, worldMap.g.clone());

        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
            return new CodeMapData(branchMapColor, branchMapConversion);
        }
    }


//    private static BufferedImage resizeImage(BufferedImage image) {
//        BufferedImage bufferedImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D graphics2D = bufferedImage.createGraphics();
//        graphics2D.setComposite(AlphaComposite.Src);
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        graphics2D.drawImage(image, 0, 0, 128, 128, null);
//        graphics2D.dispose();
//        return bufferedImage;
//    }
//    public MapData ofImage(BufferedImage image) {
////        image = resizeImage(image);
//        MapData mapData = new CodeMapData(branchMapColor, branchMapConversion);
//        for (int x = 0 ; x < 128 ; x++)
//            for (int y = 0 ; y < 128 ; y++)
//                mapData.setColor(x, y, new Color(image.getRGB(x, y), true));
//        return mapData;
//    }


    public BufferedImage toImage(MapData mapData) {
        BufferedImage bufferedImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < 128; x++)
            for (int y = 0; y < 128; y++)
                bufferedImage.setRGB(x, y, mapData.getColor(x, y).getRGB());
        return bufferedImage;
    }


}
