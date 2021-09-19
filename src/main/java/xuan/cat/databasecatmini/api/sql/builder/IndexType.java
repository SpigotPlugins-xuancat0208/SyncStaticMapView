package xuan.cat.databasecatmini.api.sql.builder;

/**
 * 更改欄位表索引類型枚舉
 */
public enum IndexType {
    /** 主要 */
    PRIMARY     ("PRIMARY KEY "),
    /** 唯一 */
    UNIQUE      ("UNIQUE "),
    /** 索引 */
    INDEX       ("INDEX "),
    /** 全文 */
    FULLTEXT    ("FULLTEXT "),
    /** 空間 */
    SPATIAL     ("SPATIAL ");


    private final String value;


    /**
     * 設定此枚舉的SQL字符串片段
     * @param v SQL可拼湊字符串片段
     */
    IndexType(final String v) {
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