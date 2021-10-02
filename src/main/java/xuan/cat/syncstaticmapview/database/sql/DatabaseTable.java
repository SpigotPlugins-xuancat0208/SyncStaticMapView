package xuan.cat.syncstaticmapview.database.sql;

import xuan.cat.syncstaticmapview.database.sql.builder.*;

import java.sql.SQLException;

/**
 * 資料表
 */
public final class DatabaseTable {
    private final String name;


    public DatabaseTable(String name) {
        this.name   = name;
    }


    /**
     * @return 資料表名稱
     */
    public String getName() {
        return name;
    }


    /**
     * @return 資料表是否存在
     */
    public boolean existTable(DatabaseConnection database) throws SQLException {
        Field<String>   tableName   = new Field<>(FieldStyle.TEXT, "TABLE_NAME");
        SQL             sql         = database.createSQL();
        return sql.QC(new SelectData(InformationSchema.TABLES).select(tableName).where(w -> w.and(new Field<>(FieldStyle.TEXT, "TABLE_SCHEMA"), database.getName()).and(tableName, name)).limit(1));
    }


    /**
     * @return 更改資料表
     */
    public AlterTable alterTable() {
        return new AlterTable(this);
    }

    /**
     * @return 創建資料表
     */
    public CreateTable createTable() {
        return new CreateTable(this);
    }


    /**
     * @return 刪除資料
     */
    public DeleteData deleteData() {
        return new DeleteData(this);
    }

    /**
     * @return 插入資料
     */
    public InsertData insertData() {
        return new InsertData(this);
    }

    /**
     * @return 選擇資料
     */
    public SelectData selectData() {
        return new SelectData(this);
    }

    /**
     * @return 更新資料
     */
    public UpdateData updateData() {
        return new UpdateData(this);
    }
}
