package xuan.cat.databasecatmini.code.sql.builder;

import xuan.cat.databasecatmini.api.sql.DatabaseTable;
import xuan.cat.databasecatmini.api.sql.builder.*;
import xuan.cat.databasecatmini.code.sql.CodeSQLBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * 選擇資料
 */
public final class CodeSelectData implements CodeSQLBuilder, SelectData {

    private final String                    from;
    private final InformationSchema         informationSchema;
    private final Map<Field, SelectReturn>  selects;
    private       boolean                   selectAll           = true;
    private       boolean                   selectDistinct      = false;
    private       CodeWhere                 where               = null;
    private       Integer                   limit               = null;
    private       Integer                   offset              = null;
    private       CodeSelectData            union               = null;
    private       CodeSelectData            unionAll            = null;
    private       boolean                   useCache            = false;
    private       boolean                   bufferResult        = false;

    public CodeSelectData(DatabaseTable table) {
        this.selects            = new HashMap<>();
        this.from               = table.getName();
        this.informationSchema  = null;
    }
    public CodeSelectData(InformationSchema informationSchema) {
        this.selects            = new HashMap<>();
        this.from               = null;
        this.informationSchema  = informationSchema;
    }
    private CodeSelectData(CodeSelectData selectData) {
        this.from               = CodeFunction.tryClone(selectData.from);
        this.informationSchema  = CodeFunction.tryClone(selectData.informationSchema);
        this.selects            = CodeFunction.tryClone(selectData.selects);
        this.selectAll          = CodeFunction.tryClone(selectData.selectAll);
        this.selectDistinct     = CodeFunction.tryClone(selectData.selectDistinct);
        this.where              = CodeFunction.tryClone(selectData.where);
        this.limit              = CodeFunction.tryClone(selectData.limit);
        this.offset             = CodeFunction.tryClone(selectData.offset);
        this.union              = CodeFunction.tryClone(selectData.union);
        this.unionAll           = CodeFunction.tryClone(selectData.unionAll);
        this.useCache           = CodeFunction.tryClone(selectData.useCache);
        this.bufferResult       = CodeFunction.tryClone(selectData.bufferResult);
    }

    public String asString() {
        StringBuilder builder = new StringBuilder();

        builder.append("SELECT ");
        if (useCache)
            builder.append("SQL_CACHE ");
        if (bufferResult)
            builder.append("SQL_BUFFER_RESULT ");
        if (selectDistinct)
            builder.append("DISTINCT ");

        if (selects.size() > 0) {
            List<CharSequence>  list                = new ArrayList<>( selects.size() );
            AtomicBoolean       hasCalcFoundRows    = new AtomicBoolean(false);
            if (selectAll)
                list.add("*");
            selects.forEach((field, dataReturn) -> {
                if (dataReturn.isCalcFoundRows())
                    hasCalcFoundRows.set(true);
                list.add(CodeFunction.combination(dataReturn.part(field != null ? CodeFunction.toField(field.name()) : dataReturn.getDefaultValue())));
            });
            if (hasCalcFoundRows.get())
                builder.append("SQL_CALC_FOUND_ROWS ");
            builder.append(CodeFunction.toStringFromList(list, StringBuilder::append));
        } else {
            // SELECT *
            builder.append('*');
        }

        builder.append(" FROM ");
        builder.append(informationSchema != null ? informationSchema.part() : CodeFunction.toField(from));
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

        if (union != null) {
            builder.append(" UNION ");
            builder.append(union.asString());
        }

        if (unionAll != null) {
            builder.append(" UNION ALL ");
            builder.append(unionAll.asString());
        }

        return builder.toString();
    }

    public CodeSelectData clone() {
        return new CodeSelectData(this);
    }


    public <T> CodeSelectData select(Field<T> field) {
        return select(field, SelectReturn.NOT);
    }

    public <T> CodeSelectData select(Field<T> field, SelectReturn dataReturn) {
        selectAll = false;
        selects.put(field, dataReturn);
        return this;
    }



    public CodeSelectData where(Where where) {
        this.where = (CodeWhere) where;
        return this;
    }
    public CodeSelectData where(Consumer<Where> consumer) {
        if (this.where == null)
            this.where = new CodeWhere();
        consumer.accept(this.where);
        return this;
    }
    public CodeSelectData brackets(Consumer<WhereBrackets> consumer) {
        if (this.where == null || !(this.where instanceof WhereBrackets)) {
            CodeWhereBrackets brackets = new CodeWhereBrackets();
            consumer.accept(brackets);
            this.where = brackets;
        } else {
            consumer.accept((WhereBrackets) this.where);
        }
        return this;
    }
    public CodeSelectData brackets(WhereBrackets brackets) {
        return where(brackets);
    }


    public CodeSelectData limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public CodeWhere where() {
        return where;
    }

}
