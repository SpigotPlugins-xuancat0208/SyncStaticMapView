package xuan.cat.syncstaticmapview.database.api.sql.builder;

/**
 * 選擇資料指定返回類型枚舉
 */
public enum SelectReturn {

    /** 正常 */
    NOT             ("",                "", false,  ""),

    ALL             ("",                "", false,  "*"),

    CHAR_LENGTH     ("CHAR_LENGTH(",    ")",false,  ""),
    DATE            ("DATE(",           ")",false,  ""),
    FROM_UNIXTIME   ("FROM_UNIXTIME(",  ")",false,  ""),
    LOWER           ("LOWER(",          ")",false,  ""),
    ROUND           ("ROUND(",          ")",false,  ""),
    FLOOR           ("FLOOR(",          ")",false,  ""),
    CEIL            ("CEIL(",           ")",false,  ""),
    SEC_TO_TIME     ("SEC_TO_TIME(",    ")",false,  ""),
    TIME_TO_SEC     ("TIME_TO_SEC(",    ")",false,  ""),
    UPPER           ("UPPER(",          ")",false,  ""),

    AVG             ("AVG(",            ")",true,   "*"),
    COUNT           ("COUNT(",          ")",true,   "*"),
    COUNT_DISTINCT  ("COUNT(DISTINCT ", ")",true,   "*"),
    GROUP_CONCAT    ("GROUP_CONCAT(",   ")",true,   "*"),
    MAX             ("MAX(",            ")",true,   "*"),
    MIN             ("MIN(",            ")",true,   "*"),
    SUM             ("SUM(",            ")",true,   "*"),

    HEX             ("HEX(",            ")",false,  ""),
    ;


    private final String    front;
    private final String    behind;
    private final boolean   isCalcFoundRows;
    private final String    defaultValue;


    SelectReturn(String front, String behind, boolean isCalcFoundRows, String defaultValue) {
        this.front              = front;
        this.behind             = behind;
        this.isCalcFoundRows    = isCalcFoundRows;
        this.defaultValue       = defaultValue;
    }


    public CharSequence part() {
        return new StringBuilder().append(front).append(defaultValue).append(behind);
    }
    public CharSequence part(CharSequence field) {
        return new StringBuilder().append(front).append(field).append(behind);
    }
    public boolean isCalcFoundRows() {
        return isCalcFoundRows;
    }
    public String getDefaultValue() {
        return defaultValue;
    }
}
