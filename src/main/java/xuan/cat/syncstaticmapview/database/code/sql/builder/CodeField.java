package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQL;
import xuan.cat.syncstaticmapview.database.api.sql.builder.*;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLPart;

import java.sql.SQLException;

/**
 * 欄位表
 */
public final class CodeField<T> implements CodeSQLPart, Field<T> {

    private final FieldStyle<T> style;                              // 樣式
    private final String        name;                               // 名稱
    private       String        rename          = null;             // 更改名稱, 僅在 AlterTable 時生效
    private       Integer       length          = null;             // 長度
    private       Collate       collate         = Collate.NOT;      // 選項
    private       boolean       canNull         = false;            // 可NULL
    private       String        comment         = null;             // 註解
    private       T             defaultValue    = null;             // 預設值
    private       boolean       autoIncrement   = false;            // 自動遞增
    private       boolean       isFirst         = false;            // 擺在第一欄
    private       String        atAfter         = null;             // 擺在其他欄位之後
    private       String[]      enumList        = null;


    public CodeField(FieldStyle<T> style, String name) {
        this.style          = style;
        this.name           = name;
        style.apply(this);
    }
    private CodeField(CodeField<T> table) {
        this.style          = CodeFunction.tryClone(table.style);
        this.name           = CodeFunction.tryClone(table.name);
        this.rename         = CodeFunction.tryClone(table.rename);
        this.length         = CodeFunction.tryClone(table.length);
        this.collate        = CodeFunction.tryClone(table.collate);
        this.canNull        = CodeFunction.tryClone(table.canNull);
        this.comment        = CodeFunction.tryClone(table.comment);
        this.defaultValue   = CodeFunction.tryClone(table.defaultValue);
        this.autoIncrement  = CodeFunction.tryClone(table.autoIncrement);
        this.isFirst        = CodeFunction.tryClone(table.isFirst);
        this.atAfter        = CodeFunction.tryClone(table.atAfter);
        this.enumList       = CodeFunction.tryClone(table.enumList);
    }


    public StringBuilder part() {
        // `a` tinyint(2) unsigned NULL DEFAULT '4' COMMENT 'test' FIRST,
        StringBuilder builder = new StringBuilder();

        builder.append(CodeFunction.toField(rename()));

        builder.append(style.part());
        if (enumList != null) {
            builder.append(CodeFunction.brackets(CodeFunction.toStringFromArray(enumList, (merge, part) -> {
                merge.append(CodeFunction.toValue(FieldStyle.TINYTEXT, part));
            })));
        }
        if (length != null) {
            builder.append(CodeFunction.brackets(length.toString()));
        }

        if      (style.isUnsigned())
            builder.append(" unsigned");
        else if (style.isZerofill())
            builder.append(" zerofill");
        else if (collate != Collate.NOT)
            builder.append(collate.part());

        builder.append(canNull ? " NULL" : " NOT NULL");

        if (defaultValue != null) {
            builder.append(" DEFAULT ");
            builder.append(CodeFunction.toValue(style, defaultValue));
        }

        if (comment != null) {
            builder.append(" COMMENT ");
            builder.append(CodeFunction.toValue(FieldStyle.TINYTEXT, comment));
        }

        if (autoIncrement)
            builder.append(" AUTO_INCREMENT PRIMARY KEY");

        if (atAfter != null) {
            builder.append(" AFTER ");
            builder.append(CodeFunction.toField(atAfter));
        }

        if (isFirst)
            builder.append(" FIRST");

        return builder;
    }

    public CodeField<T> clone() {
        return new CodeField<>(this);
    }

    public void atAfter(String atAfter) {
        this.atAfter = atAfter;
        if (atAfter != null)
            isFirst(false);
    }

    public CodeField<T> autoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
        if (autoIncrement)
            defaultValue(null);
        return this;
    }

    public CodeField<T> collate(Collate collate) {
        if (collate == null)
            collate = Collate.NOT;
        this.collate = collate;
        return this;
    }

    public void defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        if (defaultValue != null)
            autoIncrement(false);
    }

    public void isFirst(boolean isFirst) {
        this.isFirst = isFirst;
        if (isFirst)
            atAfter((String) null);
    }

    public CodeField<T> length(Integer length) {
        this.length = length;
        return this;
    }

    public CodeField<T> enumList(String[] enumList) {
        this.enumList = enumList;
        return this;
    }



    public String rename() {
        return rename != null ? rename : name;
    }

    public Collate collate() {
        return collate;
    }

    public String name() {
        return name;
    }


    public FieldStyle<T> style() {
        return style;
    }


    public FieldIndex index() {
        return new CodeFieldIndex(this);
    }


    public TablePartition.Key<T> partitionKey() {
        return (CodeTablePartition.CodeKey<T>) new CodeTablePartition.CodeKey(this);
    }


    public T get(SQL sql) throws SQLException {
        return get(sql, null);
    }
    public T get(SQL sql, T def) throws SQLException {
        T vaLue = style.get(sql, this);
        if (vaLue == null)
            vaLue = def;
        return vaLue;
    }
    public T getThenClose(SQL sql) throws SQLException {
        return getThenClose(sql, null);
    }
    public T getThenClose(SQL sql, T def) throws SQLException {
        T vaLue = get(sql, def);
        sql.close();
        return vaLue;
    }
}
