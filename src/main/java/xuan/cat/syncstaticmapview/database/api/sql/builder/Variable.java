package xuan.cat.syncstaticmapview.database.api.sql.builder;

public interface Variable {
    interface UNHEX extends Variable {
        char[] getChars();
    }
    interface TIME extends Variable {
    }
}
