package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.builder.Field;
import xuan.cat.syncstaticmapview.database.api.sql.builder.TablePartition;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLPart;

/**
 * 分區
 */
public abstract class CodeTablePartition<T> implements TablePartition<T>, CodeSQLPart {
    public abstract CodeTablePartition<T> clone();

    /**
     * 沒有清單
     */
    public static abstract class CodeIndependent<T> extends CodeTablePartition<T> implements Independent<T> {

        protected final Field<T>    field;
        protected       int         rows    = 1;
        protected       boolean     linear  = false;

        public CodeIndependent(Field<T> field) {
            this.field  = field;
        }
        private CodeIndependent(CodeIndependent<T> independent) {
            this.field  = CodeFunction.tryClone(independent.field);
            this.rows   = CodeFunction.tryClone(independent.rows);
            this.linear = CodeFunction.tryClone(independent.linear);
        }

        public StringBuilder part() {
            StringBuilder builder = new StringBuilder("PARTITION BY ");
            if (linear)
                builder.append("LINEAR ");
            return builder.append(getName()).append('(').append(CodeFunction.toField(field.name())).append(") PARTITIONS ").append(rows);
        }

        public abstract CodeIndependent<T> clone();

        protected abstract String getName();

        public CodeIndependent<T> rows(int rows) {
            this.rows = rows;
            return this;
        }

        public CodeIndependent<T> linear(boolean linear) {
            this.linear = linear;
            return this;
        }

        public Field<T> field() {
            return field;
        }

    }


    public static final class CodeKey<T> extends CodeIndependent<T> implements Key<T> {
        public CodeKey(Field<T> field) {
            super(field);
        }
        private CodeKey(CodeKey<T> independent) {
            super(independent);
        }

        protected String getName() {
            return "KEY";
        }

        public CodeKey<T> clone() {
            return new CodeKey<>(this);
        }

        public CodeKey<T> rows(int rows) {
            return (CodeKey<T>) super.rows(rows);
        }

        public CodeKey<T> linear(boolean linear) {
            return (CodeKey<T>) super.linear(linear);
        }
    }


}
