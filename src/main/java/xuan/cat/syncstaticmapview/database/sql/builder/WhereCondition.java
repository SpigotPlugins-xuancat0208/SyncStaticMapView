package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQLPart;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

/**
 * 條件判斷式
 * 表示單個判斷式
 */
public abstract class WhereCondition<T> implements SQLPart {
    public    final Field<T>    field;
    protected final WhereJudge  judge;


    protected WhereCondition(Field<T> field, WhereJudge judge, boolean isClone) {
        if (isClone) {
            this.field  = SQLTool.tryClone(field);
            this.judge  = SQLTool.tryClone(judge);
        } else {
            this.field  = field;
            this.judge  = judge;
        }
    }


    public StringBuilder part() {
        if (judge == WhereJudge.IS_NOT_NULL || judge == WhereJudge.IS_NULL) {
            return new StringBuilder().append(SQLTool.toField(field.name())).append(judge.part());
        } else if (judge == WhereJudge.FIND_IN_SET) {
            return new StringBuilder().append(judge.part()).append('(').append(partValue()).append(',').append(SQLTool.toField(field.name())).append(')');
        } else {
            return new StringBuilder().append(SQLTool.toField(field.name())).append(judge.part()).append(partValue());
        }
    }


    public abstract String partValue();

    public abstract WhereCondition<T> clone();


    /**
     * 是數值
     * @param <T>
     */
    public static final class Value<T> extends WhereCondition<T> {
        private final T         value;

        public Value(Field<T> field, WhereJudge judge) {
            this(field, judge, null);
        }
        public Value(Field<T> field, WhereJudge judge, T value) {
            this(field, judge, value, false);
        }
        private Value(Field<T> field, WhereJudge judge, T value, boolean isClone) {
            super(field, judge, isClone);
            if (isClone) {
                this.value  = SQLTool.tryClone(value);
            } else {
                this.value  = value;
            }
        }

        public String partValue() {
            return SQLTool.toValue(field, value);
        }

        public Value<T> clone() {
            return new Value<>(field, judge, value, true);
        }
    }


    /**
     * 是欄位
     */
    public static final class Relatively<T> extends WhereCondition<T> {
        private final Field<T>  value;

        public Relatively(Field<T> field, WhereJudge judge, Field<T> value) {
            this(field, judge, value, false);
        }
        private Relatively(Field<T> field, WhereJudge judge, Field<T> value, boolean isClone) {
            super(field, judge, isClone);
            if (isClone) {
                this.value  = SQLTool.tryClone(value);
            } else {
                this.value  = value;
            }
        }

        public String partValue() {
            return SQLTool.toField(value.name());
        }

        public Relatively<T> clone() {
            return new Relatively<>(field, judge, value, true);
        }
    }


    /**
     * 是運算符欄位
     */
    public static final class Operator<T extends Number> extends WhereCondition<T> {
        private final Field<T>      value;
        private final WhereOperator operator;
        private final T             calculate;

        public Operator(Field<T> field, WhereJudge judge, Field<T> value, WhereOperator operator, T calculate) {
            this(field, judge, value, operator, calculate, false);
        }
        private Operator(Field<T> field, WhereJudge judge, Field<T> value, WhereOperator operator, T calculate, boolean isClone) {
            super(field, judge, isClone);
            if (isClone) {
                this.value      = SQLTool.tryClone(value);
                this.operator   = SQLTool.tryClone(operator);
                this.calculate  = SQLTool.tryClone(calculate);
            } else {
                this.value      = value;
                this.operator   = operator;
                this.calculate  = calculate;
            }
        }

        public String partValue() {
            return SQLTool.toField(value.name()) + operator.part() + SQLTool.toValue(value, calculate);
        }

        public Operator<T> clone() {
            return new Operator<>(field, judge, value, operator, calculate, true);
        }
    }
}
