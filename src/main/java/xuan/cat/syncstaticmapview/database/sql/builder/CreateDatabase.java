package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQLCommand;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

/**
 * 創建數據庫
 */
public final class CreateDatabase implements SQLCommand {
    private final String    name;                       // 名稱
    private       Collate   collate     = Collate.NOT;  // 編碼類型


    public CreateDatabase(String name) {
        this.name       = name;
    }
    private CreateDatabase(CreateDatabase createDatabase) {
        this.name       = SQLTool.tryClone(createDatabase.name);
        this.collate    = SQLTool.tryClone(createDatabase.collate);
    }


    public String toCommand() {
        return new StringBuilder().append("CREATE DATABASE ").append(SQLTool.toField(name)).append(collate.part()).toString();
    }

    public CreateDatabase clone() {
        return new CreateDatabase(this);
    }



    public CreateDatabase collate(Collate collate) {
        this.collate = collate;
        return this;
    }


    public String name() {
        return name;
    }

    public Collate collate() {
        return collate;
    }
}
