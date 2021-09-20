package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQL;
import xuan.cat.syncstaticmapview.database.api.sql.builder.Field;
import xuan.cat.syncstaticmapview.database.api.sql.builder.FieldStyle;
import xuan.cat.syncstaticmapview.database.api.sql.builder.Value;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

public class CodeFieldStyle<T> implements FieldStyle<T> {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final FieldStyle<Integer>     INT                         = new CodeFieldStyle<>(Integer      .class, " int", (value) -> '\'' + CodeFunction.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getInt(field.name()));
    public static final FieldStyle<Long>        INT_UNSIGNED                = new CodeFieldStyle<>(Long         .class, " int", (value) -> '\'' + CodeFunction.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()), true,  false);
    public static final FieldStyle<Long>        BIGINT                      = new CodeFieldStyle<>(Long         .class, " bigint", (value) -> '\'' + CodeFunction.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()));
    private static String strDate(Date value) {
        if      (value instanceof Value.NOW_DATE || value instanceof Value.NOW_TIMESTAMP || value instanceof Value.NOW_TIME)
            return "now()";
        else if (value instanceof Value.NOW_DATE_ADD)
            return "DATE_ADD(now(),INTERVAL " + (((Value.NOW_DATE_ADD) value).add / 1000) + " SECOND)";
        else if (value instanceof Value.NOW_DATE_REMOVE)
            return "DATE_SUB(now(),INTERVAL " + (((Value.NOW_DATE_REMOVE) value).remove / 1000) + " SECOND)";
        else if (value instanceof Value.NOW_TIMESTAMP_ADD)
            return "DATE_ADD(now(),INTERVAL " + (((Value.NOW_TIMESTAMP_ADD) value).add / 1000) + " SECOND)";
        else if (value instanceof Value.NOW_TIMESTAMP_REMOVE)
            return "DATE_SUB(now(),INTERVAL " + (((Value.NOW_TIMESTAMP_REMOVE) value).remove / 1000) + " SECOND)";
        else if (value instanceof Value.NOW_TIME_ADD)
            return "DATE_ADD(now(),INTERVAL " + (((Value.NOW_TIME_ADD) value).add / 1000) + " SECOND)";
        else if (value instanceof Value.NOW_TIME_REMOVE)
            return "DATE_SUB(now(),INTERVAL " + (((Value.NOW_TIME_REMOVE) value).remove / 1000) + " SECOND)";
        else
            return '\'' + dateFormat.format(value.getTime()) + '\'';
    }
    public static final FieldStyle<String>      VARCHAR                     = new CodeFieldStyle<>(String       .class, " varchar", (value) -> '\'' + CodeFunction.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    public static final FieldStyle<String>      TINYTEXT                    = new CodeFieldStyle<>(String       .class, " tinytext", (value) -> '\'' + CodeFunction.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    public static final FieldStyle<String>      TEXT                        = new CodeFieldStyle<>(String       .class, " text", (value) -> '\'' + CodeFunction.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    public static final FieldStyle<byte[]>      BLOB                        = new CodeFieldStyle<>(byte[]       .class, " blob", (value) -> new CodeVariable.UNHEX(value).part().toString(), (sql, field) -> sql.getBytes(field.name()));


    private final Class<T>                      classType;
    private final String                        name;
    private final boolean                       unsigned;
    private final boolean                       zerofill;
    private final Function<T, String>           write;
    private final CodeFieldStyleFunction<T>     read;
    private final Consumer<Field<T>>            apply;
    public interface CodeFieldStyleFunction<T> {
        T get(SQL sql, Field<T> field) throws SQLException;
    }

    /**
     * 設定此枚舉的SQL字符串片段
     * @param name SQL可拼湊字符串片段
     */
    CodeFieldStyle(Class<T> classType, String name, Function<T, String> write, CodeFieldStyleFunction<T> read) {
        this(classType, name, write, read, false, false);
    }
    CodeFieldStyle(Class<T> classType, String name, Function<T, String> write, CodeFieldStyleFunction<T> read, Consumer<Field<T>> apply) {
        this(classType, name, write, read, apply, false, false);
    }
    CodeFieldStyle(Class<T> classType, String name, Function<T, String> write, CodeFieldStyleFunction<T> read, boolean unsigned, boolean zerofill) {
        this(classType, name, write, read, null, unsigned, zerofill);
    }
    CodeFieldStyle(Class<T> classType, String name, Function<T, String> write, CodeFieldStyleFunction<T> read, Consumer<Field<T>> apply, boolean unsigned, boolean zerofill) {
        this.classType  = classType;
        this.name       = name;
        this.write      = write;
        this.read       = read;
        this.apply      = apply;
        this.unsigned   = unsigned;
        this.zerofill   = zerofill;
    }

    public T get(SQL sql, Field<T> field) throws SQLException {
        return read.get(sql, field);
    }

    public String str(T value) {
        return write.apply(value);
    }

    public boolean isUnsigned() {
        return unsigned;
    }

    public boolean isZerofill() {
        return zerofill;
    }

    /**
     * 取得此枚舉的SQL字符串片段
     * @return SQL可拼湊字符串片段
     */
    public String part() {
        return name;
    }

    public void apply(Field<T> field) {
        if (apply != null)
            apply.accept(field);
    }
}
