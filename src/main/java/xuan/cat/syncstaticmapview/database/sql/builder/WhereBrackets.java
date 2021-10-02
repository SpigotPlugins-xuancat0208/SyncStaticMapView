package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.util.function.Consumer;

/**
 * 括號
 * 用於將多個{@link Where}組合
 */
public final class WhereBrackets extends Where {
    public WhereBrackets() {
        super();
    }

    protected WhereBrackets(Where where) {
        super(where);
    }


    protected final StringBuilder partOriginal() {
        return SQLTool.brackets(super.partOriginal());
    }
    public StringBuilder part() {
        return SQLTool.combination("WHERE", partOriginal());
    }

    public WhereBrackets clone() {
        return new WhereBrackets((Where) this);
    }



    public <T> WhereBrackets and(Field<T> field , WhereJudge judge) {
        return (WhereBrackets) super.and(field, judge);
    }
    public <T> WhereBrackets and(Field<T> field, T value) {
        return and(field, WhereJudge.EQUAL, value);
    }
    public <T> WhereBrackets and(Field<T> field , WhereJudge judge, T value) {
        return (WhereBrackets) super.and(field, judge, value);
    }
    public <T> WhereBrackets and(Field<T> field, Field<T> value) {
        return and(field, WhereJudge.EQUAL, value);
    }
    public <T> WhereBrackets and(Field<T> field , WhereJudge judge, Field<T> value) {
        return (WhereBrackets) super.and(field, judge, value);
    }
    public <T extends Number> WhereBrackets and(Field<T> field, WhereJudge judge, Field<T> value, WhereOperator operator, T calculate) {
        return (WhereBrackets) super.and(field, judge, value, operator, calculate);
    }
    public WhereBrackets and(WhereBrackets whereBrackets) {
        return (WhereBrackets) super.and(whereBrackets);
    }
    public <T> WhereBrackets and(WhereCondition<T> condition) {
        return (WhereBrackets) super.and(condition);
    }
    public WhereBrackets and(Consumer<WhereBrackets> consumer) {
        return (WhereBrackets) super.and(consumer);
    }



    public <T> WhereBrackets or(Field<T> field , WhereJudge judge) {
        return (WhereBrackets) super.or(field, judge);
    }
    public <T> WhereBrackets or(Field<T> field, T value) {
        return or(field, WhereJudge.EQUAL, value);
    }
    public <T> WhereBrackets or(Field<T> field , WhereJudge judge, Field<T> value) {
        return (WhereBrackets) super.or(field, judge, value);
    }
    public <T> WhereBrackets or(Field<T> field, Field<T> value) {
        return or(field, WhereJudge.EQUAL, value);
    }
    public <T> WhereBrackets or(Field<T> field , WhereJudge judge, T value) {
        return (WhereBrackets) super.or(field, judge, value);
    }
    public <T extends Number> WhereBrackets or(Field<T> field, WhereJudge judge, Field<T> value, WhereOperator operator, T calculate) {
        return (WhereBrackets) super.or(field, judge, value, operator, calculate);
    }
    public WhereBrackets or(WhereBrackets whereBrackets) {
        return (WhereBrackets) super.or(whereBrackets);
    }
    public <T> WhereBrackets or(WhereCondition<T> condition) {
        return (WhereBrackets) super.or(condition);
    }
    public WhereBrackets or(Consumer<WhereBrackets> consumer) {
        return (WhereBrackets) super.or(consumer);
    }
}
