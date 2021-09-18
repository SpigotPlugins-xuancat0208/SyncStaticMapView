package xuan.cat.databasecatmini.api.sql.builder;

import xuan.cat.databasecatmini.api.sql.SQLBuilder;

import java.util.Set;

/**
 * 創建資料表
 */
public interface CreateTable extends SQLBuilder {
    CreateTable clone();

    CreateTable comment(String comment);

    CreateTable collate(Collate collate);

    CreateTable engine(DatabaseEngine engine);

    <T> CreateTable field(Field<T> field);

    CreateTable index(FieldIndex table);

    CreateTable foreign(ForeignKey foreignKey);

    CreateTable autoIncrement(Integer autoIncrement);
    CreateTable autoIncrement(Long autoIncrement);

    <T> CreateTable partition(TablePartition<T> partition);

    String name();
    String comment();
    Collate collate();
    DatabaseEngine engine();
    Set<Field> field();
}
