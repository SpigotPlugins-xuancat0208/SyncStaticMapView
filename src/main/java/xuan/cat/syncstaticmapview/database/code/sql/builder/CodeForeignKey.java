package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.builder.ForeignKey;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLPart;

/**
 * 外來鍵
 */
public final class CodeForeignKey implements CodeSQLPart, ForeignKey {

    private final String                name;               // 來源名稱
    private       String                from        = null; // 目標欄位表
    private       String                table       = null; // 目標欄位表欄位


    private CodeForeignKey(CodeForeignKey foreignKey) {
        this.name       = CodeFunction.tryClone(foreignKey.name);
        this.from       = CodeFunction.tryClone(foreignKey.from);
        this.table      = CodeFunction.tryClone(foreignKey.table);
    }


    public StringBuilder part() {
        StringBuilder builder = new StringBuilder();

        builder.append("FOREIGN KEY ");
        builder.append(CodeFunction.brackets(CodeFunction.toField(name)));

        builder.append(" REFERENCES ");
        builder.append(CodeFunction.toField(from));
        builder.append(CodeFunction.brackets(CodeFunction.toField(table)));

        return builder;
    }

    public CodeForeignKey clone() {
        return new CodeForeignKey(this);
    }


//    public CodeForeignKey name(String name) {
//        this.name = name;
//        return this;
//    }
//    public CodeForeignKey name(Enum<?> name) {
//        return name(name.name());
//    }


    public CodeForeignKey from(String from) {
        this.from = from;
        return this;
    }

    public CodeForeignKey table(String table) {
        this.table = table;
        return this;
    }
    public CodeForeignKey table(Enum<?> table) {
        return table(table.name());
    }


    public String name() {
        return name;
    }

    public String table() {
        return table;
    }
}
