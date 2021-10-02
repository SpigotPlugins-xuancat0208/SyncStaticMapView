package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQL;
import xuan.cat.syncstaticmapview.database.sql.SQLPart;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.sql.SQLException;

/**
 * 欄位表
 */
public final class Field<T> implements SQLPart {
    private final FieldStyle<T> style;                              // 樣式
    private final String        name;                               // 名稱
    private       String        rename          = null;             // 更改名稱, 僅在 AlterTable 時生效
    private       Integer       length          = null;             // 長度
    private       Collate       collate         = Collate.NOT;      // 選項
    private       boolean       canNull         = false;            // 可 NULL
    private       String        comment         = null;             // 註解
    private       T             defaultValue    = null;             // 預設值
    private       boolean       autoIncrement   = false;            // 自動遞增
    private       boolean       isFirst         = false;            // 擺在第一欄
    private       String        atAfter         = null;             // 擺在其他欄位之後
    private       String[]      enumList        = null;             // 枚舉清單


    public Field(FieldStyle<T> style, String name) {
        this.style          = style;
        this.name           = name;
        style.apply(this);
    }
    private Field(Field<T> table) {
        this.style          = SQLTool.tryClone(table.style);
        this.name           = SQLTool.tryClone(table.name);
        this.rename         = SQLTool.tryClone(table.rename);
        this.length         = SQLTool.tryClone(table.length);
        this.collate        = SQLTool.tryClone(table.collate);
        this.canNull        = SQLTool.tryClone(table.canNull);
        this.comment        = SQLTool.tryClone(table.comment);
        this.defaultValue   = SQLTool.tryClone(table.defaultValue);
        this.autoIncrement  = SQLTool.tryClone(table.autoIncrement);
        this.isFirst        = SQLTool.tryClone(table.isFirst);
        this.atAfter        = SQLTool.tryClone(table.atAfter);
        this.enumList       = SQLTool.tryClone(table.enumList);
    }


    public StringBuilder part() {
        // `a` tinyint(2) unsigned NULL DEFAULT '4' COMMENT 'test' FIRST,
        StringBuilder builder = new StringBuilder();

        builder.append(SQLTool.toField(rename()));

        builder.append(style.part());
        if (enumList != null) {
            builder.append(SQLTool.brackets(SQLTool.toStringFromArray(enumList, (merge, part) -> {
                merge.append(SQLTool.toValue(FieldStyle.TINYTEXT, part));
            })));
        }
        if (length != null) {
            builder.append(SQLTool.brackets(length.toString()));
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
            builder.append(SQLTool.toValue(style, defaultValue));
        }

        if (comment != null) {
            builder.append(" COMMENT ");
            builder.append(SQLTool.toValue(FieldStyle.TINYTEXT, comment));
        }

        if (autoIncrement)
            builder.append(" AUTO_INCREMENT PRIMARY KEY");

        if (atAfter != null) {
            builder.append(" AFTER ");
            builder.append(SQLTool.toField(atAfter));
        }

        if (isFirst)
            builder.append(" FIRST");

        return builder;
    }

    public Field<T> clone() {
        return new Field<>(this);
    }

    /**
     * @param rename 更改名稱, 僅在 AlterTable 的 CHANGE 時生效
     */
    public Field<T> rename(Enum<?> rename) {
        return rename(rename.name());
    }
    public Field<T> rename(String rename) {
        this.rename = rename;
        return this;
    }

    public Field<T> atAfter(Enum<?> atAfter) {
        return atAfter(atAfter.name());
    }
    public Field<T> atAfter(String atAfter) {
        this.atAfter = atAfter;
        if (atAfter != null)
            isFirst(false);
        return this;
    }

    public Field<T> canNull(boolean canNull) {
        this.canNull = canNull;
        return this;
    }

    public Field<T> autoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
        if (autoIncrement)
            defaultValue(null);
        return this;
    }

    public Field<T> collate(Collate collate) {
        if (collate == null)
            collate = Collate.NOT;
        this.collate = collate;
        return this;
    }

    public Field<T> comment(String comment) {
        this.comment = comment;
        return this;
    }

    public Field<T> defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        if (defaultValue != null)
            autoIncrement(false);
        return this;
    }

    public Field<T> isFirst(boolean isFirst) {
        this.isFirst = isFirst;
        if (isFirst)
            atAfter((String) null);
        return this;
    }

    public Field<T> length(Integer length) {
        this.length = length;
        return this;
    }

    public Field<T> enumList(String[] enumList) {
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
        return new FieldIndex(this);
    }
    public ForeignKey foreign() {
        return new ForeignKey(this);
    }



    public TablePartition.Hash<T> partitionHash() {
        return (TablePartition.Hash<T>) new TablePartition.Hash(this);
    }
    public TablePartition.Key<T> partitionKey() {
        return (TablePartition.Key<T>) new TablePartition.Key(this);
    }
    public TablePartition.Range<T> partitionRange() {
        return (TablePartition.Range<T>) new TablePartition.Range(this);
    }
    public TablePartition.List<T> partitionList() {
        return (TablePartition.List<T>) new TablePartition.List(this);
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
