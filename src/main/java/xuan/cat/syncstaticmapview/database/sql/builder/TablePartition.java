package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQLPart;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分區
 */
public abstract class TablePartition<T> implements SQLPart {
    public abstract TablePartition<T> clone();

    /**
     * 沒有清單
     */
    public static abstract class Independent<T> extends TablePartition<T> {
        protected final Field<T>    field;
        protected       int         rows    = 1;
        protected       boolean     linear  = false;


        public Independent(Field<T> field) {
            this.field  = field;
        }
        private Independent(Independent<T> independent) {
            this.field  = SQLTool.tryClone(independent.field);
            this.rows   = SQLTool.tryClone(independent.rows);
            this.linear = SQLTool.tryClone(independent.linear);
        }


        public StringBuilder part() {
            StringBuilder builder = new StringBuilder("PARTITION BY ");
            if (linear)
                builder.append("LINEAR ");
            return builder.append(getName()).append('(').append(SQLTool.toField(field.name())).append(") PARTITIONS ").append(rows);
        }

        public abstract Independent<T> clone();

        protected abstract String getName();

        public Independent<T> rows(int rows) {
            this.rows = rows;
            return this;
        }

        public Independent<T> linear(boolean linear) {
            this.linear = linear;
            return this;
        }

        public Field<T> field() {
            return field;
        }

        public boolean linear() {
            return linear;
        }

        public int rows() {
            return rows;
        }
    }


    public static final class Hash<T> extends Independent<T> {
        public Hash(Field<T> field) {
            super(field);
        }
        public Hash(Hash<T> independent) {
            super(independent);
        }

        public Hash<T> clone() {
            return new Hash<>(this);
        }

        protected String getName() {
            return "HASH";
        }

        public Hash<T> rows(int rows) {
            return (Hash<T>) super.rows(rows);
        }

        public Hash<T> linear(boolean linear) {
            return (Hash<T>) super.linear(linear);
        }
    }


    public static final class Key<T> extends Independent<T> {
        public Key(Field<T> field) {
            super(field);
        }
        private Key(Key<T> independent) {
            super(independent);
        }

        protected String getName() {
            return "KEY";
        }

        public Key<T> clone() {
            return new Key<>(this);
        }

        public Key<T> rows(int rows) {
            return (Key<T>) super.rows(rows);
        }

        public Key<T> linear(boolean linear) {
            return (Key<T>) super.linear(linear);
        }
    }









    /**
     * 有清單
     */
    public static abstract class Combination<T> extends TablePartition<T> {
        protected static final String       isMaxValue  = "MAXVALUE";
        protected final Field<T>            field;
        protected final Map<String, T[]>    sortList;


        public Combination(Field<T> field) {
            this.field          = field;
            this.sortList       = new LinkedHashMap<>();
        }
        private Combination(Combination<T> combination) {
            this.field          = SQLTool.tryClone(combination.field);
            this.sortList       = SQLTool.tryClone(combination.sortList);
        }


        public StringBuilder part() {
            StringBuilder builder = new StringBuilder("PARTITION BY ");
            builder.append(getName()).append('(').append(SQLTool.toField(field.name())).append(") (");

            SQLTool.toStringFromMap(sortList, (merge, name, values) -> {
                merge.append("PARTITION ");
                merge.append(SQLTool.toField(name));
                merge.append(" VALUES ");
                merge.append(getValuesName());
                merge.append(SQLTool.brackets(SQLTool.toStringFromArray(values, (mergeChild, value) -> {
                    mergeChild.append(value == null ? isMaxValue : SQLTool.toValue(field, (T) value));
                })));
            });

            builder.append(')');
            return builder;
        }

        public abstract Combination<T> clone();

        protected abstract String getName();

        protected abstract String getValuesName();

        public Field<T> field() {
            return field;
        }

        public Combination<T> slice(String name, T... values) {
            if (values == null)
                throw new NullPointerException("values");
            sortList.put(name, values);
            return this;
        }

        public Combination<T> sliceMax(String name) {
            sortList.put(name, null);
            return this;
        }
    }


    public static final class Range<T> extends Combination<T> {
        public Range(Field<T> field) {
            super(field);
        }
        private Range(Range<T> combination) {
            super(combination);
        }

        protected String getName() {
            return "KEY";
        }

        protected String getValuesName() {
            return "LESS THAN";
        }

        public Range<T> clone() {
            return new Range<>(this);
        }

        public Range<T> slice(String name, T... values) {
            return (Range<T>) super.slice(name, values);
        }

        public Range<T> sliceMax(String name) {
            return (Range<T>) super.sliceMax(name);
        }
    }


    public static final class List<T> extends Combination<T> {
        public List(Field<T> field) {
            super(field);
        }
        private List(List<T> combination) {
            super(combination);
        }

        protected String getName() {
            return "KEY";
        }

        protected String getValuesName() {
            return "IN";
        }

        public List<T> clone() {
            return new List<>(this);
        }

        public List<T> slice(String name, T... values) {
            return (List<T>) super.slice(name, values);
        }

        public List<T> sliceMax(String name) {
            return (List<T>) super.sliceMax(name);
        }
    }
}
