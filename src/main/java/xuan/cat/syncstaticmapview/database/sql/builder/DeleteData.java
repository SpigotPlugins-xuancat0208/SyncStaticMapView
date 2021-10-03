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


    public DeleteData(DatabaseTable table) {
        this.name       = table.getName();
    }
    private DeleteData(DeleteData deleteData) {
        this.name       = SQLTool.tryClone(deleteData.name);
        this.where      = SQLTool.tryClone(deleteData.where);
        this.limit      = SQLTool.tryClone(deleteData.limit);
    }


    public String toCommand() {
        StringBuilder builder = new StringBuilder();

        builder.append("DELETE FROM ");
        builder.append(SQLTool.toField(name));
        builder.append(' ');

        if (where != null)
            builder.append(where.part());

        if (limit != null) {
            builder.append(" LIMIT ");
            builder.append(limit);
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

    public DeleteData where(Consumer<Where> consumer) {
        if (this.where == null)
            this.where = new Where();
        consumer.accept(this.where);
        return this;
    }
}
