package xuan.cat.databasecatmini.api.sql.builder;

import xuan.cat.databasecatmini.api.sql.SQLBuilder;

/**
 * 創建資料庫
 */
public interface CreateDatabase extends SQLBuilder {
    CreateDatabase clone();

    CreateDatabase collate(Collate collate);

    String name();
    Collate collate();
}
