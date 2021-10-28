package xuan.cat.syncstaticmapview.database.sql;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 資料庫連線
 */
public final class MySQL {
    private final String ip;
    private final int  port;
    private final String user;
    private final String password;
    private final Map<String, Object> options;


    /**
     * 資料庫
     * @param ip 資料庫地址
     * @param port 資料庫端口
     * @param user 資料庫帳號
     * @param password 資料庫密碼
     * @param options 額外選項
     */
    public MySQL(String ip, int port, String user, String password, Map<String, Object> options) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
        this.options = options;
        options.putIfAbsent("useUnicode", true);
        options.putIfAbsent("characterEncoding", StandardCharsets.UTF_8);
        options.putIfAbsent("useSSL", false);
        options.putIfAbsent("serverTimezone", "GMT+8");
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

    private String strOptions() {
        StringBuilder builder = new StringBuilder();
        options.forEach((key, value) -> {
            if (builder.length() > 0)
                builder.append('&');
            try {
                builder.append(URLEncoder.encode(key, "UTF-8"));
                builder.append('=');
                builder.append(URLEncoder.encode(value.toString(), "UTF-8"));
            } catch (UnsupportedEncodingException exception) {
                exception.printStackTrace();
            }
        });
        return builder.toString();
    }

    private String getURL() {
        return "jdbc:mysql://" + ip + ":" + port + "/?" + strOptions();
    }
    private String getURL(String database) {
        return "jdbc:mysql://" + ip + ":" + port + "/" + database + "?" + strOptions();
    }
}
