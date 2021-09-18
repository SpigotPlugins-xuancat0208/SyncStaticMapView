package xuan.cat.databasecatmini.code.sql.builder;

import xuan.cat.databasecatmini.api.sql.DatabaseTable;
import xuan.cat.databasecatmini.api.sql.builder.Field;
import xuan.cat.databasecatmini.api.sql.builder.ForeignConstraints;
import xuan.cat.databasecatmini.api.sql.builder.ForeignKey;
import xuan.cat.databasecatmini.code.sql.CodeSQLPart;

/**
 * 外來鍵
 */
public final class CodeForeignKey implements CodeSQLPart, ForeignKey {

    private final String                name;               // 來源名稱
    private       String                from        = null; // 目標欄位表
    private       String                table       = null; // 目標欄位表欄位
    private       ForeignConstraints    onDelete    = null; // 刪除限制
    private       ForeignConstraints    onUpdate    = null; // 更新限制


    public <T> CodeForeignKey(Field<T> field) {
        this.name       = field.name();
    }
    private CodeForeignKey(CodeForeignKey foreignKey) {
        this.name       = CodeFunction.tryClone(foreignKey.name);
        this.from       = CodeFunction.tryClone(foreignKey.from);
        this.table      = CodeFunction.tryClone(foreignKey.table);
        this.onDelete   = CodeFunction.tryClone(foreignKey.onDelete);
        this.onUpdate   = CodeFunction.tryClone(foreignKey.onUpdate);
    }


    public StringBuilder part() {
        StringBuilder builder = new StringBuilder();

        builder.append("FOREIGN KEY ");
        builder.append(CodeFunction.brackets(CodeFunction.toField(name)));

        builder.append(" REFERENCES ");
        builder.append(CodeFunction.toField(from));
        builder.append(CodeFunction.brackets(CodeFunction.toField(table)));

        if (onDelete != null) {
            builder.append(" ON DELETE ");
            builder.append(onDelete.part());
        }

        if (onUpdate != null) {
            builder.append(" ON UPDATE ");
            builder.append(onUpdate.part());
        }

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


    public ForeignKey from(DatabaseTable table) {
        return from(table.getName());
    }
    public CodeForeignKey from(String from) {
        this.from = from;
        return this;
    }
    public CodeForeignKey from(Enum<?> from) {
        return from(from.name());
    }

    public CodeForeignKey onDelete(ForeignConstraints onDelete) {
        this.onDelete = onDelete;
        return this;
    }

    public CodeForeignKey onUpdate(ForeignConstraints onUpdate) {
        this.onUpdate = onUpdate;
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

    public String from() {
        return from;
    }

    public String table() {
        return table;
    }

    public ForeignConstraints onDelete() {
        return onDelete;
    }

    public ForeignConstraints onUpdate() {
        return onUpdate;
    }
}
