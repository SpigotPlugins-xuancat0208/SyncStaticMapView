package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.sql.SQLCommand;
import xuan.cat.syncstaticmapview.database.sql.SQLPart;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 更改資料表
 */
public final class AlterTable implements SQLCommand {
    private final String                        name;                       // 名稱
    private       String                        rename          = null;     // 改名
    private       String                        comment         = null;     // 註解
    private       Collate                       collate         = null;     // 編碼類型
    private       DatabaseEngine                engine          = null;     // 引擎
    private final Map<Field, AlterType>         tableList;                  // 欄位
    private final Map<FieldIndex, AlterType>    indexList;                  // 索引
    private final Map<ForeignKey, AlterType>    foreignList;                // 外來鍵
    private       Long                          autoIncrement   = null;     // 自動遞增
    private       TablePartition                partition       = null;     // 分區


    public AlterTable(DatabaseTable table) {
        this.tableList      = new LinkedHashMap<>();
        this.indexList      = new LinkedHashMap<>();
        this.foreignList    = new LinkedHashMap<>();
        this.name           = table.getName();
    }
    private AlterTable(AlterTable alterTable) {
        this.name           = SQLTool.tryClone(alterTable.name);
        this.rename         = SQLTool.tryClone(alterTable.rename);
        this.comment        = SQLTool.tryClone(alterTable.comment);
        this.collate        = SQLTool.tryClone(alterTable.collate);
        this.engine         = SQLTool.tryClone(alterTable.engine);
        this.tableList      = SQLTool.tryClone(alterTable.tableList);
        this.indexList      = SQLTool.tryClone(alterTable.indexList);
        this.foreignList    = SQLTool.tryClone(alterTable.foreignList);
        this.autoIncrement  = SQLTool.tryClone(alterTable.autoIncrement);
        this.partition      = SQLTool.tryClone(alterTable.partition);
    }


    public String toCommand() {
        StringBuilder builder = new StringBuilder();

        builder.append("ALTER TABLE ");
        builder.append(SQLTool.toField(name));
        builder.append(' ');

        Map<SQLPart, AlterType> parts = new LinkedHashMap<>(tableList.size() + indexList.size() + foreignList.size());
        parts.putAll(tableList);
        parts.putAll(indexList);
        parts.putAll(foreignList);
        if (parts.size() > 0) {
            builder.append(' ');
            builder.append(SQLTool.toStringFromMap(parts, (merge, part, alterType) -> {
                if (part instanceof Field) {
                    Field table = (Field) part;
                    switch (alterType) {
                        case CHANGE:
                            merge.append("CHANGE ");
                            merge.append(SQLTool.toField(table.name()));
                            merge.append(' ');
                            merge.append(table.part());
                            break;
                        case ADD:
                        default:
                            merge.append("ADD ");
                            merge.append(table.part());
                            break;
                        case DROP:
                            merge.append("DROP ");
                            merge.append(SQLTool.toField(table.name()));
                            break;
                    }
                } else if (part instanceof FieldIndex) {
                    FieldIndex index = (FieldIndex) part;
                    switch (alterType) {
                        case ADD:
                        default:
                            merge.append("ADD ");
                            merge.append(index.part());
                            break;
                        case DROP:
                            merge.append("DROP INDEX ");
                            merge.append(SQLTool.toField(index.name()));
                            break;
                    }
                } else if (part instanceof ForeignKey) {
                    ForeignKey foreignKey = (ForeignKey) part;
                    switch (alterType) {
                        case ADD:
                        default:
                            merge.append("ADD ");
                            merge.append(foreignKey.part());
                            break;
                        case DROP:
                            merge.append("DROP FOREIGN KEY ");
                            merge.append(SQLTool.toField(foreignKey.name()));
                            break;
                    }
                } else {
                    merge.append(part.part());
                }
            }));
            if (rename != null || comment != null || engine != null || collate != null || autoIncrement != null)
                builder.append(',');
        }

        if (rename != null) {
            builder.append(" RENAME TO ");
            builder.append(SQLTool.toField(rename));
            if (comment != null || engine != null || collate != null || autoIncrement != null)
                builder.append(',');
        }

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
            builder.append(partition.part());
        }

        return builder.toString();
    }

    public AlterTable clone() {
        return new AlterTable(this);
    }


    public <T> AlterTable tableAdd(Field<T> field) {
        return tables(field, AlterType.ADD);
    }


    private <T> AlterTable tables(Field<T> field, AlterType alterType) {
        this.tableList.put(field, alterType);
        return this;
    }

    public String name() {
        return name;
    }
}
