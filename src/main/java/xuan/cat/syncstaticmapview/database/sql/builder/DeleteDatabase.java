package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQLCommand;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

/**
 * 刪除數據庫
 */
public final class DeleteDatabase implements SQLCommand {
    private final String name; // 名稱


    public DeleteDatabase(String name) {
        this.name   = name;
    }
    private DeleteDatabase(DeleteDatabase deleteDatabase) {
        this.name   = SQLTool.tryClone(deleteDatabase.name);
    }


    public String toCommand() {
        return "DROP DATABASE " + SQLTool.toField(name);
    }

    public DeleteDatabase clone() {
        return new DeleteDatabase(this);
    }


    public String name() {
        return name;
    }
}
