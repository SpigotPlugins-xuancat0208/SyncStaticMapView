package xuan.cat.databasecatmini.api.sql.builder;

import xuan.cat.databasecatmini.api.sql.SQL;

import java.sql.SQLException;

public interface Field<T> {
    Field<T> clone();

    /**
     * @param rename 更改名稱, 僅在 AlterTable 的 CHANGE 時生效
     */
    Field<T> rename(Enum<?> rename);
    Field<T> rename(String rename);

    Field<T> atAfter(Enum<?> atAfter);
    Field<T> atAfter(String atAfter);

    Field<T> canNull(boolean canNull);

    Field<T> autoIncrement(boolean autoIncrement);

    Field<T> collate(Collate collate);

    Field<T> comment(String comment);

    Field<T> defaultValue(T defaultValue);

    Field<T> isFirst(boolean isFirst);

    Field<T> length(Integer length);


    String rename();
    Collate collate();
    Integer length();
    T defaultValue();
    String atAfter();
    String comment();
    String name();
//    String[] valueList();
    FieldStyle<T> style();
    boolean autoIncrement();
    boolean canNull();
    boolean isFirst();


    FieldIndex index();
    ForeignKey foreign();


    TablePartition.Hash<T> partitionHash();
    TablePartition.Key<T> partitionKey();
    TablePartition.Range<T> partitionRange();
    TablePartition.List<T> partitionList();


    T get(SQL sql) throws SQLException;
    T get(SQL sql, T def) throws SQLException;
    T getThenClose(SQL sql) throws SQLException;
    T getThenClose(SQL sql, T def) throws SQLException;
}
