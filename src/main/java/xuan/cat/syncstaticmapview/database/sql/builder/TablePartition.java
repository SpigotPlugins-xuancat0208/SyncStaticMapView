package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQLPart;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

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
}
