package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.sql.SQLCommand;
import xuan.cat.syncstaticmapview.database.sql.SQLPart;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *  創建資料表
 */
public final class CreateTable implements SQLCommand {
    private final String            name;                           // 名稱
    private final Set<Field>        tableList;                      // 欄位
    private final Set<FieldIndex>   indexList;                      // 索引
    private       DatabaseEngine    engine;                         // 引擎
    private       Collate           collate         = Collate.NOT;  // 編碼類型
    private       TablePartition    partition       = null;         // 分區


    public CreateTable(DatabaseTable table) {
        this.tableList      = new LinkedHashSet<>();
        this.indexList      = new HashSet<>();
        this.name           = table.getName();
    }
    private CreateTable(CreateTable createTable) {
        this.name           = SQLTool.tryClone(createTable.name);
        this.tableList      = SQLTool.tryClone(createTable.tableList);
        this.indexList      = SQLTool.tryClone(createTable.indexList);
        this.engine         = SQLTool.tryClone(createTable.engine);
        this.collate        = SQLTool.tryClone(createTable.collate);
        this.partition      = SQLTool.tryClone(createTable.partition);
    }


    public String toCommand() {
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE ");
        builder.append(SQLTool.toField(name));
        builder.append(' ');

        // 創建資料表的資料欄位, 不應該有特定的屬性
        tableList.forEach((table -> {
            table.isFirst(false);
        }));
        Set<SQLPart> parts = new LinkedHashSet<>(tableList.size() + indexList.size());
        parts.addAll(tableList);
        parts.addAll(indexList);
        builder.append(SQLTool.brackets(SQLTool.toStringFromList(parts, ((merge, part) -> {
            merge.append(part.part());
        }))));

        if (engine != null) {
            builder.append(" ENGINE=");
            builder.append(engine.part());
        }

        if (collate != null) {
            builder.append(collate.part());
        }

        if (partition != null) {
            builder.append(' ');
            builder.append(partition.part());
        }

        return builder.toString();
    }

    public CreateTable clone() {
        return new CreateTable(this);
    }

    public CreateTable collate(Collate collate) {
        this.collate = collate;
        return this;
    }

    public CreateTable engine(DatabaseEngine engine) {
        this.engine = engine;
        return this;
    }

    public <T> CreateTable field(Field<T> field) {
        this.tableList.add(field);
        return this;
    }

    public CreateTable index(FieldIndex table) {
        this.indexList.add(table);
        return this;
    }

    public <T> CreateTable partition(TablePartition<T> partition) {
        this.partition = partition;
        return this;
    }
}
