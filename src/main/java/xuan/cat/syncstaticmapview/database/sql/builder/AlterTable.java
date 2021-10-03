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
    private final Map<Field, AlterType>         tableList;                  // 欄位


    public AlterTable(DatabaseTable table) {
        this.tableList      = new LinkedHashMap<>();
        this.name           = table.getName();
    }
    private AlterTable(AlterTable alterTable) {
        this.name           = SQLTool.tryClone(alterTable.name);
        this.tableList      = SQLTool.tryClone(alterTable.tableList);
    }


    public String toCommand() {
        StringBuilder builder = new StringBuilder();

        builder.append("ALTER TABLE ");
        builder.append(SQLTool.toField(name));
        builder.append(' ');

        Map<SQLPart, AlterType> parts = new LinkedHashMap<>(tableList.size());
        parts.putAll(tableList);
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
                } else {
                    merge.append(part.part());
                }
            }));
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
}
