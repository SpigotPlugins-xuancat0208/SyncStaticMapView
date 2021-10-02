package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQL;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class FieldStyle<T> {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 數字
    /** 整數  範圍 -128~127 或 0~255 */
    public static final FieldStyle<Byte>        TINYINT                     = new FieldStyle<>(Byte         .class, " tinyint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getByte(field.name()));
    public static final FieldStyle<Short>       TINYINT_UNSIGNED            = new FieldStyle<>(Short        .class, " tinyint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getShort(field.name()), true,  false);
    public static final FieldStyle<Byte>        TINYINT_ZEROFILL            = new FieldStyle<>(Byte         .class, " tinyint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getByte(field.name()), false, true);
    public static final FieldStyle<Short>       TINYINT_UNSIGNED_ZEROFILL   = new FieldStyle<>(Short        .class, " tinyint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getShort(field.name()), true,  true);


    /** 整數  範圍 -32,768~32,767 或 0~65535 */
    public static final FieldStyle<Short>       SMALLINT                    = new FieldStyle<>(Short        .class, " smallint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getShort(field.name()));
    public static final FieldStyle<Integer>     SMALLINT_UNSIGNED           = new FieldStyle<>(Integer      .class, " smallint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getInt(field.name()), true,  false);
    public static final FieldStyle<Short>       SMALLINT_ZEROFILL           = new FieldStyle<>(Short        .class, " smallint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getShort(field.name()), false, true);
    public static final FieldStyle<Integer>     SMALLINT_UNSIGNED_ZEROFILL  = new FieldStyle<>(Integer      .class, " smallint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getInt(field.name()), true,  true);


    /** 整數  範圍 -8,388,608~8,388,607 */
    public static final FieldStyle<Integer>     MEDIUMINT                   = new FieldStyle<>(Integer      .class, " mediumint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getInt(field.name()));
    public static final FieldStyle<Integer>     MEDIUMINT_UNSIGNED          = new FieldStyle<>(Integer      .class, " mediumint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getInt(field.name()), true,  false);
    public static final FieldStyle<Integer>     MEDIUMINT_ZEROFILL          = new FieldStyle<>(Integer      .class, " mediumint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getInt(field.name()), false, true);
    public static final FieldStyle<Integer>     MEDIUMINT_UNSIGNED_ZEROFILL = new FieldStyle<>(Integer      .class, " mediumint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getInt(field.name()), true,  true);


    /** 整數  範圍 -2,147,483,648~2,147,483,647 */
    public static final FieldStyle<Integer>     INT                         = new FieldStyle<>(Integer      .class, " int", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getInt(field.name()));
    public static final FieldStyle<Long>        INT_UNSIGNED                = new FieldStyle<>(Long         .class, " int", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()), true,  false);
    public static final FieldStyle<Integer>     INT_ZEROFILL                = new FieldStyle<>(Integer      .class, " int", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getInt(field.name()), false, true);
    public static final FieldStyle<Long>        INT_UNSIGNED_ZEROFILL       = new FieldStyle<>(Long         .class, " int", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()), true,  true);


    /** 整數  範圍 -9,223,372,036,854,775,808~9,223,372,036,854,775,807 */
    public static final FieldStyle<Long>        BIGINT                      = new FieldStyle<>(Long         .class, " bigint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()));
    public static final FieldStyle<Long>        BIGINT_UNSIGNED             = new FieldStyle<>(Long         .class, " bigint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()), true,  false);
    public static final FieldStyle<Long>        BIGINT_ZEROFILL             = new FieldStyle<>(Long         .class, " bigint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()), false, true);
    public static final FieldStyle<Long>        BIGINT_UNSIGNED_ZEROFILL    = new FieldStyle<>(Long         .class, " bigint", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()), true,  true);


    /** 整數  範圍 +/-79,228,162,514,264,337,593,543,950,335 或浮點數  範圍 +/-7.9228162514264337593543950335~+/-0.0000000000000000000000000001 */
    public static final FieldStyle<Long>        DECIMAL                     = new FieldStyle<>(Long         .class, " decimal", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()));
    public static final FieldStyle<Long>        DECIMAL_UNSIGNED            = new FieldStyle<>(Long         .class, " decimal", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()), true,  false);
    public static final FieldStyle<Long>        DECIMAL_ZEROFILL            = new FieldStyle<>(Long         .class, " decimal", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()), false, true);
    public static final FieldStyle<Long>        DECIMAL_UNSIGNED_ZEROFILL   = new FieldStyle<>(Long         .class, " decimal", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getLong(field.name()), true,  true);


    /** 浮點數  範圍 -127.0~128.0 */
    public static final FieldStyle<Float>       FLOAT                       = new FieldStyle<>(Float        .class, " float", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getFloat(field.name()));
    /** 浮點數  範圍 -1023.0~1024.0 */
    public static final FieldStyle<Double>      DOUBLE                      = new FieldStyle<>(Double       .class, " double", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getDouble(field.name()));




    // 時間
    /** 日期  格式 YYYY-MM-DD  0000-01-01 ~ 9999-12-31 */
    public static final FieldStyle<Date>        DATE                        = new FieldStyle<>(Date         .class, " date", FieldStyle::strDate, (sql, field) -> sql.getDate(field.name()));
    /** 時間日期  格式 YYYY-MM-DD HH:MM:SS  1000-01-01 00:00:00 ~ 9999-12-31 23:59:59 */
    public static final FieldStyle<Date>        DATETIME                    = new FieldStyle<>(Date         .class, " datetime", FieldStyle::strDate, (sql, field) -> sql.getTimestamp(field.name()));
    /** 時間日期  格式 YYYY-MM-DD HH:MM:SS  1970-01-01 00:00:00 ~ 2037-12-31 23:59:59 */
    public static final FieldStyle<Timestamp>   TIMESTAMP                   = new FieldStyle<>(Timestamp    .class, " timestamp", FieldStyle::strDate, (sql, field) -> sql.getTimestamp(field.name()));
    /** 時間  格式 HH:MM:SS  838:59:59 ~ 838:59:59 */
    public static final FieldStyle<Time>        TIME                        = new FieldStyle<>(Time         .class, " time", FieldStyle::strDate, (sql, field) -> sql.getTime(field.name()));
    /** 年  格式 YYYY  1901 ~ 2155 */
    public static final FieldStyle<Short>       YEAR                        = new FieldStyle<>(Short        .class, " year", (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'', (sql, field) -> sql.getShort(field.name()));
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




    // 字串
    /** 字串  最大長度 255 */
    public static final FieldStyle<String>      CHAR                        = new FieldStyle<>(String       .class, " char", (value) -> '\'' + SQLTool.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    /** 字串  最大長度 65,535 */
    public static final FieldStyle<String>      VARCHAR                     = new FieldStyle<>(String       .class, " varchar", (value) -> '\'' + SQLTool.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    /** 字串  最大長度 255 */
    public static final FieldStyle<String>      TINYTEXT                    = new FieldStyle<>(String       .class, " tinytext", (value) -> '\'' + SQLTool.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    /** 字串  最大長度 65,535(65KB) */
    public static final FieldStyle<String>      TEXT                        = new FieldStyle<>(String       .class, " text", (value) -> '\'' + SQLTool.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    /** 字串  最大長度 16,777,215(16MB) */
    public static final FieldStyle<String>      MEDIUMTEXT                  = new FieldStyle<>(String       .class, " mediumtext", (value) -> '\'' + SQLTool.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));
    /** 字串  最大長度 (4GB) */
    public static final FieldStyle<String>      LONGTEXT                    = new FieldStyle<>(String       .class, " longtext", (value) -> '\'' + SQLTool.safetyValue(value) + '\'', (sql, field) -> sql.getString(field.name()));




    // 二進位
    /** 二進位  字節長度 1 */
    public static final FieldStyle<Byte>        BIT                         = new FieldStyle<>(Byte       .class, " bit", Object::toString, (sql, field) -> sql.getByte(field.name()));
    /** 二進位  字節長度 無  類似 CHAR*/
    public static final FieldStyle<byte[]>      BINARY                      = new FieldStyle<>(byte[]       .class, " binary", (value) -> new Variable.UNHEX(value).part().toString(), (sql, field) -> sql.getBytes(field.name()));
    /** 二進位  字節長度 無  類似 VARCHAR*/
    public static final FieldStyle<byte[]>      VARBINARY                   = new FieldStyle<>(byte[]       .class, " varbinary", (value) -> new Variable.UNHEX(value).part().toString(), (sql, field) -> sql.getBytes(field.name()));
    /** 大小寫敏感  最大長度 255*/
    public static final FieldStyle<byte[]>      TINYBLOB                    = new FieldStyle<>(byte[]       .class, " tinyblob", (value) -> new Variable.UNHEX(value).part().toString(), (sql, field) -> sql.getBytes(field.name()));
    /** 大小寫敏感  最大長度 (64KB)*/
    public static final FieldStyle<byte[]>      BLOB                        = new FieldStyle<>(byte[]       .class, " blob", (value) -> new Variable.UNHEX(value).part().toString(), (sql, field) -> sql.getBytes(field.name()));
    /** 大小寫敏感  最大長度 (16MB)*/
    public static final FieldStyle<byte[]>      MEDIUMBLOB                  = new FieldStyle<>(byte[]       .class, " mediumblob", (value) -> new Variable.UNHEX(value).part().toString(), (sql, field) -> sql.getBytes(field.name()));
    /** 大小寫敏感  最大長度 (4GB)*/
    public static final FieldStyle<byte[]>      LONGBLOB                    = new FieldStyle<>(byte[]       .class, " longblob", (value) -> new Variable.UNHEX(value).part().toString(), (sql, field) -> sql.getBytes(field.name()));




    // 列表
    /** 枚舉  字節長度 1 ~ 2  最大數量 65535 */
    public static <E extends Enum<E>> FieldStyle<E>         ENUM(E[] enumList) {
        return new FieldStyleEnum<>(enumList);
    }
    public static final class FieldStyleEnum<E extends Enum<E>> extends FieldStyle<E> {
        FieldStyleEnum(E[] enumList) {
            super(
                    null,
                    " enum",
                    (value) -> '\'' + SQLTool.safetyValue(value.toString()) + '\'',
                    (sql, field) -> sql.getEnum(field.name(), enumList),
                    apply -> {
                        String[] strList = new String[enumList.length];
                        for (int index = 0 ; index < enumList.length ; index++)
                            strList[index] = enumList[index].toString();
                        ((Field) apply).enumList(strList);
                    }
            );
        }
    }

    /** 集合  字節長度 8  最大數量 64 */
    public static <E extends Enum<E>> FieldStyle<Set<E>>    SET(E[] enumList) {
        return new FieldStyleSet<>(enumList);
    }
    public static final class FieldStyleSet<E extends Enum<E>> extends FieldStyle<Set<E>> {
        FieldStyleSet(E[] enumList) {
            super(
                    null,
                    " set",
                    (value) -> '\'' + SQLTool.toStringFromList(value, (merge, part) -> {
                        merge.append(SQLTool.safetyValue(part.toString()));
                    }).toString() + '\'',
                    (sql, field) -> sql.getEnumSet(field.name(), enumList),
                    apply -> {
                        String[] strList = new String[enumList.length];
                        for (int index = 0 ; index < enumList.length ; index++)
                            strList[index] = enumList[index].toString();
                        ((Field) apply).enumList(strList);
                    }
            );
        }
    }



//    // 幾何
//    /** 幾何 */
//    public static final CodeFieldStyle<> GEOMETRY            = new CodeFieldStyle<>(, " geometry");
//    /** 幾何 */
//    public static final CodeFieldStyle<> POINT               = new CodeFieldStyle<>(, " point");
//    /** 幾何 */
//    public static final CodeFieldStyle<> LINESTRING          = new CodeFieldStyle<>(, " linestring");
//    /** 幾何 */
//    public static final CodeFieldStyle<> POLYGON             = new CodeFieldStyle<>(, " polygon");
//    /** 幾何 */
//    public static final CodeFieldStyle<> MULTIPOINT          = new CodeFieldStyle<>(, " multipoint");
//    /** 幾何 */
//    public static final CodeFieldStyle<> MULTILINESTRING     = new CodeFieldStyle<>(, " multilinestring");
//    /** 幾何 */
//    public static final CodeFieldStyle<> MULTIPOLYGON        = new CodeFieldStyle<>(, " multipolygon");
//    /** 幾何 */
//    public static final CodeFieldStyle<> GEOMETRYCOLLECTION  = new CodeFieldStyle<>(, " geometrycollection");



    // 其他
    /** 原始 */
    public static FieldStyle<String>                        ORIGINAL(String str) {
        return new FieldStyle<>(String.class, null, (value) -> str, (sql, field) -> "");
    }
    public static final FieldStyle<Boolean>     BOOLEAN                     = new FieldStyle<>(
            Boolean.class,
            " enum",
            (value) -> '\'' + value.toString() + '\'',
            (sql, field) -> sql.getBoolean(field.name()),
            apply -> ((Field) apply).enumList(new String[] {"false", "true"}).collate(Collate.ascii_bin)
    );
    public static final FieldStyle<UUID>        UUID                        = new FieldStyle<>(
            UUID.class,
            " binary",
            (value) -> new Variable.UNHEX(SQLTool.zipUUID(value).toString()).part().toString(),
            (sql, field) -> sql.getUUID(field.name()),
            apply -> apply.length(16)
    );





    private final Class<T>              classType;
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
    FieldStyle(Class<T> classType, String name, Write<T> write, Read<T> read) {
        this(classType, name, write, read, false, false);
    }
    FieldStyle(Class<T> classType, String name, Write<T> write, Read<T> read, Consumer<Field<T>> apply) {
        this(classType, name, write, read, apply, false, false);
    }
    FieldStyle(Class<T> classType, String name, Write<T> write, Read<T> read, boolean unsigned, boolean zerofill) {
        this(classType, name, write, read, null, unsigned, zerofill);
    }
    FieldStyle(Class<T> classType, String name, Write<T> write, Read<T> read, Consumer<Field<T>> apply, boolean unsigned, boolean zerofill) {
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

    public Class<T> getClassType() {
        return classType;
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
