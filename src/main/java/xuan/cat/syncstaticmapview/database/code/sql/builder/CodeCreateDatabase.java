package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.builder.Collate;
import xuan.cat.syncstaticmapview.database.api.sql.builder.CreateDatabase;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLBuilder;

/**
 * 創建數據庫
 */
public final class CodeCreateDatabase implements CodeSQLBuilder, CreateDatabase {
    private final String    name;                       // 數據庫名稱
    private       Collate   collate     = Collate.NOT;  // 數據庫編碼類型


    public CodeCreateDatabase(String name) {
        this.name       = name;
    }
    private CodeCreateDatabase(CodeCreateDatabase createDatabase) {
        this.name       = CodeFunction.tryClone(createDatabase.name);
        this.collate    = CodeFunction.tryClone(createDatabase.collate);
    }


    public String asString() {
        return new StringBuilder().append("CREATE DATABASE ").append(CodeFunction.toField(name)).append(collate.part()).toString();
    }

    public CodeCreateDatabase clone() {
        return new CodeCreateDatabase(this);
    }



    public CodeCreateDatabase collate(Collate collate) {
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
