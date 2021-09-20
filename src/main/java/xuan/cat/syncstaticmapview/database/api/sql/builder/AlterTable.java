package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;

/**
 * 更改資料表
 */
public interface AlterTable extends SQLBuilder {
    AlterTable clone();

    AlterTable collate(Collate collate);
}
