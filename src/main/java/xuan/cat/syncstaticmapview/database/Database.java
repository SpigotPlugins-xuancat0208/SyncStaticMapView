package xuan.cat.syncstaticmapview.database;

import xuan.cat.syncstaticmapview.database.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.sql.MySQL;
import xuan.cat.syncstaticmapview.database.sql.builder.Field;
import xuan.cat.syncstaticmapview.database.sql.builder.FieldStyle;

import java.util.Map;

/**
 * 資料庫的一切操作
 */
public final class Database {
    private Database() {
    }

    /**
     * 連線資料庫
     * @param ip 地址
     * @param port 端口
     * @param user 用戶
     * @param password 密碼
     * @param options 額外選項
     * @return 資料庫連線
     */
    public static MySQL createMySQL(String ip, int port, String user, String password, Map<String, Object> options) {
        return new MySQL(ip, port, user, password, options);
    }

    /**
     * 創建欄位
     * @param style 型態
     * @param name 名稱
     * @param <T> 資料型態
     */
    public static <T> Field<T> atField(FieldStyle<T> style, String name){
        return new Field<>(style, name);
    }

    /**
     * 創建資料表
     * @param name 名稱
     */
    public static DatabaseTable atTable(String name) {
        return new DatabaseTable(name);
    }
}
