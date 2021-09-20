package xuan.cat.syncstaticmapview.database.api.sql;

import xuan.cat.syncstaticmapview.database.api.sql.builder.*;

import java.sql.SQLException;

/**
 * 資料表
 */
public interface DatabaseTable {
    /**
     * @return 資料表名稱
     */
    String getName();

    /**
     * @return 資料表是否存在
     */
    boolean existTable(DatabaseConnection database) throws SQLException;

    /**
     * @return 創建資料表
     */
    CreateTable createTable();

    /**
     * @return 刪除資料
     */
    DeleteData deleteData();

    /**
     * @return 插入資料
     */
    InsertData insertData();

    /**
     * @return 選擇資料
     */
    SelectData selectData();
    /**
     * @return 選擇訊息架構資料
     */
    SelectData selectData(InformationSchema informationSchema);

    /**
     * @return 更新資料
     */
    UpdateData updateData();
}
