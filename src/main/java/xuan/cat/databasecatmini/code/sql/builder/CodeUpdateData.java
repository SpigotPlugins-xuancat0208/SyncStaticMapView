package xuan.cat.databasecatmini.code.sql.builder;

import xuan.cat.databasecatmini.api.sql.DatabaseTable;
import xuan.cat.databasecatmini.api.sql.builder.*;
import xuan.cat.databasecatmini.code.sql.CodeSQLBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 更新資料
 */
public final class CodeUpdateData implements CodeSQLBuilder, UpdateData {

    private final String                    name;
    private       CodeWhere                 where       = null;
    private       Integer                   limit       = null;
    private       Integer                   offset      = null;
    private       CodeOrder                 order       = null;
    private final Map<Field, Change>        updates;           // 欄位對應值清單
    private       boolean                   lowPriority = false;


    private static class Change<T> {
        Change(Field field, Object value, UpdateAlgorithm algorithm) {
            this.field      = field;
            this.value      = value;
            this.algorithm  = algorithm;
        }

        public Field            field;
        public Object           value;
        public UpdateAlgorithm  algorithm;
    }


    public CodeUpdateData(DatabaseTable table) {
        this.name           = table.getName();
        this.updates        = new HashMap<>();
    }
    private CodeUpdateData(CodeUpdateData updateData) {
        this.name           = CodeFunction.tryClone(updateData.name);
        this.where          = CodeFunction.tryClone(updateData.where);
        this.limit          = CodeFunction.tryClone(updateData.limit);
        this.offset         = CodeFunction.tryClone(updateData.offset);
        this.order          = CodeFunction.tryClone(updateData.order);
        this.updates        = CodeFunction.tryClone(updateData.updates);
        this.lowPriority    = CodeFunction.tryClone(updateData.lowPriority);
    }


    public String asString() {
        StringBuilder builder = new StringBuilder();

        builder.append("UPDATE ");
        if (lowPriority) {
            builder.append("LOW_PRIORITY ");
        }
        builder.append(CodeFunction.toField(name));

        builder.append(" SET ");
        builder.append(CodeFunction.toStringFromMap(updates, (merge, field, change) -> {
            switch (change.algorithm) {
                case EQUAL:
                default:
                    merge.append(CodeFunction.toField(field.name()));
                    merge.append('=');
                    merge.append(CodeFunction.toValue(field, change.value));
                    break;
                case ADDITION:
                    merge.append(CodeFunction.toField(field.name()));
                    merge.append('=');
                    merge.append(CodeFunction.toField(field.name()));
                    merge.append('+');
                    merge.append(CodeFunction.toValue(field, change.value));
                    break;
                case SUBTRACTION:
                    merge.append(CodeFunction.toField(field.name()));
                    merge.append('=');
                    merge.append(CodeFunction.toField(field.name()));
                    merge.append('-');
                    merge.append(CodeFunction.toValue(field, change.value));
                    break;
            }
        }));
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

        return builder.toString();
    }

    public CodeUpdateData clone() {
        return new CodeUpdateData(this);
    }


    public CodeUpdateData where(Where where) {
        this.where = (CodeWhere) where;
        return this;
    }
    public CodeUpdateData where(Consumer<Where> consumer) {
        if (this.where == null)
            this.where = new CodeWhere();
        consumer.accept(this.where);
        return this;
    }
    public CodeUpdateData brackets(Consumer<WhereBrackets> consumer) {
        if (this.where == null || !(this.where instanceof WhereBrackets)) {
            CodeWhereBrackets brackets = new CodeWhereBrackets();
            consumer.accept(brackets);
            this.where = brackets;
        } else {
            consumer.accept((WhereBrackets) this.where);
        }
        return this;
    }
    public CodeUpdateData brackets(WhereBrackets brackets) {
        return where(brackets);
    }

    public CodeUpdateData order(Order order) {
        this.order = (CodeOrder) order;
        return this;
    }
    public CodeUpdateData order(Consumer<Order> consumer) {
        if (this.order == null)
            this.order = new CodeOrder();
        consumer.accept(this.order);
        return this;
    }

    public CodeUpdateData offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public CodeUpdateData limit(Integer limit) {
        this.limit = limit;
        return this;
    }


    public <T> CodeUpdateData updates(Field<T> field, T value) {
        return updates(field, value, UpdateAlgorithm.EQUAL);
    }
    public <T extends Number> CodeUpdateData updatesAddition(Field<T> field, T value) {
        return updates(field, value, UpdateAlgorithm.ADDITION);
    }
    public <T extends Number> CodeUpdateData updatesSubtraction(Field<T> field, T value) {
        return updates(field, value, UpdateAlgorithm.SUBTRACTION);
    }
    public <T> CodeUpdateData updates(Field<T> field, T value, UpdateAlgorithm algorithm) {
        updates.put(field, new Change<>(field, value, algorithm));
        return this;
    }


    public CodeUpdateData lowPriority(boolean lowPriority) {
        this.lowPriority = lowPriority;
        return this;
    }

    public boolean lowPriority() {
        return lowPriority;
    }



    public CodeOrder order() {
        return order;
    }

    public String name() {
        return name;
    }

    public CodeWhere where() {
        return where;
    }

    public Integer offset() {
        return offset;
    }

    public Integer limit() {
        return limit;
    }
}
