package xuan.cat.syncstaticmapview.database.code.sql.builder;

import xuan.cat.syncstaticmapview.database.api.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.api.sql.builder.Field;
import xuan.cat.syncstaticmapview.database.api.sql.builder.InsertData;
import xuan.cat.syncstaticmapview.database.api.sql.builder.UpdateAlgorithm;
import xuan.cat.syncstaticmapview.database.code.sql.CodeSQLBuilder;

import java.util.*;

/**
 * 插入資料
 */
public final class CodeInsertData implements CodeSQLBuilder, InsertData {

    private final Map<Field, List<Object>>  values;            // 欄位對應值清單
    private final Map<Field, Change>        updates;           // 欄位對應值清單
    private final String                    name;
    private       boolean                   lowPriority = false;


    private static class Change<T> {
        Change(Field<T> field, T value, UpdateAlgorithm algorithm) {
            this.field      = field;
            this.value      = value;
            this.algorithm  = algorithm;
        }

        public Field<T>         field;
        public T                value;
        public UpdateAlgorithm  algorithm;
    }


    public CodeInsertData(DatabaseTable table) {
        this.values         = new HashMap<>();
        this.updates        = new HashMap<>();
        this.name           = table.getName();
    }
    private CodeInsertData(CodeInsertData insertData) {
        this.values         = CodeFunction.tryClone(insertData.values);
        this.updates        = CodeFunction.tryClone(insertData.updates);
        this.name           = CodeFunction.tryClone(insertData.name);
        this.lowPriority    = CodeFunction.tryClone(insertData.lowPriority);
    }


    public String asString() {
        StringBuilder builder = new StringBuilder();

        builder.append("INSERT ");
        if (lowPriority) {
            builder.append("DELAYED ");
        }
        builder.append("INTO ");
        builder.append(CodeFunction.toField(name));
        builder.append(' ');

        builder.append(CodeFunction.brackets(CodeFunction.toStringFromList(values.keySet(), (merge, field) -> {
            merge.append(CodeFunction.toField(field.name()));
        })));
        builder.append(" VALUES ");

        // 所有數值組合
        int                         firstValueLine  = 0;
        for (List<Object> valueList : values.values()) {
            firstValueLine = valueList.size();
            break;
        }
        if (firstValueLine == 0) {
            builder.append("()");
        } else {
            List<Map<Field, Object>>    valueMapList    = new ArrayList<>(firstValueLine);
            for (int index = 0 ; index < firstValueLine ; index++)
                valueMapList.add(new LinkedHashMap<>(values.size()));
            for (Map.Entry<Field, List<Object>> entry : values.entrySet())
                for (int index = 0 ; index < firstValueLine ; index++)
                    valueMapList.get(index).put(entry.getKey(), entry.getValue().get(index));
            builder.append(CodeFunction.toStringFromList(valueMapList, (merge, valueMap) -> {
                merge.append(CodeFunction.brackets(CodeFunction.toStringFromList(valueMap.entrySet(), ((mergeChild, entry) -> {
                    mergeChild.append(CodeFunction.toValue(entry.getKey(), entry.getValue()));
                }))));
            }));
        }

        if (updates.size() > 0) {
            builder.append(" ON DUPLICATE KEY UPDATE ");
            builder.append(CodeFunction.toStringFromMap(updates, ((merge, field, change) -> {
                switch (change.algorithm) {
                    case EQUAL:
                    default:
                        merge.append(CodeFunction.toField(field.name()));
                        merge.append('=');
                        merge.append(CodeFunction.toValue(field, change.value));
                        break;
                    case ADDITION:
                        merge.append(CodeFunction.toField(field.name()));
                        merge.append('=');
                        merge.append(CodeFunction.toField(field.name()));
                        merge.append('+');
                        merge.append(CodeFunction.toValue(field, change.value));
                        break;
                    case SUBTRACTION:
                        merge.append(CodeFunction.toField(field.name()));
                        merge.append('=');
                        merge.append(CodeFunction.toField(field.name()));
                        merge.append('-');
                        merge.append(CodeFunction.toValue(field, change.value));
                        break;
                }
            })));
        }

        return builder.toString();
    }

    public CodeInsertData clone() {
        return new CodeInsertData(this);
    }




    public <T> CodeInsertData insert(Field<T> field, T value) {
        values.computeIfAbsent(field, v -> new ArrayList<>()).add(value);
        return this;
    }

    public <T> CodeInsertData updates(Field<T> field, T value) {
        return updates(field, value, UpdateAlgorithm.EQUAL);
    }

    public <T> CodeInsertData updates(Field<T> field, T value, UpdateAlgorithm algorithm) {
        insert(field, value);
        updates.put(field, new Change<>(field, value, algorithm));
        return this;
    }


    public String name() {
        return name;
    }
}
