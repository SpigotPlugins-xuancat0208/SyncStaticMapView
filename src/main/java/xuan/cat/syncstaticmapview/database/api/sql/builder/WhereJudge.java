package xuan.cat.syncstaticmapview.database.api.sql.builder;

/**
 * 判斷式條件枚舉
 */
public enum WhereJudge {

    /** 等於 */
    EQUAL       ("="),
    /** 大於 */
    EXCEED      (">"),
    /** 小於 */
    LESS        ("<"),
    /** 等於或大於 */
    ABOVE       (">="),
    /** 等於或小於 */
    UNDER       ("<="),
    /** 不等於 */
    CANNOT      ("!="),
    /** 相似 */
    LIKE        ("LIKE"),
    /** 不相似 */
    NOT_LIKE    ("NOT LIKE"),
    /** 正則表達式 */
    REGEXP      ("REGEXP"),
    /** 不等於正則表達式 */
    NOT_REGEXP  ("NOT REGEXP"),
    /** 有其一符合 */
    IN          ("IN"),
    /** 全部都不符合 */
    NOT_IN      ("NOT IN"),
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
