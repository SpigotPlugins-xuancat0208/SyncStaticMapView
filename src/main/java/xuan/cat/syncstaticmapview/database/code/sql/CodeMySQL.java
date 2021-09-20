package xuan.cat.syncstaticmapview.database.code.sql;

import xuan.cat.syncstaticmapview.database.api.sql.MySQL;
import xuan.cat.syncstaticmapview.database.api.sql.SQL;
import xuan.cat.syncstaticmapview.database.code.sql.builder.CodeFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class CodeMySQL implements MySQL {

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
    public CodeMySQL(String ip, int port, String user, String password) {
        this.ip         = ip;
        this.port       = port;
        this.user       = user;
        this.password   = password;
    }


    public CodeDatabaseConnection getDatabase(String database) throws SQLException {
        return new CodeDatabaseConnection(this, database, DriverManager.getConnection(getURL(database), user, password));
    }

    public CodeDatabaseConnection getOrCreateDatabase(String database) throws SQLException {
        return existDatabase(database) ? getDatabase(database) : createDatabase(database);
    }

    public boolean existDatabase(String database) throws SQLException {
        Connection  connection  = DriverManager.getConnection(getURL(), user, password);
        SQL         sql         = createSQL(connection);

        if (sql.Q("SHOW DATABASES;"))
            while (sql.N()) {
                // 是否已經存在資料庫
                if (database.equals(sql.getString("Database"))) {
                    connection.close();
                    sql.C();
                    return true;
                }
            }

        connection.close();
        sql.C();
        return false;
    }
    public CodeDatabaseConnection createDatabase(String database) throws SQLException {
        Connection  connection  = DriverManager.getConnection(getURL(), user, password);
        createSQL(connection).UC("CREATE DATABASE " + CodeFunction.toField(database) + ";");
        connection.close();

        return getDatabase(database);
    }


    private CodeSQL createSQL(Connection connection) throws SQLException {
        return new CodeSQL(connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
    }

    private String getURL() {
        return "jdbc:mysql://" + ip + ":" + port + "/?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8&";
    }
    private String getURL(String database) {
        return "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT%2B8&";
    }
}
