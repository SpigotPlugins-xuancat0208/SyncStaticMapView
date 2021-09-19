package xuan.cat.databasecatmini.api.sql.builder;

import xuan.cat.databasecatmini.api.sql.SQLBuilder;

/**
 * 更改資料表
 */
public interface AlterTable extends SQLBuilder {
    AlterTable clone();

    AlterTable collate(Collate collate);
}
