package xuan.cat.syncstaticmapview.code.data;

import java.util.List;

public class MapRedirectsCache {
    public final long vitality;
    public final List<MapRedirectEntry> redirects;

    public MapRedirectsCache(List<MapRedirectEntry> redirects, long vitality) {
        this.redirects = redirects;
        this.vitality = vitality;
    }
}
