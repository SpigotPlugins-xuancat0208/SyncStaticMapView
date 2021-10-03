package xuan.cat.syncstaticmapview.database.sql;

/**
 * SQL部分語法生成器
 * 生成出的無法直接當作SQL使用, 必須依賴 SQLBuilder 再度處理
 */
public interface SQLPart {
    StringBuilder part();
    SQLPart clone();
}
