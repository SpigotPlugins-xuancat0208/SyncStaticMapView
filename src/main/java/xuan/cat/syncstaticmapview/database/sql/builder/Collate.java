package xuan.cat.syncstaticmapview.database.sql.builder;

/**
 * 編碼類型,或特殊儲存狀態
 */

public enum Collate {
    /** 不指定 */
    NOT,

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
