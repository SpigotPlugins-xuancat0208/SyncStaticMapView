package xuan.cat.syncstaticmapview.api;

import org.bukkit.Bukkit;
import org.bukkit.map.MapView;
import xuan.cat.syncstaticmapview.api.data.MapData;
import xuan.cat.syncstaticmapview.api.data.MapRedirect;
import xuan.cat.syncstaticmapview.code.Index;
import xuan.cat.syncstaticmapview.code.MapDatabase;
import xuan.cat.syncstaticmapview.code.data.CodeMapData;
import xuan.cat.syncstaticmapview.code.data.MapRedirectEntry;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.List;

public final class SyncMap {
    private SyncMap() {
    }


    public static MapData createData() {
        return new CodeMapData(Index.getBranchMapColor(), Index.getBranchMapConversion());
    }

    public static MapRedirect createRedirect(int priority, String permission, int redirectId) {
        return new MapRedirectEntry(priority, permission, redirectId);
    }


    public static MapData fromBukkit(MapView mapView) {
        return Index.getBranchMapConversion().ofBukkit(mapView);
    }

    public static MapData fromImage(BufferedImage image) {
        MapData mapData = createData();
        if (image.getWidth() != 128 || image.getHeight() != 128) {
            BufferedImage spaceImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
            Graphics2D spaceGraphics = spaceImage.createGraphics();
            spaceGraphics.setComposite(AlphaComposite.Src);
            spaceGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            spaceGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            spaceGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            spaceGraphics.drawImage(image, 128, 128, null);
            spaceGraphics.dispose();
            image = spaceImage;
        }
        for (int x = 0 ; x < 128 ; x++)
            for (int y = 0 ; y < 128 ; y++)
                mapData.setColor(x, y, new Color(image.getRGB(x, y), true));
        return mapData;
    }

    public static BufferedImage toImage(MapData mapData) {
        return mapData.toImage();
    }


    private static void checkAsync() {
        if (Bukkit.isPrimaryThread())
            throw new RuntimeException("Cannot operate database in primary thread!");
    }
    private static void checkMapId(int mapId) {
        if (mapId < 0)
            throw new IllegalArgumentException("Map Id cannot be negative!");
    }


    public static boolean existData(int mapId) throws SQLException {
        checkAsync();
        checkMapId(mapId);
        return Index.getMapDatabase().existMapData(mapId);
    }

    public static int createData(MapData mapData) throws SQLException {
        return createData(mapData, 0);
    }
    public static int createData(MapData mapData, int uploaderId) throws SQLException {
        checkAsync();
        return Index.getMapDatabase().addMapData(mapData, uploaderId);
    }

//    @Deprecated
//    public static boolean setData(int mapId, MapData mapData) throws SQLException {
//        checkAsync();
//        checkMapId(mapId);
//        return Index.getMapDatabase().saveMapData(mapId, mapData);
//    }

    public static MapData getData(int mapId) throws SQLException {
        checkAsync();
        checkMapId(mapId);
        return Index.getMapDatabase().loadMapData(mapId);
    }

//    public static boolean deleteData(int mapId) throws SQLException {
//        checkAsync();
//        checkMapId(mapId);
//        return Index.getMapDatabase().removeMapData(mapId);
//    }


    public static boolean setRedirect(int mapId, MapRedirect mapRedirect) throws SQLException {
//        checkAsync();
        checkMapId(mapId);
        if (existData(mapId)) {
            MapDatabase database = Index.getMapDatabase();
            database.removeMapRedirect(mapId, mapRedirect.getPermission());
            database.removeMapRedirect(mapId, mapRedirect.getPriority());
            boolean successfully = database.addMapRedirects(mapId, mapRedirect);
            database.markMapUpdate(mapId);
            return successfully;
        } else {
            return false;
        }
    }
    public static List<MapRedirect> getRedirects(int mapId) throws SQLException {
//        checkAsync();
        checkMapId(mapId);
        return Index.getMapDatabase().getMapRedirects(mapId);
    }

    public static boolean deleteRedirects(int mapId) throws SQLException {
//        checkAsync();
        checkMapId(mapId);
        MapDatabase database = Index.getMapDatabase();
        boolean successfully = database.removeMapRedirect(mapId);
        database.markMapUpdate(mapId);
        return successfully;
    }
    public static boolean deleteRedirect(int mapId, String permission) throws SQLException {
//        checkAsync();
        checkMapId(mapId);
        MapDatabase database = Index.getMapDatabase();
        boolean successfully = database.removeMapRedirect(mapId, permission);
        database.markMapUpdate(mapId);
        return successfully;
    }
    public static boolean deleteRedirect(int mapId, int priority) throws SQLException {
//        checkAsync();
        checkMapId(mapId);
        MapDatabase database = Index.getMapDatabase();
        boolean successfully = database.removeMapRedirect(mapId, priority);
        database.markMapUpdate(mapId);
        return successfully;
    }
}
