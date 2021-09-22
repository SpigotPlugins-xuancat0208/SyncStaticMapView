package xuan.cat.syncstaticmapview.database.api.sql.builder;

/**
 * 分區
 */
public interface TablePartition<T> {

    TablePartition<T> clone();
    Field<T> field();



    interface Independent<T> extends TablePartition<T> {
        boolean linear();
        int rows();
        Independent<T> clone();
        Independent<T> rows(int rows);
        Independent<T> linear(boolean linear);
    }
    interface Hash<T> extends Independent<T> {
        Hash<T> clone();
        Hash<T> rows(int rows);
        Hash<T> linear(boolean linear);
    }
    interface Key<T> extends Independent<T> {
        Key<T> clone();
        Key<T> rows(int rows);
        Key<T> linear(boolean linear);
    }



    interface Combination<T> extends TablePartition<T> {
        Combination<T> clone();
        Combination<T> slice(String name, T... values);
        Combination<T> sliceMax(String name);
    }
    interface Range<T> extends Combination<T> {
        Range<T> clone();
        Range<T> slice(String name, T... values);
        Range<T> sliceMax(String name);

    }
    interface List<T> extends Combination<T> {
        List<T> clone();
        List<T> slice(String name, T... values);
        List<T> sliceMax(String name);
    }
}
