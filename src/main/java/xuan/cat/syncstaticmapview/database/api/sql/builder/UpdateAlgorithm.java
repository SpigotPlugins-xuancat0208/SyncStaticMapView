package xuan.cat.syncstaticmapview.database.api.sql.builder;

/**
 * 更新資料運算法枚舉
 */
public enum UpdateAlgorithm {

    /** 等於 */
    EQUAL       ("="),
    /** 加法 */
    ADDITION    ("+"),
    /** 減法 */
    SUBTRACTION ("-");


    private final String value;


    /**
     * 設定此枚舉的SQL字符串片段
     * @param v SQL可拼湊字符串片段
     */
    UpdateAlgorithm(String v) {
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
