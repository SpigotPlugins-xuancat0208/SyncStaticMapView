package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;

/**
 * 更改資料表
 */
public interface AlterTable extends SQLBuilder {
    AlterTable clone();

    AlterTable rename(Enum<?> rename);
    AlterTable rename(String rename);

    AlterTable collate(Collate collate);

    AlterTable comment(String comment);

    AlterTable engine(DatabaseEngine engine);

    <T> AlterTable tableAdd(Field<T> field);
    <T> AlterTable tableChange(Field<T> field);
    <T> AlterTable tableRemove(Field<T> field);

    AlterTable indexAdd(FieldIndex index);
    AlterTable indexRemove(FieldIndex index);

    AlterTable foreignAdd(ForeignKey foreignKey);
    AlterTable foreignRemove(ForeignKey foreignKey);

    AlterTable autoIncrement(Integer autoIncrement);
    AlterTable autoIncrement(Long autoIncrement);

    <T> AlterTable partition(TablePartition<T> partition);

    String name();
    DatabaseEngine engine();
    Collate collate();
    String comment();
    String rename();
}
