package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;

/**
 * 創建資料庫
 */
public interface CreateDatabase extends SQLBuilder {
    CreateDatabase clone();

    CreateDatabase collate(Collate collate);

    String name();
    Collate collate();
}
