package xuan.cat.databasecatmini.api.sql.builder;

public interface ForeignKey {
    ForeignKey clone();

    ForeignKey from(String from);

    ForeignKey table(String table);
    ForeignKey table(Enum<?> table);
}
