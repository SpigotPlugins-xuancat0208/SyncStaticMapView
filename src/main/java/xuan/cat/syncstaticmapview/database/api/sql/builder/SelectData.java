package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 選擇資料
 */
public interface SelectData extends SQLBuilder {
    SelectData clone();

    SelectData selectDistinct(boolean selectDistinct);

    <T> SelectData select(Field<T> field);
    SelectData select(SelectReturn dataReturn);
    <T> SelectData select(Field<T> field, SelectReturn dataReturn);

    SelectData where(Where where);
    SelectData where(Consumer<Where> consumer);

    SelectData brackets(Consumer<WhereBrackets> consumer);
    SelectData brackets(WhereBrackets brackets);

    SelectData limit(Integer limit);
    SelectData offset(Integer offset);

    SelectData order(Order order);
    SelectData order(Consumer<Order> consumer);

    SelectData union(SelectData union);

    SelectData unionAll(SelectData unionAll);

    SelectData useCache(boolean useCache);

    SelectData bufferResult(boolean bufferResult);

    boolean selectDistinct();
    Integer limit();
    Integer offset();
    Map<Field, SelectReturn> selects();
    Order order();
    String from();
    InformationSchema informationSchema();
    Where where();
    SelectData union();
    SelectData unionAll();
    boolean bufferResult();
    boolean useCache();
}
