package xuan.cat.syncstaticmapview.database.api.sql.builder;

import java.util.function.Consumer;

public interface WhereBrackets extends Where {
    <T> WhereBrackets and(Field<T> field, WhereJudge judge);
    <T> WhereBrackets and(Field<T> field, T value);
    <T> WhereBrackets and(Field<T> field, WhereJudge judge, T value);
    <T> WhereBrackets and(Field<T> field, Field<T> value);
    <T> WhereBrackets and(Field<T> field, WhereJudge judge, Field<T> value);
    <T extends Number> WhereBrackets and(Field<T> field, WhereJudge judge, Field<T> value, WhereOperator operator, T calculate);
    WhereBrackets and(WhereBrackets whereBrackets);
    WhereBrackets and(Consumer<WhereBrackets> consumer);

    <T> WhereBrackets or(Field<T> field, WhereJudge judge);
    <T> WhereBrackets or(Field<T> field, T value);
    <T> WhereBrackets or(Field<T> field, WhereJudge judge, T value);
    <T> WhereBrackets or(Field<T> field, Field<T> value);
    <T> WhereBrackets or(Field<T> field, WhereJudge judge, Field<T> value);
    <T extends Number> WhereBrackets or(Field<T> field, WhereJudge judge, Field<T> value, WhereOperator operator, T calculate);
    WhereBrackets or(WhereBrackets whereBrackets);
    WhereBrackets or(Consumer<WhereBrackets> consumer);
}
