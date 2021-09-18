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
    private       CodeOrder                 order               = null;
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
        this.order              = CodeFunction.tryClone(selectData.order);
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

        if (order != null)
            builder.append(order.part());

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



    public CodeSelectData selectDistinct(boolean selectDistinct) {
        this.selectDistinct = selectDistinct;
        return this;
    }

    public <T> CodeSelectData select(Field<T> field) {
        return select(field, SelectReturn.NOT);
    }
    public CodeSelectData select(SelectReturn dataReturn) {
        if (dataReturn == SelectReturn.ALL) {
            selectAll = true;
            return this;
        } else {
            return select(null, dataReturn);
        }
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
    public CodeSelectData offset(Integer offset) {
        this.offset = offset;
        return this;
    }


    public CodeSelectData order(Order order) {
        this.order = (CodeOrder) order;
        return this;
    }
    public CodeSelectData order(Consumer<Order> consumer) {
        if (this.order == null)
            this.order = new CodeOrder();
        consumer.accept(this.order);
        return this;
    }


    public CodeSelectData union(SelectData union) {
        this.union = (CodeSelectData) union;
        return this;
    }

    public CodeSelectData unionAll(SelectData unionAll) {
        this.unionAll = (CodeSelectData) unionAll;
        return this;
    }

    public CodeSelectData useCache(boolean useCache) {
        this.useCache = useCache;
        return this;
    }

    public CodeSelectData bufferResult(boolean bufferResult) {
        this.bufferResult = bufferResult;
        return this;
    }


    /*
    public void selectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
    public boolean selectAll() {
        return selectAll;
    }
     */

    public boolean selectDistinct() {
        return selectDistinct;
    }

    public Integer limit() {
        return limit;
    }

    public Integer offset() {
        return offset;
    }

    public Map<Field, SelectReturn> selects() {
        return selects;
    }

    public CodeOrder order() {
        return order;
    }

    public String from() {
        return from;
    }

    public InformationSchema informationSchema() {
        return informationSchema;
    }

    public CodeWhere where() {
        return where;
    }

    public CodeSelectData union() {
        return union;
    }

    public CodeSelectData unionAll() {
        return unionAll;
    }

    public boolean bufferResult() {
        return bufferResult;
    }

    public boolean useCache() {
        return useCache;
    }
}
