package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQL;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.function.Consumer;

public class FieldStyle<T> {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final FieldStyle<Integer>     INT                         = new FieldStyle<>(" int", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getInt(field.name()));
    public static final FieldStyle<Long>        INT_UNSIGNED                = new FieldStyle<>(" int", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()), true,  false);
    public static final FieldStyle<String>      VARCHAR                     = new FieldStyle<>(" varchar", (value) -> '\'' + SQLTool.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    public static final FieldStyle<Date>        DATETIME                    = new FieldStyle<>(" datetime", FieldStyle::strDate, (sql, field) -> sql.getTimestamp(field.name()));
    private static String strDate(Date value) {
        if      (value instanceof Value.NOW_DATE)
            return "now()";
        else if (value instanceof Value.NOW_TIME_ADD)
            return "DATE_ADD(now(),INTERVAL " + (((Value.NOW_TIME_ADD) value).add / 1000) + " SECOND)";
        else
            return '\'' + dateFormat.format(value.getTime()) + '\'';
    }
    public static final FieldStyle<String>      TINYTEXT                    = new FieldStyle<>(" tinytext", (value) -> '\'' + SQLTool.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    public static final FieldStyle<String>      TEXT                        = new FieldStyle<>(" text", (value) -> '\'' + SQLTool.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    public static final FieldStyle<byte[]>      BLOB                        = new FieldStyle<>(" blob", (value) -> new Variable.UNHEX(value).part().toString(), (sql, field) -> sql.getBytes(field.name()));
    public static final FieldStyle<UUID>        UUID                        = new FieldStyle<>(
            " binary",
            (value) -> new Variable.UNHEX(SQLTool.zipUUID(value).toString()).part().toString(),
            (sql, field) -> sql.getUUID(field.name()),
            apply -> apply.length(16)
    );


    private final String                name;
    private final boolean               unsigned;
    private final boolean               zerofill;
    private final Write<T>              write;
    private final Read<T>               read;
    private final Consumer<Field<T>>    apply;


    public interface Read<R> {
        R get(SQL sql, Field<R> field) throws SQLException;
    }
    public interface Write<W> {
        String apply(W value);
    }

    /**
     * 設定此枚舉的SQL字符串片段
     * @param name SQL可拼湊字符串片段
     */
    FieldStyle(String name, Write<T> write, Read<T> read) {
        this(name, write, read, false, false);
    }
    FieldStyle(String name, Write<T> write, Read<T> read, Consumer<Field<T>> apply) {
        this(name, write, read, apply, false, false);
    }
    FieldStyle(String name, Write<T> write, Read<T> read, boolean unsigned, boolean zerofill) {
        this(name, write, read, null, unsigned, zerofill);
    }
    FieldStyle(String name, Write<T> write, Read<T> read, Consumer<Field<T>> apply, boolean unsigned, boolean zerofill) {
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
