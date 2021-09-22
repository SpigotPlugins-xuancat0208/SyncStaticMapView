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

    UpdateData order(Order order);
    UpdateData order(Consumer<Order> consumer);

    UpdateData offset(Integer offset);

    UpdateData limit(Integer limit);

    <T> UpdateData updates(Field<T> field, T value);

    <T extends Number> UpdateData updatesIncrease(Field<T> field, T value);
    <T extends Number> UpdateData updatesSubtract(Field<T> field, T value);

    <T> UpdateData updates(Field<T> field, T value, UpdateAlgorithm algorithm);

    UpdateData lowPriority(boolean lowPriority);

    boolean lowPriority();


    Order order();
    String name();
    Where where();
    Integer offset();
    Integer limit();
}
