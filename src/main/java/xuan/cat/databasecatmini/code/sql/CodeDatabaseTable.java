package xuan.cat.databasecatmini.code.sql;

import xuan.cat.databasecatmini.api.sql.DatabaseConnection;
import xuan.cat.databasecatmini.api.sql.DatabaseTable;
import xuan.cat.databasecatmini.api.sql.SQL;
import xuan.cat.databasecatmini.api.sql.builder.Field;
import xuan.cat.databasecatmini.api.sql.builder.FieldStyle;
import xuan.cat.databasecatmini.api.sql.builder.InformationSchema;
import xuan.cat.databasecatmini.code.sql.builder.*;

import java.sql.SQLException;

public final class CodeDatabaseTable implements DatabaseTable {

    private final String name;

    public CodeDatabaseTable(String name) {
        this.name   = name;
    }


    public String getName() {
        return name;
    }


    public boolean existTable(DatabaseConnection database) throws SQLException {
        Field<String>   tableName   = new CodeField<>(FieldStyle.TEXT, "TABLE_NAME");
        SQL             sql         = database.createSQL();
        return sql.QC(new CodeSelectData(InformationSchema.TABLES).select(tableName).where(w -> w.and(new CodeField<>(FieldStyle.TEXT, "TABLE_SCHEMA"), database.getName()).and(tableName, name)).limit(1));
    }


    public CodeCreateTable createTable() {
        return new CodeCreateTable(this);
    }


    public CodeDeleteData deleteData() {
        return new CodeDeleteData(this);
    }

    public CodeInsertData insertData() {
        return new CodeInsertData(this);
    }

    public CodeSelectData selectData() {
        return new CodeSelectData(this);
    }
    public CodeSelectData selectData(InformationSchema informationSchema) {
        return new CodeSelectData(informationSchema);
    }

    public CodeUpdateData updateData() {
        return new CodeUpdateData(this);
    }
}
