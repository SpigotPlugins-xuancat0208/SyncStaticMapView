package xuan.cat.databasecatmini.api.sql.builder;

import xuan.cat.databasecatmini.api.sql.SQLBuilder;

import java.util.function.Consumer;

/**
 * 刪除資料
 */
public interface DeleteData extends SQLBuilder {
    DeleteData clone();

    DeleteData limit(Integer limit);

    DeleteData where(Where where);
    DeleteData where(Consumer<Where> consumer);
    DeleteData brackets(Consumer<WhereBrackets> consumer);
    DeleteData brackets(WhereBrackets brackets);

    Where where();
    String name();
}
