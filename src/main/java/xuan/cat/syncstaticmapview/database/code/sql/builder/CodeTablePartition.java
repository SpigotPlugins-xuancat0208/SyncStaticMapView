package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.builder.Field;
import xuan.cat.syncstaticmapview.database.api.sql.builder.TablePartition;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLPart;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分區
 */
public abstract class CodeTablePartition<T> implements TablePartition<T>, CodeSQLPart {
    public abstract CodeTablePartition<T> clone();

    /**
     * 沒有清單
     */
    public static abstract class CodeIndependent<T> extends CodeTablePartition<T> implements TablePartition.Independent<T> {

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

        public boolean linear() {
            return linear;
        }

        public int rows() {
            return rows;
        }
    }


    public static final class CodeHash<T> extends CodeIndependent<T> implements TablePartition.Hash<T> {
        public CodeHash(Field<T> field) {
            super(field);
        }
        public CodeHash(CodeHash<T> independent) {
            super(independent);
        }

        public CodeHash<T> clone() {
            return new CodeHash<>(this);
        }

        protected String getName() {
            return "HASH";
        }

        public CodeHash<T> rows(int rows) {
            return (CodeHash<T>) super.rows(rows);
        }

        public CodeHash<T> linear(boolean linear) {
            return (CodeHash<T>) super.linear(linear);
        }
    }


    public static final class CodeKey<T> extends CodeIndependent<T> implements TablePartition.Key<T> {
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









    /**
     * 有清單
     */
    public static abstract class CodeCombination<T> extends CodeTablePartition<T> implements TablePartition.Combination<T> {

        protected static final String       isMaxValue  = "MAXVALUE";
        protected final Field<T>            field;
        protected final Map<String, T[]>    sortList;

        public CodeCombination(Field<T> field) {
            this.field          = field;
            this.sortList       = new LinkedHashMap<>();
        }
        private CodeCombination(CodeCombination<T> combination) {
            this.field          = CodeFunction.tryClone(combination.field);
            this.sortList       = CodeFunction.tryClone(combination.sortList);
        }

        public StringBuilder part() {
            StringBuilder builder = new StringBuilder("PARTITION BY ");
            builder.append(getName()).append('(').append(CodeFunction.toField(field.name())).append(") (");

            CodeFunction.toStringFromMap(sortList, (merge, name, values) -> {
                merge.append("PARTITION ");
                merge.append(CodeFunction.toField(name));
                merge.append(" VALUES ");
                merge.append(getValuesName());
                merge.append(CodeFunction.brackets(CodeFunction.toStringFromArray(values, (mergeChild, value) -> {
                    mergeChild.append(value == null ? isMaxValue : CodeFunction.toValue(field, (T) value));
                })));
            });

            builder.append(')');
            return builder;
        }

        public abstract CodeCombination<T> clone();

        protected abstract String getName();

        protected abstract String getValuesName();

        public Field<T> field() {
            return field;
        }

        public CodeCombination<T> slice(String name, T... values) {
            if (values == null)
                throw new NullPointerException("values");
            sortList.put(name, values);
            return this;
        }

        public CodeCombination<T> sliceMax(String name) {
            sortList.put(name, null);
            return this;
        }
    }


    public static final class CodeRange<T> extends CodeCombination<T> implements TablePartition.Range<T> {
        public CodeRange(Field<T> field) {
            super(field);
        }
        private CodeRange(CodeRange<T> combination) {
            super(combination);
        }

        protected String getName() {
            return "KEY";
        }

        protected String getValuesName() {
            return "LESS THAN";
        }

        public CodeRange<T> clone() {
            return new CodeRange<>(this);
        }

        public CodeRange<T> slice(String name, T... values) {
            return (CodeRange<T>) super.slice(name, values);
        }

        public CodeRange<T> sliceMax(String name) {
            return (CodeRange<T>) super.sliceMax(name);
        }
    }


    public static final class CodeList<T> extends CodeCombination<T> implements TablePartition.List<T> {
        public CodeList(Field<T> field) {
            super(field);
        }
        private CodeList(CodeList<T> combination) {
            super(combination);
        }

        protected String getName() {
            return "KEY";
        }

        protected String getValuesName() {
            return "IN";
        }

        public CodeList<T> clone() {
            return new CodeList<>(this);
        }

        public CodeList<T> slice(String name, T... values) {
            return (CodeList<T>) super.slice(name, values);
        }

        public CodeList<T> sliceMax(String name) {
            return (CodeList<T>) super.sliceMax(name);
        }
    }
}
