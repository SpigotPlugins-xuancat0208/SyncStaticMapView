package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.api.sql.builder.*;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLBuilder;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLPart;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *  創建資料表
 */
public final class CodeCreateTable implements CodeSQLBuilder, CreateTable {

    private final String                name;                               // 名稱
    private final Set<CodeField>        tableList;                          // 欄位
    private final Set<CodeFieldIndex>   indexList;                          // 索引
    private final Set<CodeForeignKey>   foreignList;                        // 外來鍵
    private       DatabaseEngine        engine;                             // 引擎
    private       Collate               collate             = Collate.NOT;
    private       String                comment             = null;         // 註解
    private       Long                  autoIncrement       = null;         // 資料表引擎
    private       TablePartition        partition           = null;         // 分區


    public CodeCreateTable(DatabaseTable table) {
        this.tableList      = new LinkedHashSet<>();
        this.indexList      = new HashSet<>();
        this.foreignList    = new HashSet<>();
        this.name           = table.getName();
    }
    private CodeCreateTable(CodeCreateTable createTable) {
        this.name           = CodeFunction.tryClone(createTable.name);
        this.tableList      = CodeFunction.tryClone(createTable.tableList);
        this.indexList      = CodeFunction.tryClone(createTable.indexList);
        this.foreignList    = CodeFunction.tryClone(createTable.foreignList);
        this.engine         = CodeFunction.tryClone(createTable.engine);
        this.collate        = CodeFunction.tryClone(createTable.collate);
        this.comment        = CodeFunction.tryClone(createTable.comment);
        this.autoIncrement  = CodeFunction.tryClone(createTable.autoIncrement);
        this.partition      = CodeFunction.tryClone(createTable.partition);
    }


    public String asString() {
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE ");
        builder.append(CodeFunction.toField(name));
        builder.append(' ');

        // 創建資料表的資料欄位, 不應該有特定的屬性
        tableList.forEach((table -> {
            table.isFirst(false);
            table.atAfter((String) null);
        }));
        Set<CodeSQLPart> parts = new LinkedHashSet<>(tableList.size() + indexList.size() + foreignList.size());
        parts.addAll(tableList);
        parts.addAll(indexList);
        parts.addAll(foreignList);
        builder.append(CodeFunction.brackets(CodeFunction.toStringFromList(parts, ((merge, part) -> {
            merge.append(part.part());
        }))));

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

    public CodeCreateTable clone() {
        return new CodeCreateTable(this);
    }


    public CodeCreateTable collate(Collate collate) {
        this.collate = collate;
        return this;
    }

    public CodeCreateTable engine(DatabaseEngine engine) {
        this.engine = engine;
        return this;
    }

    public <T> CodeCreateTable field(Field<T> field) {
        this.tableList.add((CodeField<T>) field);
        return this;
    }

    public CodeCreateTable index(FieldIndex table) {
        this.indexList.add((CodeFieldIndex) table);
        return this;
    }

    public <T> CodeCreateTable partition(TablePartition<T> partition) {
        this.partition = partition;
        return this;
    }

    public String name() {
        return name;
    }

    public Collate collate() {
        return collate;
    }

    public Set<Field> field() {
        Set<Field> fields = new LinkedHashSet<>(this.tableList.size());
        fields.addAll(this.tableList);
        return fields;
    }
}
