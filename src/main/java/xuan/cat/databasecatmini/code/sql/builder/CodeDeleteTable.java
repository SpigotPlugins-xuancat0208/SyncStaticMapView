package xuan.cat.databasecatmini.code.sql.builder;

import xuan.cat.databasecatmini.api.sql.DatabaseTable;
import xuan.cat.databasecatmini.api.sql.builder.DeleteTable;
import xuan.cat.databasecatmini.code.sql.CodeSQLBuilder;

/**
 * 刪除資料表
 */
public final class CodeDeleteTable implements CodeSQLBuilder, DeleteTable {

    private final String name;

    public CodeDeleteTable(DatabaseTable table) {
        this.name   = table.getName();
    }
    private CodeDeleteTable(CodeDeleteTable deleteTable) {
        this.name   = CodeFunction.tryClone(deleteTable.name);
    }

    public String asString() {
        return "DROP TABLE " + CodeFunction.toField(name);
    }

    public CodeDeleteTable clone() {
        return new CodeDeleteTable(this);
    }



    public String name() {
        return name;
    }
}
