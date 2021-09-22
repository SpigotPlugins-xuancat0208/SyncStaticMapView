package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.builder.Field;
import xuan.cat.syncstaticmapview.database.api.sql.builder.FieldIndex;
import xuan.cat.syncstaticmapview.database.api.sql.builder.IndexType;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLPart;

/**
 * 索引
 */
public final class CodeFieldIndex implements CodeSQLPart, FieldIndex {
    private final String        name;
    private       IndexType     type    = IndexType.UNIQUE;
    private       String        field   = null;


    public <T> CodeFieldIndex(Field<T> field) {
        this.name   = field.name();
    }
    private CodeFieldIndex(CodeFieldIndex tableIndex) {
        this.name   = CodeFunction.tryClone(tableIndex.name);
        this.type   = CodeFunction.tryClone(tableIndex.type);
        this.field  = CodeFunction.tryClone(tableIndex.field);
    }


    public StringBuilder part() {
        StringBuilder builder = new StringBuilder();

        builder.append(type.part());
        builder.append(CodeFunction.toField(name()));
        builder.append(CodeFunction.brackets(CodeFunction.toField(field())));

        return builder;
    }

    public CodeFieldIndex clone() {
        return new CodeFieldIndex(this);
    }


    public CodeFieldIndex field(Enum<?> field) {
        return field(field.name());
    }
    public CodeFieldIndex field(String field) {
        this.field = field;
        return this;
    }

    public CodeFieldIndex type(IndexType type) {
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
