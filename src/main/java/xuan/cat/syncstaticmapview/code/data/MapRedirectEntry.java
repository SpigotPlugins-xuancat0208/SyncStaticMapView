package xuan.cat.syncstaticmapview.code.data;

import xuan.cat.syncstaticmapview.api.data.MapRedirect;

public final class MapRedirectEntry implements MapRedirect {
    private final String permission;
    private final int priority;
    private final int redirectId;


    public MapRedirectEntry(int priority, String permission, int redirectId) {
        this.permission = permission;
        this.priority = priority;
        this.redirectId = redirectId;
    }


    public int getPriority() {
        return priority;
    }

    public int getRedirectId() {
        return redirectId;
    }

    public String getPermission() {
        return permission;
    }
}
