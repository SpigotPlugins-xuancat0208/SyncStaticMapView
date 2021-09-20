package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQL;

import java.sql.SQLException;

public interface Field<T> {
    Field<T> clone();

    void atAfter(String atAfter);

    Field<T> autoIncrement(boolean autoIncrement);

    Field<T> collate(Collate collate);

    void
    defaultValue(T defaultValue);

    void isFirst(boolean isFirst);

    Field<T> length(Integer length);


    String rename();
    Collate collate();

    String name();
//    String[] valueList();
    FieldStyle<T> style();


    FieldIndex index();


    TablePartition.Key<T> partitionKey();


    T get(SQL sql) throws SQLException;
    T get(SQL sql, T def) throws SQLException;
    T getThenClose(SQL sql) throws SQLException;
    T getThenClose(SQL sql, T def) throws SQLException;
}
