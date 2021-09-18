package xuan.cat.databasecatmini.api.sql;

import java.sql.SQLException;

/**
 * 資料庫連線
 */
public interface MySQL {
    /**
     * @param database 資料庫名稱
     * @return 資料庫是否存在
     */
    boolean existDatabase(String database) throws SQLException;

    /**
     * 取得或創建資料庫
     * @param database 資料庫名稱
     */
    DatabaseConnection getOrCreateDatabase(String database) throws SQLException;

    /**
     * 取得資料庫
     * @param database 資料庫名稱
     */
    DatabaseConnection getDatabase(String database) throws SQLException;

    /**
     * 創建資料庫
     * @param database 資料庫名稱
     */
    DatabaseConnection createDatabase(String database) throws SQLException;

    /**
     * 刪除資料庫
     * @param database 資料庫名稱
     */
    void deleteDatabase(String database) throws SQLException;
}
