package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQL;
import xuan.cat.syncstaticmapview.database.code.sql.builder.CodeFieldStyle;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * 欄位資料型態
 */
public interface FieldStyle<T> {
    // 數字
    /** 整數  範圍 -128~127 或 0~255 */
    FieldStyle<Byte>        TINYINT                     = CodeFieldStyle.TINYINT;
    FieldStyle<Short>       TINYINT_UNSIGNED            = CodeFieldStyle.TINYINT_UNSIGNED;
    FieldStyle<Byte>        TINYINT_ZEROFILL            = CodeFieldStyle.TINYINT_ZEROFILL;
    FieldStyle<Short>       TINYINT_UNSIGNED_ZEROFILL   = CodeFieldStyle.TINYINT_UNSIGNED_ZEROFILL;

    /** 整數  範圍 -32,768~32,767 或 0~65535 */
    FieldStyle<Short>       SMALLINT                    = CodeFieldStyle.SMALLINT;
    FieldStyle<Integer>     SMALLINT_UNSIGNED           = CodeFieldStyle.SMALLINT_UNSIGNED;
    FieldStyle<Short>       SMALLINT_ZEROFILL           = CodeFieldStyle.SMALLINT_ZEROFILL;
    FieldStyle<Integer>     SMALLINT_UNSIGNED_ZEROFILL  = CodeFieldStyle.SMALLINT_UNSIGNED_ZEROFILL;

    /** 整數  範圍 -8,388,608~8,388,607 */
    FieldStyle<Integer>     MEDIUMINT                   = CodeFieldStyle.MEDIUMINT;
    FieldStyle<Integer>     MEDIUMINT_UNSIGNED          = CodeFieldStyle.MEDIUMINT_UNSIGNED;
    FieldStyle<Integer>     MEDIUMINT_ZEROFILL          = CodeFieldStyle.MEDIUMINT_ZEROFILL;
    FieldStyle<Integer>     MEDIUMINT_UNSIGNED_ZEROFILL = CodeFieldStyle.MEDIUMINT_UNSIGNED_ZEROFILL;

    /** 整數  範圍 -2,147,483,648~2,147,483,647 */
    FieldStyle<Integer>     INT                         = CodeFieldStyle.INT;
    FieldStyle<Long>        INT_UNSIGNED                = CodeFieldStyle.INT_UNSIGNED;
    FieldStyle<Integer>     INT_ZEROFILL                = CodeFieldStyle.INT_ZEROFILL;
    FieldStyle<Long>        INT_UNSIGNED_ZEROFILL       = CodeFieldStyle.INT_UNSIGNED_ZEROFILL;

    /** 整數  範圍 -9,223,372,036,854,775,808~9,223,372,036,854,775,807 */
    FieldStyle<Long>        BIGINT                      = CodeFieldStyle.BIGINT;
    FieldStyle<Long>        BIGINT_UNSIGNED             = CodeFieldStyle.BIGINT_UNSIGNED;
    FieldStyle<Long>        BIGINT_ZEROFILL             = CodeFieldStyle.BIGINT_ZEROFILL;
    FieldStyle<Long>        BIGINT_UNSIGNED_ZEROFILL    = CodeFieldStyle.BIGINT_UNSIGNED_ZEROFILL;

    /** 整數  範圍 +/-79,228,162,514,264,337,593,543,950,335 或浮點數  範圍 +/-7.9228162514264337593543950335~+/-0.0000000000000000000000000001 */

    FieldStyle<Long>        DECIMAL                     = CodeFieldStyle.DECIMAL;
    FieldStyle<Long>        DECIMAL_UNSIGNED            = CodeFieldStyle.DECIMAL_UNSIGNED;
    FieldStyle<Long>        DECIMAL_ZEROFILL            = CodeFieldStyle.DECIMAL_ZEROFILL;
    FieldStyle<Long>        DECIMAL_UNSIGNED_ZEROFILL   = CodeFieldStyle.DECIMAL_UNSIGNED_ZEROFILL;

    /** 浮點數  範圍 -127.0~128.0 */
    FieldStyle<Float>       FLOAT                       = CodeFieldStyle.FLOAT;
    FieldStyle<Double>      DOUBLE                      = CodeFieldStyle.DOUBLE;

    // 時間
    /** 日期  格式 YYYY-MM-DD  0000-01-01 ~ 9999-12-31 */
    FieldStyle<Date>        DATE                        = CodeFieldStyle.DATE;
    FieldStyle<Date>        DATETIME                    = CodeFieldStyle.DATETIME;
    /** 時間日期  格式 YYYY-MM-DD HH:MM:SS  1970-01-01 00:00:00 ~ 2037-12-31 23:59:59 */
    FieldStyle<Timestamp>   TIMESTAMP                   = CodeFieldStyle.TIMESTAMP;
    /** 時間  格式 HH:MM:SS  838:59:59 ~ 838:59:59 */
    FieldStyle<Time>        TIME                        = CodeFieldStyle.TIME;
    FieldStyle<Short>       YEAR                        = CodeFieldStyle.YEAR;

    // 字串
    /** 字串  最大長度 255 */
    FieldStyle<String>      CHAR                        = CodeFieldStyle.CHAR;
    FieldStyle<String>      VARCHAR                     = CodeFieldStyle.VARCHAR;
    /** 字串  最大長度 255 */
    FieldStyle<String>      TINYTEXT                    = CodeFieldStyle.TINYTEXT;
    /** 字串  最大長度 65,535(65KB) */
    FieldStyle<String>      TEXT                        = CodeFieldStyle.TEXT;
    FieldStyle<String>      MEDIUMTEXT                  = CodeFieldStyle.MEDIUMTEXT;
    /** 字串  最大長度 (4GB) */
    FieldStyle<String>      LONGTEXT                    = CodeFieldStyle.LONGTEXT;

    // 二進位
    /** 二進位  字節長度 1 */
    FieldStyle<Byte>        BIT                         = CodeFieldStyle.BIT;
    FieldStyle<byte[]>      BINARY                      = CodeFieldStyle.BINARY;
    /** 二進位  字節長度 無  類似 VARCHAR */
    FieldStyle<byte[]>      VARBINARY                   = CodeFieldStyle.VARBINARY;
    /** 大小寫敏感  最大長度 255*/
    FieldStyle<byte[]>      TINYBLOB                    = CodeFieldStyle.TINYBLOB;
    /** 大小寫敏感  最大長度 (64KB) */
    FieldStyle<byte[]>      BLOB                        = CodeFieldStyle.BLOB;
    FieldStyle<byte[]>      MEDIUMBLOB                  = CodeFieldStyle.MEDIUMBLOB;
    /** 大小寫敏感  最大長度 (4GB) */
    FieldStyle<byte[]>      LONGBLOB                    = CodeFieldStyle.LONGBLOB;

    
    // 列表
    /** 枚舉  字節長度 1 ~ 2  最大數量 65535 */
    static <E extends Enum<E>> FieldStyle<E>        ENUM(E[] enumList) {
        return CodeFieldStyle.ENUM(enumList);
    }
    /** 集合  字節長度 8  最大數量 64 */
    static <E extends Enum<E>> FieldStyle<Set<E>>   SET(E[] enumList) {
        return CodeFieldStyle.SET(enumList);
    }


    /** 集合  字節長度 8  最大數量 64*/
    static  FieldStyle<String>                      ORIGINAL(String str) {
        return CodeFieldStyle.ORIGINAL(str);
    }
    FieldStyle<Boolean>     BOOLEAN                     = CodeFieldStyle.BOOLEAN;
    FieldStyle<UUID>        UUID                        = CodeFieldStyle.UUID;





    boolean isUnsigned();

    boolean isZerofill();

    T get(SQL sql, Field<T> field) throws SQLException;

    String str(T value);

    String part();

    void apply(Field<T> field);
}
