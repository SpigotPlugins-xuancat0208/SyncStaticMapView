package xuan.cat.databasecatmini.code.sql.builder;

import xuan.cat.databasecatmini.api.sql.builder.Field;
import xuan.cat.databasecatmini.api.sql.builder.Order;
import xuan.cat.databasecatmini.code.sql.CodeSQLPart;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 排序
 */
public final class CodeOrder implements CodeSQLPart, Order {

    private final Map<Field, Boolean> orders;


    public CodeOrder() {
        this.orders     = new LinkedHashMap<>();
    }
    private CodeOrder(CodeOrder order) {
        this.orders     = CodeFunction.tryClone(order.orders);
    }


    public StringBuilder part() {
        StringBuilder builder = new StringBuilder();
        builder.append(" ORDER BY ");
        builder.append(CodeFunction.toStringFromMap(orders, ((merge, field, isDesc) -> {
            merge.append(CodeFunction.toField(field.name()));
            if (isDesc)
                merge.append(" DESC");
        })));
        return builder;
    }

    public CodeOrder clone() {
        return new CodeOrder(this);
    }


    public <T> CodeOrder increment(Field<T> field) {
        orders.put(field, false);
        return this;
    }


    public <T> CodeOrder decrement(Field<T> field) {
        orders.put(field, true);
        return this;
    }
}
