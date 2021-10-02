package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQLPart;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 排序
 */
public final class Order implements SQLPart {
    private final Map<Field, Boolean> orders;


    public Order() {
        this.orders     = new LinkedHashMap<>();
    }
    private Order(Order order) {
        this.orders     = SQLTool.tryClone(order.orders);
    }


    public StringBuilder part() {
        StringBuilder builder = new StringBuilder();
        builder.append(" ORDER BY ");
        builder.append(SQLTool.toStringFromMap(orders, ((merge, field, isDesc) -> {
            merge.append(SQLTool.toField(field.name()));
            if (isDesc)
                merge.append(" DESC");
        })));
        return builder;
    }

    public Order clone() {
        return new Order(this);
    }


    public <T> Order increment(Field<T> field) {
        orders.put(field, false);
        return this;
    }


    public <T> Order decrement(Field<T> field) {
        orders.put(field, true);
        return this;
    }
}
