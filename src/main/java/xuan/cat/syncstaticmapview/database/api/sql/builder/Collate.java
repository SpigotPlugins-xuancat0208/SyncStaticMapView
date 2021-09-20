package xuan.cat.syncstaticmapview.database.api.sql.builder;

/**
 * 編碼類型,或特殊儲存狀態
 */

public enum Collate {

    /** 不指定 */
    NOT(),
    ascii_bin,
    utf8mb4_bin,
    ;

    private final String name;

    Collate() {
        this.name = " COLLATE '" + this.name() + "'";
    }

    public String part() {
        return name;
    }

}
