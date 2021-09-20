package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.api.sql.builder.DeleteData;
import xuan.cat.syncstaticmapview.database.api.sql.builder.Where;
import xuan.cat.syncstaticmapview.database.api.sql.builder.WhereBrackets;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLBuilder;

import java.util.function.Consumer;

/**
 * 刪除資料
 */
public final class CodeDeleteData implements CodeSQLBuilder, DeleteData {

    private final String    name;
    private       CodeWhere where   = null;
    private       Integer   limit   = null;
    private       Integer   offset  = null;


    public CodeDeleteData(DatabaseTable table) {
        this.name       = table.getName();
    }
    private CodeDeleteData(CodeDeleteData deleteData) {
        this.name       = CodeFunction.tryClone(deleteData.name);
        this.where      = CodeFunction.tryClone(deleteData.where);
        this.limit      = CodeFunction.tryClone(deleteData.limit);
        this.offset     = CodeFunction.tryClone(deleteData.offset);
    }


    public String asString() {
        StringBuilder builder = new StringBuilder();

        builder.append("DELETE FROM ");
        builder.append(CodeFunction.toField(name));
        builder.append(' ');

        if (where != null)
            builder.append(where.part());

        if (limit != null) {
            builder.append(" LIMIT ");
            builder.append(limit);
            if (offset != null) {
                builder.append(" OFFSET ");
                builder.append(offset);
            }
        }

        return builder.toString();
    }

    public CodeDeleteData clone() {
        return new CodeDeleteData(this);
    }



    public CodeDeleteData limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public CodeDeleteData where(Where where) {
        this.where = (CodeWhere) where;
        return this;
    }
    public CodeDeleteData where(Consumer<Where> consumer) {
        if (this.where == null)
            this.where = new CodeWhere();
        consumer.accept(this.where);
        return this;
    }
    public CodeDeleteData brackets(Consumer<WhereBrackets> consumer) {
        if (this.where == null || !(this.where instanceof CodeWhereBrackets)) {
            CodeWhereBrackets brackets = new CodeWhereBrackets();
            consumer.accept(brackets);
            this.where = brackets;
        } else {
            consumer.accept((CodeWhereBrackets) this.where);
        }
        return this;
    }
    public CodeDeleteData brackets(WhereBrackets brackets) {
        return where(brackets);
    }



    public CodeWhere where() {
        return where;
    }

    public String name() {
        return name;
    }

}
