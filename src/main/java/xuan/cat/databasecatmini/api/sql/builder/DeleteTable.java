package xuan.cat.databasecatmini.api.sql.builder;

import xuan.cat.databasecatmini.api.sql.SQLBuilder;

/**
 * 刪除資料表
 */
public interface DeleteTable extends SQLBuilder {
    DeleteTable clone();

    String name();
}
