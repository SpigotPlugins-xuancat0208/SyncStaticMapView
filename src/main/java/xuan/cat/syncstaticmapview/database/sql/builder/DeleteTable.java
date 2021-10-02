package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.sql.SQLCommand;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

/**
 * 刪除資料表
 */
public final class DeleteTable implements SQLCommand {
    private final String name; // 名稱


    public DeleteTable(DatabaseTable table) {
        this.name   = table.getName();
    }
    private DeleteTable(DeleteTable deleteTable) {
        this.name   = SQLTool.tryClone(deleteTable.name);
    }


    public String toCommand() {
        return "DROP TABLE " + SQLTool.toField(name);
    }

    public DeleteTable clone() {
        return new DeleteTable(this);
    }



    public String name() {
        return name;
    }
}
