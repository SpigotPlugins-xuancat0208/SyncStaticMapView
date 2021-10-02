package xuan.cat.syncstaticmapview.database.sql;

import xuan.cat.syncstaticmapview.database.sql.builder.CreateDatabase;
import xuan.cat.syncstaticmapview.database.sql.builder.DeleteDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 資料庫
 */
public final class DatabaseConnection {
    private final MySQL         mySQL;
    private       Connection    connection; // 庫訪問資料連線
    private final String        name;


    public DatabaseConnection(MySQL mySQL, String name, Connection connection) {
        this.mySQL      = mySQL;
        this.name       = name;
        this.connection = connection;
    }


    /**
     * 斷開資料庫連線
     */
    public void disconnect() throws SQLException {
        // 斷開連線
        if (isConnected()) {
            connection.close();
        }
    }


    /**
     * @return 是否有連線資料庫
     */
    public boolean isConnected() throws SQLException {
        return !connection.isClosed();
    }


    /**
     * @return 資料庫名稱
     */
    public String getName() {
        return name;
    }


    /**
     * @return 創建SQL操作器
     */
    public SQL createSQL() throws SQLException {
        if (connection.isClosed())
            connection = mySQL.getDatabase(name).connection;
        return new SQL(connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
    }
}
