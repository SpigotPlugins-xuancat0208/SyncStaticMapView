package xuan.cat.syncstaticmapview.database.api.sql;

import java.sql.SQLException;

public interface SQLBuilder {
    String          asString();
    SQLBuilder      clone();
    SQL             callSQL(DatabaseConnection connection) throws SQLException;
}
