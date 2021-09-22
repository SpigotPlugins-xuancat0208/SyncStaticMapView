package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.api.sql.builder.*;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLBuilder;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLPart;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 更改資料表
 */
public final class CodeAlterTable implements CodeSQLBuilder, AlterTable {
    private final String                            name;                           // 名稱
    private       String                            rename              = null;     // 改名
    private       String                            comment             = null;     // 註解
    private       Collate                           collate             = null;     // 資料表編碼類型
    private       DatabaseEngine                    engine              = null;     // 資料表引擎
    private final Map<CodeField, AlterType>         tableList;                      // 欄位
    private final Map<CodeFieldIndex, AlterType>    indexList;                      // 索引
    private final Map<CodeForeignKey, AlterType>    foreignList;                    // 外來鍵
    private       Long                              autoIncrement       = null;     // 資料表引擎
    private       TablePartition                    partition           = null;     // 分區


    public CodeAlterTable(DatabaseTable table) {
        this.tableList      = new LinkedHashMap<>();
        this.indexList      = new LinkedHashMap<>();
        this.foreignList    = new LinkedHashMap<>();
        this.name           = table.getName();
    }
    private CodeAlterTable(CodeAlterTable alterTable) {
        this.name           = CodeFunction.tryClone(alterTable.name);
        this.rename         = CodeFunction.tryClone(alterTable.rename);
        this.comment        = CodeFunction.tryClone(alterTable.comment);
        this.collate        = CodeFunction.tryClone(alterTable.collate);
        this.engine         = CodeFunction.tryClone(alterTable.engine);
        this.tableList      = CodeFunction.tryClone(alterTable.tableList);
        this.indexList      = CodeFunction.tryClone(alterTable.indexList);
        this.foreignList    = CodeFunction.tryClone(alterTable.foreignList);
        this.autoIncrement  = CodeFunction.tryClone(alterTable.autoIncrement);
        this.partition      = CodeFunction.tryClone(alterTable.partition);
    }


    public String asString() {
        StringBuilder builder = new StringBuilder();

        builder.append("ALTER TABLE ");
        builder.append(CodeFunction.toField(name));
        builder.append(' ');

        Map<CodeSQLPart, AlterType> parts = new LinkedHashMap<>(tableList.size() + indexList.size() + foreignList.size());
        parts.putAll(tableList);
        parts.putAll(indexList);
        parts.putAll(foreignList);
        if (parts.size() > 0) {
            builder.append(' ');
            builder.append(CodeFunction.toStringFromMap(parts, (merge, part, alterType) -> {
                if (part instanceof CodeField) {
                    CodeField table = (CodeField) part;
                    switch (alterType) {
                        case CHANGE:
                            merge.append("CHANGE ");
                            merge.append(CodeFunction.toField(table.name()));
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
                            merge.append(CodeFunction.toField(table.name()));
                            break;
                    }
                } else if (part instanceof CodeFieldIndex) {
                    CodeFieldIndex index = (CodeFieldIndex) part;
                    switch (alterType) {
                        case ADD:
                        default:
                            merge.append("ADD ");
                            merge.append(index.part());
                            break;
                        case DROP:
                            merge.append("DROP INDEX ");
                            merge.append(CodeFunction.toField(index.name()));
                            break;
                    }
                } else if (part instanceof CodeForeignKey) {
                    CodeForeignKey foreignKey = (CodeForeignKey) part;
                    switch (alterType) {
                        case ADD:
                        default:
                            merge.append("ADD ");
                            merge.append(foreignKey.part());
                            break;
                        case DROP:
                            merge.append("DROP FOREIGN KEY ");
                            merge.append(CodeFunction.toField(foreignKey.name()));
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
            builder.append(CodeFunction.toField(rename));
            if (comment != null || engine != null || collate != null || autoIncrement != null)
                builder.append(',');
        }

        if (comment != null) {
            builder.append(" COMMENT=");
            builder.append(CodeFunction.toValue(FieldStyle.TINYTEXT, comment));
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
            builder.append(((CodeTablePartition) partition).part());
        }

        return builder.toString();
    }

    public CodeAlterTable clone() {
        return new CodeAlterTable(this);
    }


    public CodeAlterTable rename(Enum<?> rename) {
        return rename(rename.name());
    }
    public CodeAlterTable rename(String rename) {
        this.rename = rename;
        return this;
    }

    public CodeAlterTable collate(Collate collate) {
        this.collate = collate;
        return this;
    }

    public CodeAlterTable comment(String comment) {
        this.comment = comment;
        return this;
    }

    public CodeAlterTable engine(DatabaseEngine engine) {
        this.engine = engine;
        return this;
    }


    public <T> CodeAlterTable tableAdd(Field<T> field) {
        return tables(field, AlterType.ADD);
    }
    public <T> CodeAlterTable tableChange(Field<T> field) {
        return tables(field, AlterType.CHANGE);
    }
    public <T> CodeAlterTable tableRemove(Field<T> field) {
        return tables(field, AlterType.DROP);
    }


    private <T> CodeAlterTable tables(Field<T> field, AlterType alterType) {
        this.tableList.put((CodeField<T>) field, alterType);
        return this;
    }


    public CodeAlterTable indexAdd(FieldIndex index) {
        return indexs(index, AlterType.ADD);
    }
    public CodeAlterTable indexRemove(FieldIndex index) {
        return indexs(index, AlterType.DROP);
    }


    private CodeAlterTable indexs(FieldIndex table, AlterType alterType) {
        this.indexList.put((CodeFieldIndex) table, alterType);
        return this;
    }

    public CodeAlterTable foreignAdd(ForeignKey foreignKey) {
        return foreigns(foreignKey, AlterType.ADD);
    }
    public CodeAlterTable foreignRemove(ForeignKey foreignKey) {
        return foreigns(foreignKey, AlterType.DROP);
    }


    private CodeAlterTable foreigns(ForeignKey foreignKey, AlterType alterType) {
        this.foreignList.put((CodeForeignKey) foreignKey, alterType);
        return this;
    }


    public CodeAlterTable autoIncrement(Integer autoIncrement) {
        this.autoIncrement = autoIncrement != null ? (long) autoIncrement : null;
        return this;
    }
    public CodeAlterTable autoIncrement(Long autoIncrement) {
        this.autoIncrement = autoIncrement;
        return this;
    }

    public <T> CodeAlterTable partition(TablePartition<T> partition) {
        this.partition = partition;
        return this;
    }

    public String name() {
        return name;
    }

    public DatabaseEngine engine() {
        return engine;
    }

    public Collate collate() {
        return collate;
    }

    public String comment() {
        return comment;
    }

    public String rename() {
        return rename;
    }
}
