package xuan.cat.syncstaticmapview.database.sql.builder;

/**
 * 選擇資料指定返回類型枚舉
 */
public enum SelectReturn {
    /** 正常 */
    NOT             ("",                "", false,  ""),
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
