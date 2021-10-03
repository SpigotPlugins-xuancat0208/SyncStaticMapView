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
    private       Integer       length          = null;             // 長度
    private       Collate       collate         = Collate.NOT;      // 選項
    private       T             defaultValue    = null;             // 預設值
    private       boolean       autoIncrement   = false;            // 自動遞增
    private       boolean       isFirst         = false;            // 擺在第一欄


    public Field(FieldStyle<T> style, String name) {
        this.style          = style;
        this.name           = name;
        style.apply(this);
    }
    private Field(Field<T> table) {
        this.style          = SQLTool.tryClone(table.style);
        this.name           = SQLTool.tryClone(table.name);
        this.length         = SQLTool.tryClone(table.length);
        this.collate        = SQLTool.tryClone(table.collate);
        this.defaultValue   = SQLTool.tryClone(table.defaultValue);
        this.autoIncrement  = SQLTool.tryClone(table.autoIncrement);
        this.isFirst        = SQLTool.tryClone(table.isFirst);
    }


    public StringBuilder part() {
        // `a` tinyint(2) unsigned NULL DEFAULT '4' COMMENT 'test' FIRST,
        StringBuilder builder = new StringBuilder();

        builder.append(SQLTool.toField(name));

        builder.append(style.part());
        if (length != null) {
            builder.append(SQLTool.brackets(length.toString()));
        }

        if      (style.isUnsigned())
            builder.append(" unsigned");
        else if (style.isZerofill())
            builder.append(" zerofill");
        else if (collate != Collate.NOT)
            builder.append(collate.part());

        builder.append(" NOT NULL");

        if (defaultValue != null) {
            builder.append(" DEFAULT ");
            builder.append(SQLTool.toValue(style, defaultValue));
        }

        if (autoIncrement)
            builder.append(" AUTO_INCREMENT PRIMARY KEY");

        if (isFirst)
            builder.append(" FIRST");

        return builder;
    }

    public Field<T> clone() {
        return new Field<>(this);
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

    public Field<T> defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        if (defaultValue != null)
            autoIncrement(false);
        return this;
    }

    public Field<T> isFirst(boolean isFirst) {
        this.isFirst = isFirst;
        return this;
    }

    public Field<T> length(Integer length) {
        this.length = length;
        return this;
    }



    public String name() {
        return name;
    }

    public FieldStyle<T> style() {
        return style;
    }

    public FieldIndex index() {
        return new FieldIndex(this);
    }


    public TablePartition.Key<T> partitionKey() {
        return (TablePartition.Key<T>) new TablePartition.Key(this);
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
