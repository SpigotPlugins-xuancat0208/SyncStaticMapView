package xuan.cat.syncstaticmapview.database.code.sql;

import xuan.cat.syncstaticmapview.database.api.sql.DatabaseConnection;
import xuan.cat.syncstaticmapview.database.code.sql.builder.CodeCreateDatabase;
import xuan.cat.syncstaticmapview.database.code.sql.builder.CodeDeleteDatabase;

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

    /*
    protected final void connect() throws SQLException {
        if (!isConnected()) {
            // 連線
            BoneCPDataSource config = new BoneCPDataSource();
            config.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC");
            config.setUsername(user);
            config.setPassword(password);
            // 每個分區的最大連線數
            //config.setMaxConnectionsPerPartition(60);
            // 分區數
            //config.setPartitionCount(10);
            // 每次連接的時候一次性拿幾個
            //config.setAcquireIncrement(100);
            // 執行續數量
            //config.setReleaseHelperThreads(4);
            connection = config.getConnection();//DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC", user, password); // 連線 mySQL 資料庫
        }
    }

     */

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
