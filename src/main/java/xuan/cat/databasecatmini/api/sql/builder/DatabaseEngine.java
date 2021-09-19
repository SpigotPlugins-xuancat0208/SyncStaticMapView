package xuan.cat.databasecatmini.api.sql.builder;

/**
 * 資料表引擎類型枚舉
 */
public enum DatabaseEngine {
    MEMORY              ("'MEMORY'"),
    MyISAM              ("'MyISAM'"),
    ;


    private final String value;

    DatabaseEngine(final String v) {
        value = v;
    }

    public String part() {
        return value;
    }
}
