package xuan.cat.databasecatmini.code.sql;

import xuan.cat.databasecatmini.api.sql.DatabaseConnection;
import xuan.cat.databasecatmini.code.sql.builder.CodeCreateDatabase;
import xuan.cat.databasecatmini.code.sql.builder.CodeDeleteDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class CodeDatabaseConnection implements DatabaseConnection {

    private final CodeMySQL     mySQL;
    private       Connection    connection; // 庫訪問資料連線
    private final String        name;


    public CodeDatabaseConnection(CodeMySQL mySQL, String name, Connection connection) {
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
     * 是否有連線資料庫
     * @return 是否有連線
     */
    public boolean isConnected() throws SQLException {
        return !connection.isClosed();
    }



    public CodeSQL createSQL() throws SQLException {
        if (connection.isClosed())
            connection = mySQL.getDatabase(name).connection;
        return new CodeSQL(connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
    }


    public String getName() {
        return name;
    }


    public CodeCreateDatabase createDatabase() {
        return new CodeCreateDatabase(getName());
    }

    public CodeDeleteDatabase deleteDatabase() {
        return new CodeDeleteDatabase(getName());
    }

}
