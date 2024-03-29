package xuan.cat.syncstaticmapview.code.data;

public final class MapRedirectEntry {
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
