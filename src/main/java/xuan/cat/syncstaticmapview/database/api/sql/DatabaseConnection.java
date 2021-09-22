package xuan.cat.syncstaticmapview.database.api.sql;

import xuan.cat.syncstaticmapview.database.api.sql.builder.CreateDatabase;
import xuan.cat.syncstaticmapview.database.api.sql.builder.DeleteDatabase;

import java.sql.SQLException;

/**
 * 資料庫
 */
public interface DatabaseConnection {

    /**
     * 已失去連線
     */
    void disconnect() throws SQLException;

    /**
     * 是否有連線資料庫
     * @return 是否有連線
     */
    boolean isConnected() throws SQLException;

    /**
     * @return 資料庫名稱
     */
    String getName();

    /**
     * @return 創建SQL操作器
     */
    SQL createSQL() throws SQLException;

    /**
     * @return 創建資料庫
     */
    CreateDatabase createDatabase();

    /**
     * @return 刪除資料庫
     */
    DeleteDatabase deleteDatabase();
}
