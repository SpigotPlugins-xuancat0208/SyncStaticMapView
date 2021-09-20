package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;

/**
 * 刪除資料庫
 */
public interface DeleteDatabase extends SQLBuilder {
    DeleteDatabase clone();

    String name();
}
