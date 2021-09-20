package xuan.cat.syncstaticmapview.database.code.sql;

import xuan.cat.syncstaticmapview.database.api.sql.DatabaseConnection;
import xuan.cat.syncstaticmapview.database.api.sql.SQL;
import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;

import java.sql.SQLException;

/**
 * SQL語法生成器
 */
public interface CodeSQLBuilder extends SQLBuilder {
    default SQL callSQL(DatabaseConnection connection) throws SQLException {
        CodeSQL sql = ((CodeDatabaseConnection) connection).createSQL();
        sql.setSQL(this);
        return sql;
    }
}
