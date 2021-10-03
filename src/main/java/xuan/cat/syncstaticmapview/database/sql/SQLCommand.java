package xuan.cat.syncstaticmapview.database.sql;

import java.sql.SQLException;

/**
 * SQL語法生成器
 */
public interface SQLCommand {
    /**
     * @return 轉出成為字符串
     */
    String toCommand();

    /**
     * @return 複製
     */
    SQLCommand clone();

    /**
     * 創建新的SQL操作器, 並將指令寫入
     * @param connection 資料庫連線
     * @return SQL操作器
     */
    default SQL callSQL(DatabaseConnection connection) throws SQLException {
        SQL sql = connection.createSQL();
        sql.setSQL(this);
        return sql;
    }
}
