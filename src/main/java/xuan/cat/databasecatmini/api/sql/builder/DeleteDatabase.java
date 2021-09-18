package xuan.cat.databasecatmini.api.sql.builder;

import xuan.cat.databasecatmini.api.sql.SQLBuilder;

/**
 * 刪除資料庫
 */
public interface DeleteDatabase extends SQLBuilder {
    DeleteDatabase clone();

    String name();
}
