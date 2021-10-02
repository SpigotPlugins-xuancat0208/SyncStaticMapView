package xuan.cat.syncstaticmapview.database.sql.builder;

/**
 * 判斷式互相配合形式枚舉
 */
public enum WhereMutual {
    /** 必須與另一判斷式同時符合 */
    AND ("AND"),
    /** 或是符合另一判斷式 */
    OR  ("OR");


    private final String value;


    /**
     * 設定此枚舉的SQL字符串片段
     * @param v SQL可拼湊字符串片段
     */
    WhereMutual(String v) {
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
