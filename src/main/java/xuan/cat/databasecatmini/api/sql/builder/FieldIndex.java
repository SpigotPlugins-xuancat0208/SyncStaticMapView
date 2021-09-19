package xuan.cat.databasecatmini.api.sql.builder;

public interface FieldIndex {
    FieldIndex clone();


    FieldIndex field(Enum<?> field);
    FieldIndex field(String field);

    String name();

    String field();
}
