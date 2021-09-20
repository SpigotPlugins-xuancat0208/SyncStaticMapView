package xuan.cat.syncstaticmapview.database.api.sql.builder;

public interface ForeignKey {
    ForeignKey clone();

    ForeignKey from(String from);

    ForeignKey table(String table);
    ForeignKey table(Enum<?> table);
}
