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
    private       Where                     where               = null;
    private       Integer                   limit               = null;


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
        this.where              = SQLTool.tryClone(selectData.where);
        this.limit              = SQLTool.tryClone(selectData.limit);
    }


    public String toCommand() {
        StringBuilder builder = new StringBuilder();

        builder.append("SELECT ");

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

        if (limit != null) {
            builder.append(" LIMIT ");
            builder.append(limit);
        }

        return builder.toString();
    }

    public SelectData clone() {
        return new SelectData(this);
    }


    public <T> SelectData select(Field<T> field) {
        return select(field, SelectReturn.NOT);
    }
    public <T> SelectData select(Field<T> field, SelectReturn dataReturn) {
        selectAll = false;
        selects.put(field, dataReturn);
        return this;
    }



    public SelectData where(Consumer<Where> consumer) {
        if (this.where == null)
            this.where = new Where();
        consumer.accept(this.where);
        return this;
    }


    public SelectData limit(Integer limit) {
        this.limit = limit;
        return this;
    }
}
