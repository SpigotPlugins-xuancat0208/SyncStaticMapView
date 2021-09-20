package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQL;
import xuan.cat.syncstaticmapview.database.code.sql.builder.CodeFieldStyle;

import java.sql.SQLException;
import java.util.Date;

/**
 * 欄位資料型態
 */
public interface FieldStyle<T> {
    FieldStyle<Integer>     INT                         = CodeFieldStyle.INT;
    FieldStyle<Long>        INT_UNSIGNED                = CodeFieldStyle.INT_UNSIGNED;
    FieldStyle<Long>        BIGINT                      = CodeFieldStyle.BIGINT;
    FieldStyle<Date>        DATETIME                    = CodeFieldStyle.DATETIME;
    FieldStyle<String>      VARCHAR                     = CodeFieldStyle.VARCHAR;
    FieldStyle<String>      TINYTEXT                    = CodeFieldStyle.TINYTEXT;
    FieldStyle<String>      TEXT                        = CodeFieldStyle.TEXT;
    FieldStyle<byte[]>      BLOB                        = CodeFieldStyle.BLOB;

    boolean isUnsigned();

    boolean isZerofill();

    T get(SQL sql, Field<T> field) throws SQLException;

    String str(T value);

    String part();

    void apply(Field<T> field);
}
