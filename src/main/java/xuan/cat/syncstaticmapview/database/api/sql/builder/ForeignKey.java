package xuan.cat.syncstaticmapview.database.api.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.DatabaseTable;

public interface ForeignKey {
    ForeignKey clone();

    ForeignKey from(DatabaseTable table);
    ForeignKey from(String from);
    ForeignKey from(Enum<?> from);

    ForeignKey onDelete(ForeignConstraints onDelete);

    ForeignKey onUpdate(ForeignConstraints onUpdate);

    ForeignKey table(String table);
    ForeignKey table(Enum<?> table);

    String name();
    String from();
    String table();
    ForeignConstraints onDelete();
    ForeignConstraints onUpdate();
}
