package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.sql.SQLCommand;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.util.function.Consumer;

/**
 * 刪除資料
 */
public final class DeleteData implements SQLCommand {
    private final String    name;           // 名稱
    private       Where     where   = null; // 判斷式
    private       Integer   limit   = null; // 限制數量
    private       Integer   offset  = null; // 偏移數量
    private       Order     order   = null; // 排序


    public DeleteData(DatabaseTable table) {
        this.name       = table.getName();
    }
    private DeleteData(DeleteData deleteData) {
        this.name       = SQLTool.tryClone(deleteData.name);
        this.where      = SQLTool.tryClone(deleteData.where);
        this.limit      = SQLTool.tryClone(deleteData.limit);
        this.offset     = SQLTool.tryClone(deleteData.offset);
        this.order      = SQLTool.tryClone(deleteData.order);
    }


    public String toCommand() {
        StringBuilder builder = new StringBuilder();

        builder.append("DELETE FROM ");
        builder.append(SQLTool.toField(name));
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

    public DeleteData clone() {
        return new DeleteData(this);
    }



    public DeleteData limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public DeleteData offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public DeleteData order(Order order) {
        this.order = (Order) order;
        return this;
    }
    public DeleteData order(Consumer<Order> consumer) {
        if (this.order == null)
            this.order = new Order();
        consumer.accept(this.order);
        return this;
    }

    public DeleteData where(Where where) {
        this.where = (Where) where;
        return this;
    }
    public DeleteData where(Consumer<Where> consumer) {
        if (this.where == null)
            this.where = new Where();
        consumer.accept(this.where);
        return this;
    }
    public DeleteData brackets(Consumer<WhereBrackets> consumer) {
        if (this.where == null || !(this.where instanceof WhereBrackets)) {
            WhereBrackets brackets = new WhereBrackets();
            consumer.accept(brackets);
            this.where = brackets;
        } else {
            consumer.accept((WhereBrackets) this.where);
        }
        return this;
    }
    public DeleteData brackets(WhereBrackets brackets) {
        return where(brackets);
    }



    public Where where() {
        return where;
    }

    public String name() {
        return name;
    }

    public Order order() {
        return order;
    }

    public Integer limit() {
        return limit;
    }

    public Integer offset() {
        return offset;
    }
}
