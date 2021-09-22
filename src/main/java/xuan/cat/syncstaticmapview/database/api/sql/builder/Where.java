package xuan.cat.syncstaticmapview.database.api.sql.builder;

import java.util.function.Consumer;

public interface Where {
    <T> Where and(Field<T> field, WhereJudge judge);
    <T> Where and(Field<T> field, T value);
    <T> Where and(Field<T> field, WhereJudge judge, T value);
    <T> Where and(Field<T> field, Field<T> value);
    <T> Where and(Field<T> field, WhereJudge judge, Field<T> value);
    <T extends Number> Where and(Field<T> field, WhereJudge judge, Field<T> value, WhereOperator operator, T calculate);
    Where and(WhereBrackets whereBrackets);
    Where and(Consumer<WhereBrackets> consumer);

    <T> Where or(Field<T> field, WhereJudge judge);
    <T> Where or(Field<T> field, T value);
    <T> Where or(Field<T> field, WhereJudge judge, T value);
    <T> Where or(Field<T> field, Field<T> value);
    <T> Where or(Field<T> field, WhereJudge judge, Field<T> value);
    <T extends Number> Where or(Field<T> field, WhereJudge judge, Field<T> value, WhereOperator operator, T calculate);
    Where or(WhereBrackets whereBrackets);
    Where or(Consumer<WhereBrackets> consumer);
}
