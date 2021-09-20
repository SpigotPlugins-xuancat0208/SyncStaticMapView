package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;

/**
 * 插入資料
 */
public interface InsertData extends SQLBuilder {
    InsertData clone();

    <T> InsertData insert(Field<T> field, T value);

    <T> InsertData updates(Field<T> field, T value);

    <T> InsertData updates(Field<T> field, T value, UpdateAlgorithm algorithm);

    String name();
}