package xuan.cat.databasecatmini.api.sql.builder;

import xuan.cat.databasecatmini.api.sql.SQLBuilder;

import java.util.Set;

/**
 * 創建資料表
 */
public interface CreateTable extends SQLBuilder {
    CreateTable clone();

    CreateTable collate(Collate collate);

    CreateTable engine(DatabaseEngine engine);

    <T> CreateTable field(Field<T> field);

    CreateTable index(FieldIndex table);


    <T> CreateTable partition(TablePartition<T> partition);

    String name();
    Collate collate();
    Set<Field> field();
}
