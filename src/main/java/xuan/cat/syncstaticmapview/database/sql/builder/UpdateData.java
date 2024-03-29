package xuan.cat.syncstaticmapview.database.sql.builder;

import xuan.cat.syncstaticmapview.database.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.sql.SQLCommand;
import xuan.cat.syncstaticmapview.database.sql.SQLTool;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 更新資料
 */
public final class UpdateData implements SQLCommand {
    private final String                name;
    private       Where                 where       = null;
    private       Integer               limit       = null;
    private final Map<Field, Change>    updates;            // 欄位對應值清單


    private static class Change<T> {
        Change(Field field, Object value, UpdateAlgorithm algorithm) {
            this.field      = field;
            this.value      = value;
            this.algorithm  = algorithm;
        }

        public Field            field;
        public Object           value;
        public UpdateAlgorithm  algorithm;
    }


    public UpdateData(DatabaseTable table) {
        this.name           = table.getName();
        this.updates        = new HashMap<>();
    }
    private UpdateData(UpdateData updateData) {
        this.name           = SQLTool.tryClone(updateData.name);
        this.where          = SQLTool.tryClone(updateData.where);
        this.limit          = SQLTool.tryClone(updateData.limit);
        this.updates        = SQLTool.tryClone(updateData.updates);
    }


    public String toCommand() {
        StringBuilder builder = new StringBuilder();

        builder.append("UPDATE ");
        builder.append(SQLTool.toField(name));

        builder.append(" SET ");
        builder.append(SQLTool.toStringFromMap(updates, (merge, field, change) -> {
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
        }));
        builder.append(' ');

        if (where != null)
            builder.append(where.part());

        if (limit != null) {
            builder.append(" LIMIT ");
            builder.append(limit);
        }

        return builder.toString();
    }

    public UpdateData clone() {
        return new UpdateData(this);
    }

    public UpdateData where(Consumer<Where> consumer) {
        if (this.where == null)
            this.where = new Where();
        consumer.accept(this.where);
        return this;
    }

    public UpdateData limit(Integer limit) {
        this.limit = limit;
        return this;
    }


    public <T> UpdateData update(Field<T> field, T value) {
        return update(field, value, UpdateAlgorithm.EQUAL);
    }
    public <T extends Number> UpdateData updateIncrease(Field<T> field, T value) {
        return update(field, value, UpdateAlgorithm.INCREASE);
    }
    public <T extends Number> UpdateData updateSubtract(Field<T> field, T value) {
        return update(field, value, UpdateAlgorithm.SUBTRACT);
    }
    public <T> UpdateData update(Field<T> field, T value, UpdateAlgorithm algorithm) {
        updates.put(field, new Change<>(field, value, algorithm));
        return this;
    }
}
