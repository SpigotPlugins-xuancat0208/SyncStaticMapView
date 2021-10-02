package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.sql.SQLPart;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

/**
 * 外來鍵
 */
public final class ForeignKey implements SQLPart {
    private final String                name;               // 來源名稱
    private       String                from        = null; // 目標欄位表
    private       String                table       = null; // 目標欄位表欄位
    private       ForeignConstraints    onDelete    = null; // 刪除限制
    private       ForeignConstraints    onUpdate    = null; // 更新限制


    public <T> ForeignKey(Field<T> field) {
        this.name       = field.name();
    }
    private ForeignKey(ForeignKey foreignKey) {
        this.name       = SQLTool.tryClone(foreignKey.name);
        this.from       = SQLTool.tryClone(foreignKey.from);
        this.table      = SQLTool.tryClone(foreignKey.table);
        this.onDelete   = SQLTool.tryClone(foreignKey.onDelete);
        this.onUpdate   = SQLTool.tryClone(foreignKey.onUpdate);
    }


    public StringBuilder part() {
        StringBuilder builder = new StringBuilder();

        builder.append("FOREIGN KEY ");
        builder.append(SQLTool.brackets(SQLTool.toField(name)));

        builder.append(" REFERENCES ");
        builder.append(SQLTool.toField(from));
        builder.append(SQLTool.brackets(SQLTool.toField(table)));

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

    public ForeignKey clone() {
        return new ForeignKey(this);
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
    public ForeignKey from(String from) {
        this.from = from;
        return this;
    }
    public ForeignKey from(Enum<?> from) {
        return from(from.name());
    }

    public ForeignKey onDelete(ForeignConstraints onDelete) {
        this.onDelete = onDelete;
        return this;
    }

    public ForeignKey onUpdate(ForeignConstraints onUpdate) {
        this.onUpdate = onUpdate;
        return this;
    }

    public ForeignKey table(String table) {
        this.table = table;
        return this;
    }
    public ForeignKey table(Enum<?> table) {
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
