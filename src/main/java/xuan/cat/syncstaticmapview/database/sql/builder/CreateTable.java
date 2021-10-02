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
    private final Set<ForeignKey>   foreignList;                    // 外來鍵
    private       DatabaseEngine    engine;                         // 引擎
    private       Collate           collate         = Collate.NOT;  // 編碼類型
    private       String            comment         = null;         // 註解
    private       Long              autoIncrement   = null;         // 自動遞增
    private       TablePartition    partition       = null;         // 分區


    public CreateTable(DatabaseTable table) {
        this.tableList      = new LinkedHashSet<>();
        this.indexList      = new HashSet<>();
        this.foreignList    = new HashSet<>();
        this.name           = table.getName();
    }
    private CreateTable(CreateTable createTable) {
        this.name           = SQLTool.tryClone(createTable.name);
        this.tableList      = SQLTool.tryClone(createTable.tableList);
        this.indexList      = SQLTool.tryClone(createTable.indexList);
        this.foreignList    = SQLTool.tryClone(createTable.foreignList);
        this.engine         = SQLTool.tryClone(createTable.engine);
        this.collate        = SQLTool.tryClone(createTable.collate);
        this.comment        = SQLTool.tryClone(createTable.comment);
        this.autoIncrement  = SQLTool.tryClone(createTable.autoIncrement);
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
            table.atAfter((String) null);
        }));
        Set<SQLPart> parts = new LinkedHashSet<>(tableList.size() + indexList.size() + foreignList.size());
        parts.addAll(tableList);
        parts.addAll(indexList);
        parts.addAll(foreignList);
        builder.append(SQLTool.brackets(SQLTool.toStringFromList(parts, ((merge, part) -> {
            merge.append(part.part());
        }))));

        if (comment != null) {
            builder.append(" COMMENT=");
            builder.append(SQLTool.toValue(FieldStyle.TINYTEXT, comment));
        }

        if (engine != null) {
            builder.append(" ENGINE=");
            builder.append(engine.part());
        }

        if (collate != null) {
            builder.append(collate.part());
        }

        if (autoIncrement != null) {
            builder.append(" AUTO_INCREMENT=");
            builder.append(autoIncrement);
        }

        if (partition != null) {
            builder.append(' ');
            builder.append(((TablePartition) partition).part());
        }

        return builder.toString();
    }

    public CreateTable clone() {
        return new CreateTable(this);
    }


    public CreateTable comment(String comment) {
        this.comment = comment;
        return this;
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
        this.tableList.add((Field<T>) field);
        return this;
    }

    public CreateTable index(FieldIndex table) {
        this.indexList.add((FieldIndex) table);
        return this;
    }

    public CreateTable foreign(ForeignKey foreignKey) {
        this.foreignList.add((ForeignKey) foreignKey);
        return this;
    }

    public CreateTable autoIncrement(Integer autoIncrement) {
        this.autoIncrement = autoIncrement != null ? (long) autoIncrement : null;
        return this;
    }
    public CreateTable autoIncrement(Long autoIncrement) {
        this.autoIncrement = autoIncrement;
        return this;
    }

    public <T> CreateTable partition(TablePartition<T> partition) {
        this.partition = partition;
        return this;
    }

    public String name() {
        return name;
    }

    public String comment() {
        return comment;
    }

    public Collate collate() {
        return collate;
    }

    public DatabaseEngine engine() {
        return engine;
    }

    public Set<Field> field() {
        Set<Field> fields = new LinkedHashSet<>(this.tableList.size());
        fields.addAll(this.tableList);
        return fields;
    }
}
