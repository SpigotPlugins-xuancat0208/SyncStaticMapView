package xuan.cat.databasecatmini.code.sql.builder;

import xuan.cat.databasecatmini.api.sql.builder.Field;
import xuan.cat.databasecatmini.api.sql.builder.WhereBrackets;
import xuan.cat.databasecatmini.api.sql.builder.WhereJudge;

import java.util.function.Consumer;

/**
 * 括號
 * 用於將多個{@link CodeWhere}組合
 */
public final class CodeWhereBrackets extends CodeWhere implements WhereBrackets {
    public CodeWhereBrackets() {
        super();
    }

    protected CodeWhereBrackets(CodeWhere where) {
        super(where);
    }


    protected final StringBuilder partOriginal() {
        return CodeFunction.brackets(super.partOriginal());
    }
    public StringBuilder part() {
        return CodeFunction.combination("WHERE", partOriginal());
    }

    public CodeWhereBrackets clone() {
        return new CodeWhereBrackets((CodeWhere) this);
    }



    public <T> CodeWhereBrackets and(Field<T> field , WhereJudge judge) {
        return (CodeWhereBrackets) super.and(field, judge);
    }
    public <T> CodeWhereBrackets and(Field<T> field, T value) {
        return and(field, WhereJudge.EQUAL, value);
    }
    public <T> CodeWhereBrackets and(Field<T> field , WhereJudge judge, T value) {
        return (CodeWhereBrackets) super.and(field, judge, value);
    }
    public <T> CodeWhereBrackets and(Field<T> field, Field<T> value) {
        return and(field, WhereJudge.EQUAL, value);
    }
    public <T> CodeWhereBrackets and(Field<T> field , WhereJudge judge, Field<T> value) {
        return (CodeWhereBrackets) super.and(field, judge, value);
    }
    public CodeWhereBrackets and(WhereBrackets whereBrackets) {
        return (CodeWhereBrackets) super.and(whereBrackets);
    }
    public <T> CodeWhereBrackets and(CodeWhereCondition<T> condition) {
        return (CodeWhereBrackets) super.and(condition);
    }
    public CodeWhereBrackets and(Consumer<WhereBrackets> consumer) {
        return (CodeWhereBrackets) super.and(consumer);
    }



    public <T> CodeWhereBrackets or(Field<T> field , WhereJudge judge) {
        return (CodeWhereBrackets) super.or(field, judge);
    }
    public <T> CodeWhereBrackets or(Field<T> field, T value) {
        return or(field, WhereJudge.EQUAL, value);
    }
    public <T> CodeWhereBrackets or(Field<T> field , WhereJudge judge, Field<T> value) {
        return (CodeWhereBrackets) super.or(field, judge, value);
    }
    public <T> CodeWhereBrackets or(Field<T> field, Field<T> value) {
        return or(field, WhereJudge.EQUAL, value);
    }
    public <T> CodeWhereBrackets or(Field<T> field , WhereJudge judge, T value) {
        return (CodeWhereBrackets) super.or(field, judge, value);
    }
    public CodeWhereBrackets or(WhereBrackets whereBrackets) {
        return (CodeWhereBrackets) super.or(whereBrackets);
    }
    public <T> CodeWhereBrackets or(CodeWhereCondition<T> condition) {
        return (CodeWhereBrackets) super.or(condition);
    }
    public CodeWhereBrackets or(Consumer<WhereBrackets> consumer) {
        return (CodeWhereBrackets) super.or(consumer);
    }
}
