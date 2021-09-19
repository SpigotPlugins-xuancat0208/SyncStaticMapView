package xuan.cat.databasecatmini.api.sql.builder;

/**
 * 判斷式條件枚舉
 */
public enum WhereJudge {

    /** 等於 */
    EQUAL       ("="),
    /** 小於 */
    LESS        ("<"),
    /** 空值 */
    IS_NULL     ("IS NULL"),
    /** 非空值 */
    IS_NOT_NULL ("IS NOT NULL"),

    /** 含有指定的清單 */
    FIND_IN_SET ("FIND_IN_SET")

    ;


    private final String value;


    /**
     * 設定此枚舉的SQL字符串片段
     * @param v SQL可拼湊字符串片段
     */
    WhereJudge(String v) {
        value = v;
    }


    /**
     * 取得此枚舉的SQL字符串片段
     * @return SQL可拼湊字符串片段
     */
    public String part() {
        return value;
    }
}
