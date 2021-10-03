package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.sql.SQLCommand;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.util.*;

/**
 * 插入資料
 */
public final class InsertData implements SQLCommand {
    private final Map<Field, List<Object>>  values;            // 欄位對應值清單
    private final Map<Field, Change>        updates;           // 欄位對應值清單
    private final String                    name;


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


    public InsertData(DatabaseTable table) {
        this.values         = new HashMap<>();
        this.updates        = new HashMap<>();
        this.name           = table.getName();
    }
    private InsertData(InsertData insertData) {
        this.values         = SQLTool.tryClone(insertData.values);
        this.updates        = SQLTool.tryClone(insertData.updates);
        this.name           = SQLTool.tryClone(insertData.name);
    }


    public String toCommand() {
        StringBuilder builder = new StringBuilder();

        builder.append("INSERT ");
        builder.append("INTO ");
        builder.append(SQLTool.toField(name));
        builder.append(' ');

        builder.append(SQLTool.brackets(SQLTool.toStringFromList(values.keySet(), (merge, field) -> {
            merge.append(SQLTool.toField(field.name()));
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
            builder.append(SQLTool.toStringFromList(valueMapList, (merge, valueMap) -> {
                merge.append(SQLTool.brackets(SQLTool.toStringFromList(valueMap.entrySet(), ((mergeChild, entry) -> {
                    mergeChild.append(SQLTool.toValue(entry.getKey(), entry.getValue()));
                }))));
            }));
        }

        if (updates.size() > 0) {
            builder.append(" ON DUPLICATE KEY UPDATE ");
            builder.append(SQLTool.toStringFromMap(updates, ((merge, field, change) -> {
                switch (change.algorithm) {
                    case EQUAL:
                    default:
                        merge.append(SQLTool.toField(field.name()));
                        merge.append('=');
                        merge.append(SQLTool.toValue(field, change.value));
                        break;
                    case INCREASE:
                        merge.append(SQLTool.toField(field.name()));
                        merge.append('=');
                        merge.append(SQLTool.toField(field.name()));
                        merge.append('+');
                        merge.append(SQLTool.toValue(field, change.value));
                        break;
                    case SUBTRACT:
                        merge.append(SQLTool.toField(field.name()));
                        merge.append('=');
                        merge.append(SQLTool.toField(field.name()));
                        merge.append('-');
                        merge.append(SQLTool.toValue(field, change.value));
                        break;
                }
            })));
        }

        return builder.toString();
    }

    public InsertData clone() {
        return new InsertData(this);
    }




    public <T> InsertData insert(Field<T> field, T value) {
        values.computeIfAbsent(field, v -> new ArrayList<>()).add(value);
        return this;
    }

    public <T> InsertData insertOrUpdate(Field<T> field, T value) {
        return insertOrUpdate(field, value, UpdateAlgorithm.EQUAL);
    }
    public <T> InsertData insertOrUpdate(Field<T> field, T value, UpdateAlgorithm algorithm) {
        insert(field, value);
        updates.put(field, new Change<>(field, value, algorithm));
        return this;
    }
}
