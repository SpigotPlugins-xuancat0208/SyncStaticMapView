package xuan.cat.databasecatmini.api.sql.builder;

/**
 * 訊息架構資料表類型
 */
public enum InformationSchema {
    TABLES,
    ;

    private final String value;

    InformationSchema() {
        value = "INFORMATION_SCHEMA." + name().toUpperCase();
    }

    public String part() {
        return value;
    }
}
