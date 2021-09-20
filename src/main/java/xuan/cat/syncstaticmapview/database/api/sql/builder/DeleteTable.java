package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;

/**
 * 刪除資料表
 */
public interface DeleteTable extends SQLBuilder {
    DeleteTable clone();

    String name();
}
