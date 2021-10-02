package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.sql.SQLCommand;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * 選擇資料
 */
public final class SelectData implements SQLCommand {
    private final String                    from;
    private final InformationSchema         informationSchema;
    private final Map<Field, SelectReturn>  selects;
    private       boolean                   selectAll           = true;
    private       boolean                   selectDistinct      = false;
    private       Where                     where               = null;
    private       Order                     order               = null;
    private       Integer                   limit               = null;
    private       Integer                   offset              = null;
    private       SelectData                union               = null;
    private       SelectData                unionAll            = null;
    private       boolean                   useCache            = false;
    private       boolean                   bufferResult        = false;


    public SelectData(DatabaseTable table) {
        this.selects            = new HashMap<>();
        this.from               = table.getName();
        this.informationSchema  = null;
    }
    public SelectData(InformationSchema informationSchema) {
        this.selects            = new HashMap<>();
        this.from               = null;
        this.informationSchema  = informationSchema;
    }
    private SelectData(SelectData selectData) {
        this.from               = SQLTool.tryClone(selectData.from);
        this.informationSchema  = SQLTool.tryClone(selectData.informationSchema);
        this.selects            = SQLTool.tryClone(selectData.selects);
        this.selectAll          = SQLTool.tryClone(selectData.selectAll);
        this.selectDistinct     = SQLTool.tryClone(selectData.selectDistinct);
        this.where              = SQLTool.tryClone(selectData.where);
        this.order              = SQLTool.tryClone(selectData.order);
        this.limit              = SQLTool.tryClone(selectData.limit);
        this.offset             = SQLTool.tryClone(selectData.offset);
        this.union              = SQLTool.tryClone(selectData.union);
        this.unionAll           = SQLTool.tryClone(selectData.unionAll);
        this.useCache           = SQLTool.tryClone(selectData.useCache);
        this.bufferResult       = SQLTool.tryClone(selectData.bufferResult);
    }


    public String toCommand() {
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
                list.add(SQLTool.combination(dataReturn.part(field != null ? SQLTool.toField(field.name()) : dataReturn.getDefaultValue())));
            });
            if (hasCalcFoundRows.get())
                builder.append("SQL_CALC_FOUND_ROWS ");
            builder.append(SQLTool.toStringFromList(list, StringBuilder::append));
        } else {
            // SELECT *
            builder.append('*');
        }

        builder.append(" FROM ");
        builder.append(informationSchema != null ? informationSchema.part() : SQLTool.toField(from));
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
            builder.append(union.toCommand());
        }

        if (unionAll != null) {
            builder.append(" UNION ALL ");
            builder.append(unionAll.toCommand());
        }

        return builder.toString();
    }

    public SelectData clone() {
        return new SelectData(this);
    }



    public SelectData selectDistinct(boolean selectDistinct) {
        this.selectDistinct = selectDistinct;
        return this;
    }

    public <T> SelectData select(Field<T> field) {
        return select(field, SelectReturn.NOT);
    }
    public SelectData select(SelectReturn dataReturn) {
        if (dataReturn == SelectReturn.ALL) {
            selectAll = true;
            return this;
        } else {
            return select(null, dataReturn);
        }
    }
    public <T> SelectData select(Field<T> field, SelectReturn dataReturn) {
        selectAll = false;
        selects.put(field, dataReturn);
        return this;
    }



    public SelectData where(Where where) {
        this.where = (Where) where;
        return this;
    }
    public SelectData where(Consumer<Where> consumer) {
        if (this.where == null)
            this.where = new Where();
        consumer.accept(this.where);
        return this;
    }
    public SelectData brackets(Consumer<WhereBrackets> consumer) {
        if (this.where == null || !(this.where instanceof WhereBrackets)) {
            WhereBrackets brackets = new WhereBrackets();
            consumer.accept(brackets);
            this.where = brackets;
        } else {
            consumer.accept((WhereBrackets) this.where);
        }
        return this;
    }
    public SelectData brackets(WhereBrackets brackets) {
        return where(brackets);
    }


    public SelectData limit(Integer limit) {
        this.limit = limit;
        return this;
    }
    public SelectData offset(Integer offset) {
        this.offset = offset;
        return this;
    }


    public SelectData order(Order order) {
        this.order = (Order) order;
        return this;
    }
    public SelectData order(Consumer<Order> consumer) {
        if (this.order == null)
            this.order = new Order();
        consumer.accept(this.order);
        return this;
    }


    public SelectData union(SelectData union) {
        this.union = (SelectData) union;
        return this;
    }

    public SelectData unionAll(SelectData unionAll) {
        this.unionAll = (SelectData) unionAll;
        return this;
    }

    public SelectData useCache(boolean useCache) {
        this.useCache = useCache;
        return this;
    }

    public SelectData bufferResult(boolean bufferResult) {
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

    public Order order() {
        return order;
    }

    public String from() {
        return from;
    }

    public InformationSchema informationSchema() {
        return informationSchema;
    }

    public Where where() {
        return where;
    }

    public SelectData union() {
        return union;
    }

    public SelectData unionAll() {
        return unionAll;
    }

    public boolean bufferResult() {
        return bufferResult;
    }

    public boolean useCache() {
        return useCache;
    }
}
