package xuan.cat.syncstaticmapview.database.api.sql;

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
}
