package xuan.cat.databasecatmini.code.sql.builder;

import xuan.cat.databasecatmini.api.sql.builder.*;
import xuan.cat.databasecatmini.code.sql.CodeSQLPart;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 判斷式
 */
public class CodeWhere implements CodeSQLPart, Where {

    private final Map<Object, WhereMutual> conditions;


    public CodeWhere() {
        this.conditions = new LinkedHashMap<>();
    }
    protected CodeWhere(CodeWhere where) {
        this.conditions = CodeFunction.tryClone(where.conditions);
    }





    protected StringBuilder partOriginal() {
        Set<CharSequence> list = new LinkedHashSet<>( conditions.size() );
        conditions.forEach((condition, mutual) -> {
            if (condition instanceof CodeWhereCondition) {
                // 單個判斷式
                CodeWhereCondition whereCondition = (CodeWhereCondition) condition;
                if (list.size() <= 0) {
                    list.add(CodeFunction.combination(whereCondition.part()));
                } else {
                    list.add(CodeFunction.combination(mutual.part(), whereCondition.part()));
                }
            } else if (condition instanceof CodeWhereBrackets) {
                // 括號複數判斷式
                CodeWhereBrackets whereBrackets = (CodeWhereBrackets) condition;
                if (list.size() <= 0) {
                    list.add(CodeFunction.combination(whereBrackets.partOriginal()));
                } else {
                    list.add(CodeFunction.combination(mutual.part(), whereBrackets.partOriginal()));
                }
            }
        });
        return CodeFunction.combination(list.toArray(new CharSequence[0]));
    }
    public StringBuilder part() {
        return CodeFunction.combination("WHERE", partOriginal());
    }

    public CodeWhere clone() {
        return new CodeWhere(this);
    }




    private void put(Object o, WhereMutual mutual) {
        if (conditions.size() <= 0) {
            // 第一個不用加入 AND 或 OR
            conditions.put(o, null);
        } else {
            // 檢查是否有舊的
            if (o instanceof CodeWhereCondition) {
                CodeWhereCondition c = condition(((CodeWhereCondition) o).field);
                if (c != null)
                    conditions.remove(c);
            }
            conditions.put(o, mutual);
        }
    }




    public <T> CodeWhere and(Field<T> field, WhereJudge judge) {
        return and(new CodeWhereCondition.Value<>(field, judge));
    }
    public <T> CodeWhere and(Field<T> field, T value) {
        return and(field, WhereJudge.EQUAL, value);
    }
    public <T> CodeWhere and(Field<T> field, WhereJudge judge, T value) {
        return and(new CodeWhereCondition.Value<>(field, judge, value));
    }
    public <T> CodeWhere and(Field<T> field, Field<T> value) {
        return and(field, WhereJudge.EQUAL, value);
    }
    public <T> CodeWhere and(Field<T> field, WhereJudge judge, Field<T> value) {
        return and(new CodeWhereCondition.Relatively<>(field, judge, value));
    }
    public <T> CodeWhere and(CodeWhereCondition<T> condition) {
        put(condition, WhereMutual.AND);
        return this;
    }
    public CodeWhere and(WhereBrackets whereBrackets) {
        put(whereBrackets, WhereMutual.AND);
        return this;
    }
    public CodeWhere and(Consumer<WhereBrackets> consumer) {
        CodeWhereBrackets brackets = new CodeWhereBrackets();
        consumer.accept(brackets);
        and(brackets);
        return this;
    }




    public <T> CodeWhere or(Field<T> field, WhereJudge judge) {
        return or(new CodeWhereCondition.Value<>(field, judge));
    }
    public <T> CodeWhere or(Field<T> field, T value) {
        return or(field, WhereJudge.EQUAL, value);
    }
    public <T> CodeWhere or(Field<T> field, WhereJudge judge, T value) {
        return or(new CodeWhereCondition.Value<>(field, judge, value));
    }
    public <T> CodeWhere or(Field<T> field, Field<T> value) {
        return or(field, WhereJudge.EQUAL, value);
    }
    public <T> CodeWhere or(Field<T> field, WhereJudge judge, Field<T> value) {
        return or(new CodeWhereCondition.Relatively<>(field, judge, value));
    }
    public <T> CodeWhere or(CodeWhereCondition<T> condition) {
        put(condition, WhereMutual.OR);
        return this;
    }
    public CodeWhere or(WhereBrackets whereBrackets) {
        put(whereBrackets, WhereMutual.OR);
        return this;
    }
    public CodeWhere or(Consumer<WhereBrackets> consumer) {
        CodeWhereBrackets brackets = new CodeWhereBrackets();
        consumer.accept(brackets);
        or(brackets);
        return this;
    }




    public <T> CodeWhereCondition<T> condition(Field<T> field) {
        AtomicReference<CodeWhereCondition> condition = new AtomicReference<>(null);
        conditions.forEach((k, v) -> {
            if (k instanceof CodeWhereCondition && k.equals(field)) {
                condition.set((CodeWhereCondition) k);
            }
        });
        return condition.get();
    }
}
