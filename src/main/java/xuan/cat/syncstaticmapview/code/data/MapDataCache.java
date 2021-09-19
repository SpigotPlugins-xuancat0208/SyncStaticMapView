package xuan.cat.syncstaticmapview.code.data;

import xuan.cat.syncstaticmapview.api.data.MapData;

public class MapDataCache {
    public final long vitality;
    public final MapData data;

    public MapDataCache(MapData data, long vitality) {
        this.data = data;
        this.vitality = vitality;
    }
}
