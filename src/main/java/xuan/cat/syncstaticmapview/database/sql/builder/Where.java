package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.SQLPart;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 判斷式
 */
public class Where implements SQLPart {
    private final Map<Object, WhereMutual> conditions;


    public Where() {
        this.conditions = new LinkedHashMap<>();
    }
    protected Where(Where where) {
        this.conditions = SQLTool.tryClone(where.conditions);
    }



    protected StringBuilder partOriginal() {
        Set<CharSequence> list = new LinkedHashSet<>( conditions.size() );
        conditions.forEach((condition, mutual) -> {
            if (condition instanceof WhereCondition) {
                // 單個判斷式
                WhereCondition whereCondition = (WhereCondition) condition;
                if (list.size() <= 0) {
                    list.add(SQLTool.combination(whereCondition.part()));
                } else {
                    list.add(SQLTool.combination(mutual.part(), whereCondition.part()));
                }
            } else if (condition instanceof WhereBrackets) {
                // 括號複數判斷式
                WhereBrackets whereBrackets = (WhereBrackets) condition;
                if (list.size() <= 0) {
                    list.add(SQLTool.combination(whereBrackets.partOriginal()));
                } else {
                    list.add(SQLTool.combination(mutual.part(), whereBrackets.partOriginal()));
                }
            }
        });
        return SQLTool.combination(list.toArray(new CharSequence[0]));
    }
    public StringBuilder part() {
        return SQLTool.combination("WHERE", partOriginal());
    }

    public Where clone() {
        return new Where(this);
    }



    private void put(Object o, WhereMutual mutual) {
        if (conditions.size() <= 0) {
            // 第一個不用加入 AND 或 OR
            conditions.put(o, null);
        } else {
            // 檢查是否有舊的
            if (o instanceof WhereCondition) {
                WhereCondition c = condition(((WhereCondition) o).field);
                if (c != null)
                    conditions.remove(c);
            }
            conditions.put(o, mutual);
        }
    }



    public <T> Where and(Field<T> field, WhereJudge judge) {
        return and(new WhereCondition.Value<>(field, judge));
    }
    public <T> Where and(Field<T> field, T value) {
        return and(field, WhereJudge.EQUAL, value);
    }
    public <T> Where and(Field<T> field, WhereJudge judge, T value) {
        return and(new WhereCondition.Value<>(field, judge, value));
    }
    public <T> Where and(Field<T> field, Field<T> value) {
        return and(field, WhereJudge.EQUAL, value);
    }
    public <T> Where and(Field<T> field, WhereJudge judge, Field<T> value) {
        return and(new WhereCondition.Relatively<>(field, judge, value));
    }
    public <T extends Number> Where and(Field<T> field, WhereJudge judge, Field<T> value, WhereOperator operator, T calculate) {
        return and(new WhereCondition.Operator<>(field, judge, value, operator, calculate));
    }
    public <T> Where and(WhereCondition<T> condition) {
        put(condition, WhereMutual.AND);
        return this;
    }
    public Where and(WhereBrackets whereBrackets) {
        put(whereBrackets, WhereMutual.AND);
        return this;
    }
    public Where and(Consumer<WhereBrackets> consumer) {
        WhereBrackets brackets = new WhereBrackets();
        consumer.accept(brackets);
        and(brackets);
        return this;
    }




    public <T> Where or(Field<T> field, WhereJudge judge) {
        return or(new WhereCondition.Value<>(field, judge));
    }
    public <T> Where or(Field<T> field, T value) {
        return or(field, WhereJudge.EQUAL, value);
    }
    public <T> Where or(Field<T> field, WhereJudge judge, T value) {
        return or(new WhereCondition.Value<>(field, judge, value));
    }
    public <T> Where or(Field<T> field, Field<T> value) {
        return or(field, WhereJudge.EQUAL, value);
    }
    public <T> Where or(Field<T> field, WhereJudge judge, Field<T> value) {
        return or(new WhereCondition.Relatively<>(field, judge, value));
    }
    public <T extends Number> Where or(Field<T> field, WhereJudge judge, Field<T> value, WhereOperator operator, T calculate) {
        return or(new WhereCondition.Operator<>(field, judge, value, operator, calculate));
    }
    public <T> Where or(WhereCondition<T> condition) {
        put(condition, WhereMutual.OR);
        return this;
    }
    public Where or(WhereBrackets whereBrackets) {
        put(whereBrackets, WhereMutual.OR);
        return this;
    }
    public Where or(Consumer<WhereBrackets> consumer) {
        WhereBrackets brackets = new WhereBrackets();
        consumer.accept(brackets);
        or(brackets);
        return this;
    }




    public <T> WhereCondition<T> condition(Field<T> field) {
        AtomicReference<WhereCondition> condition = new AtomicReference<>(null);
        conditions.forEach((k, v) -> {
            if (k instanceof WhereCondition && k.equals(field)) {
                condition.set((WhereCondition) k);
            }
        });
        return condition.get();
    }
}
