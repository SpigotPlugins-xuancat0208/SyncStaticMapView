package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQLPart;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

/**
 * 索引
 */
public final class FieldIndex implements SQLPart {
    private final String    name;                       // 名稱
    private       IndexType type    = IndexType.UNIQUE; // 類型
    private       String    field   = null;             // 綁定欄位


    public <T> FieldIndex(Field<T> field) {
        this.name   = field.name();
    }
    private FieldIndex(FieldIndex tableIndex) {
        this.name   = SQLTool.tryClone(tableIndex.name);
        this.type   = SQLTool.tryClone(tableIndex.type);
        this.field  = SQLTool.tryClone(tableIndex.field);
    }


    public StringBuilder part() {
        StringBuilder builder = new StringBuilder();

        builder.append(type.part());
        builder.append(SQLTool.toField(name()));
        builder.append(SQLTool.brackets(SQLTool.toField(field())));

        return builder;
    }

    public FieldIndex clone() {
        return new FieldIndex(this);
    }


    public FieldIndex field(Enum<?> field) {
        return field(field.name());
    }
    public FieldIndex field(String field) {
        this.field = field;
        return this;
    }

    public FieldIndex type(IndexType type) {
        this.type = type;
        return this;
    }


    public String name() {
        return name != null ? name : field;
    }

    public IndexType type() {
        return type;
    }

    public String field() {
        return field != null ? field : name;
    }
}
