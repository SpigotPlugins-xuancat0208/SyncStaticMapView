package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;

import java.util.function.Consumer;

/**
 * 選擇資料
 */
public interface SelectData extends SQLBuilder {
    SelectData clone();

    <T> SelectData select(Field<T> field);

    <T> SelectData select(Field<T> field, SelectReturn dataReturn);

    SelectData where(Where where);
    SelectData where(Consumer<Where> consumer);

    SelectData brackets(Consumer<WhereBrackets> consumer);
    SelectData brackets(WhereBrackets brackets);

    SelectData limit(Integer limit);

    Where where();

}
