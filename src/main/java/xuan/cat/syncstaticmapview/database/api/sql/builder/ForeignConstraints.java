package xuan.cat.syncstaticmapview.database.api.sql.builder;

/**
 * 欄位表外來鍵限制
 */
public enum ForeignConstraints {
    RESTRICT    ("RESTRICT"),       // 阻止父表上的更改,這是雙方的默認行為ON DELETE和ON UPDATE
    NO_ACTION   ("NO ACTION"),      // 同RESTRICT
    CASCADE     ("CASCADE"),        // 允許更改並在子表上傳播。例如，如果刪除父行，則也會刪除子行; 如果父行的ID發生更改，子行的ID也將更改
    SET_NULL    ("SET NULL"),       // 允許更改，並將子行的外鍵列設置為NULL
    SET_DEFAULT ("SET DEFAULT");    // 僅適用於PBXT。與之類似SET NULL，但外鍵列已設置為其默認值。如果不存在默認值，則會產生錯誤


    private final String value;


    /**
     * 設定此枚舉的SQL字符串片段
     * @param v SQL可拼湊字符串片段
     */
    ForeignConstraints(final String v) {
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
