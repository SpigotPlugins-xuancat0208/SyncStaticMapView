package xuan.cat.syncstaticmapview.database.api.sql.builder;

import java.util.function.Consumer;

public interface Where {
    <T> Where and(Field<T> field, WhereJudge judge);
    <T> Where and(Field<T> field, T value);
    <T> Where and(Field<T> field, WhereJudge judge, T value);
    <T> Where and(Field<T> field, Field<T> value);
    <T> Where and(Field<T> field, WhereJudge judge, Field<T> value);
    Where and(WhereBrackets whereBrackets);
    Where and(Consumer<WhereBrackets> consumer);

    <T> Where or(Field<T> field, WhereJudge judge);

    <T> Where or(Field<T> field, WhereJudge judge, T value);

    <T> Where or(Field<T> field, WhereJudge judge, Field<T> value);
    Where or(WhereBrackets whereBrackets);
    Where or(Consumer<WhereBrackets> consumer);
}
