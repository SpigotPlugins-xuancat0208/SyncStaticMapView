package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.builder.DeleteDatabase;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLBuilder;

/**
 * 刪除數據庫
 */
public final class CodeDeleteDatabase implements CodeSQLBuilder, DeleteDatabase {
    private final String name;  // 資料表名稱


    public CodeDeleteDatabase(String name) {
        this.name   = name;
    }
    private CodeDeleteDatabase(CodeDeleteDatabase deleteDatabase) {
        this.name   = CodeFunction.tryClone(deleteDatabase.name);
    }


    public String asString() {
        return "DROP DATABASE " + CodeFunction.toField(name);
    }

    public CodeDeleteDatabase clone() {
        return new CodeDeleteDatabase(this);
    }


    public String name() {
        return name;
    }
}
