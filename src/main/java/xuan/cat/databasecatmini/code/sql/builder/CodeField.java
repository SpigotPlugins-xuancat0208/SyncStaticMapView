package xuan.cat.databasecatmini.code.sql.builder;

import xuan.cat.databasecatmini.api.sql.SQL;
import xuan.cat.databasecatmini.api.sql.builder.*;
import xuan.cat.databasecatmini.code.sql.CodeSQLPart;

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

    /**
     * @param rename 更改名稱, 僅在 AlterTable 的 CHANGE 時生效
     */
    public CodeField<T> rename(Enum<?> rename) {
        return rename(rename.name());
    }
    public CodeField<T> rename(String rename) {
        this.rename = rename;
        return this;
    }

    public CodeField<T> atAfter(Enum<?> atAfter) {
        return atAfter(atAfter.name());
    }
    public CodeField<T> atAfter(String atAfter) {
        this.atAfter = atAfter;
        if (atAfter != null)
            isFirst(false);
        return this;
    }

    public CodeField<T> canNull(boolean canNull) {
        this.canNull = canNull;
        return this;
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

    public CodeField<T> comment(String comment) {
        this.comment = comment;
        return this;
    }

    public CodeField<T> defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        if (defaultValue != null)
            autoIncrement(false);
        return this;
    }

    public CodeField<T> isFirst(boolean isFirst) {
        this.isFirst = isFirst;
        if (isFirst)
            atAfter((String) null);
        return this;
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

    public Integer length() {
        return length;
    }

    public T defaultValue() {
        return defaultValue;
    }

    public String atAfter() {
        return atAfter;
    }

    public String comment() {
        return comment;
    }

    public String name() {
        return name;
    }


    public FieldStyle<T> style() {
        return style;
    }

    public boolean autoIncrement() {
        return autoIncrement;
    }

    public boolean canNull() {
        return canNull;
    }

    public boolean isFirst() {
        return isFirst;
    }



    public FieldIndex index() {
        return new CodeFieldIndex(this);
    }
    public ForeignKey foreign() {
        return new CodeForeignKey(this);
    }



    public TablePartition.Hash<T> partitionHash() {
        return (CodeTablePartition.CodeHash<T>) new CodeTablePartition.CodeHash(this);
    }
    public TablePartition.Key<T> partitionKey() {
        return (CodeTablePartition.CodeKey<T>) new CodeTablePartition.CodeKey(this);
    }
    public TablePartition.Range<T> partitionRange() {
        return (CodeTablePartition.CodeRange<T>) new CodeTablePartition.CodeRange(this);
    }
    public TablePartition.List<T> partitionList() {
        return (CodeTablePartition.CodeList<T>) new CodeTablePartition.CodeList(this);
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
