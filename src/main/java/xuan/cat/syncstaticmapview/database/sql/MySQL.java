package xuan.cat.syncstaticmapview.database.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 資料庫連線
 */
public final class MySQL {
    private final   String      ip;
    private final   int         port;
    private final   String      user;
    private final   String      password;


    /**
     * 資料庫
     * @param ip 資料庫地址
     * @param port 資料庫端口
     * @param user 資料庫帳號
     * @param password 資料庫密碼
     */
    public MySQL(String ip, int port, String user, String password) {
        this.ip         = ip;
        this.port       = port;
        this.user       = user;
        this.password   = password;
    }


    /**
     * 取得資料庫
     * @param database 資料庫名稱
     */
    public DatabaseConnection getDatabase(String database) throws SQLException {
        return new DatabaseConnection(this, database, DriverManager.getConnection(getURL(database), user, password));
    }

    /**
     * 取得或創建資料庫
     * @param database 資料庫名稱
     */
    public DatabaseConnection getOrCreateDatabase(String database) throws SQLException {
        return existDatabase(database) ? getDatabase(database) : createDatabase(database);
    }

    /**
     * @param database 資料庫名稱
     * @return 資料庫是否存在
     */
    public boolean existDatabase(String database) throws SQLException {
        Connection  connection  = DriverManager.getConnection(getURL(), user, password);
        SQL         sql         = createSQL(connection);

        if (sql.Q("SHOW DATABASES;"))
            while (sql.N()) {
                // 是否已經存在資料庫
                if (database.equalsIgnoreCase(sql.getString("Database"))) {
                    connection.close();
                    sql.C();
                    return true;
                }
            }

        connection.close();
        sql.C();
        return false;
    }

    /**
     * 創建資料庫
     * @param database 資料庫名稱
     */
    public DatabaseConnection createDatabase(String database) throws SQLException {
        Connection  connection  = DriverManager.getConnection(getURL(), user, password);
        createSQL(connection).UC("CREATE DATABASE " + SQLTool.toField(database) + ";");
        connection.close();

        return getDatabase(database);
    }



    private SQL createSQL(Connection connection) throws SQLException {
        return new SQL(connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
    }

    private String getURL() {
        return "jdbc:mysql://" + ip + ":" + port + "/?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8&";
    }
    private String getURL(String database) {
        return "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8&";
    }
}
