package xuan.cat.syncstaticmapview.database.sql.builder;

/**
 * 資料表引擎類型枚舉
 */
public enum DatabaseEngine {
    CSV                 ("'CSV'"),
    MRG_MyISAM          ("'MRG_MyISAM'"),
    MEMORY              ("'MEMORY'"),
    MyISAM              ("'MyISAM'"),
    SEQUENCE            ("'SEQUENCE'"),
    ARCHIVE             ("'ARCHIVE'"),
    Aria                ("'Aria'"),
    PERFORMANCE_SCHEMA  ("'PERFORMANCE_SCHEMA'"),
    InnoDB              ("'InnoDB'"),

    ;


    private final String value;

    DatabaseEngine(final String v) {
        value = v;
    }

    public String part() {
        return value;
    }
}
