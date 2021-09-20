package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;

import java.util.function.Consumer;

/**
 * 更新資料
 */
public interface UpdateData extends SQLBuilder {
    UpdateData clone();

    UpdateData where(Where where);
    UpdateData where(Consumer<Where> consumer);
    UpdateData brackets(Consumer<WhereBrackets> consumer);
    UpdateData brackets(WhereBrackets brackets);

    UpdateData limit(Integer limit);

    <T> UpdateData updates(Field<T> field, T value);

    <T> UpdateData updates(Field<T> field, T value, UpdateAlgorithm algorithm);

    String name();
    Where where();

}
