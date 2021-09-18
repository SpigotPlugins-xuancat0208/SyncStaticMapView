package xuan.cat.databasecatmini.api.sql.builder;

public interface Variable {
    interface UNHEX extends Variable {
        char[] getChars();
    }
    interface TIME extends Variable {
    }
}
