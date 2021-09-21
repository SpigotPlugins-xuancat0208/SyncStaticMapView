package xuan.cat.syncstaticmapview.code.data;

import xuan.cat.syncstaticmapview.api.data.MapRedirect;

import java.util.List;

public final class MapRedirectsCache {
    public final long vitality;
    public final List<MapRedirect> redirects;

    public MapRedirectsCache(List<MapRedirect> redirects, long vitality) {
        this.redirects = redirects;
        this.vitality = vitality;
    }
}
