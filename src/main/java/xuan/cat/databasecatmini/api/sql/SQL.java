package xuan.cat.databasecatmini.api.sql;

import xuan.cat.databasecatmini.api.sql.builder.Field;

import java.sql.*;
import java.util.Set;
import java.util.UUID;

/**
 * SQL操作器
 */
public interface SQL {
    /**
     * 查詢數據庫資料
     * @return 是否成功
     */
    boolean Q() throws SQLException;
    /**
     * 查詢數據庫資料
     * @return 是否成功
     * @param sqlCmd SQL命令
     */
    boolean Q(String sqlCmd) throws SQLException;
    /**
     * 查詢數據庫資料
     * @return 是否成功
     * @param sqlBuilder SQL命令建構器
     */
    boolean Q(SQLBuilder sqlBuilder) throws SQLException;
    /**
     * 查詢數據庫資料
     * @return 是否成功
     */
    boolean question() throws SQLException;
    /**
     * 查詢數據庫資料
     * @return 是否成功
     * @param sqlCmd SQL命令
     */
    boolean question(String sqlCmd) throws SQLException;
    /**
     * 查詢數據庫資料
     * @return 是否成功
     * @param sqlBuilder SQL命令建構器
     */
    boolean question(SQLBuilder sqlBuilder) throws SQLException;


    /**
     * 查詢數據庫資料, 然後關閉
     * @return 是否成功
     */
    boolean QC() throws SQLException;
    /**
     * 查詢數據庫資料, 然後關閉
     * @return 是否成功
     * @param sqlCmd SQL命令
     */
    boolean QC(String sqlCmd) throws SQLException;
    /**
     * 查詢數據庫資料, 然後關閉
     * @return 是否成功
     * @param sqlBuilder SQL命令建構器
     */
    boolean QC(SQLBuilder sqlBuilder) throws SQLException;


    /**
     * 查詢數據庫資料, 然後資料查詢結果跳下一行
     * @return 是否成功
     */
    boolean QL() throws SQLException;
    /**
     * 查詢數據庫資料, 然後資料查詢結果跳下一行
     * @return 是否成功
     * @param sqlCmd SQL命令
     */
    boolean QL(String sqlCmd) throws SQLException;
    /**
     * 查詢數據庫資料, 然後資料查詢結果跳下一行
     * @return 是否成功
     * @param sqlBuilder SQL命令建構器
     */
    boolean QL(SQLBuilder sqlBuilder) throws SQLException;


    /**
     * 更新數據庫資料
     * @return 成功行數
     */
    int U() throws SQLException;
    /**
     * 更新數據庫資料
     * @return 成功行數
     * @param sqlCmd SQL命令
     */
    int U(String sqlCmd) throws SQLException;
    /**
     * 更新數據庫資料
     * @return 成功行數
     * @param sqlBuilder SQL命令建構器
     */
    int U(SQLBuilder sqlBuilder) throws SQLException;
    /**
     * 更新數據庫資料
     * @return 成功行數
     */
    int update() throws SQLException;
    /**
     * 更新數據庫資料
     * @return 成功行數
     * @param sqlCmd SQL命令
     */
    int update(String sqlCmd) throws SQLException;
    /**
     * 更新數據庫資料
     * @return 成功行數
     * @param sqlBuilder SQL命令建構器
     */
    int update(SQLBuilder sqlBuilder) throws SQLException;


    /**
     * 更新數據庫資料, 然後關閉
     * @return 成功行數
     */
    int UC() throws SQLException;
    /**
     * 更新數據庫資料, 然後關閉
     * @return 成功行數
     * @param sqlCmd SQL命令
     */
    int UC(String sqlCmd) throws SQLException;
    /**
     * 更新數據庫資料, 然後關閉
     * @return 成功行數
     * @param sqlBuilder SQL命令建構器
     */
    int UC(SQLBuilder sqlBuilder) throws SQLException;


    /**
     * 更新數據庫資料, 然後資料查詢結果跳下一行
     * @return 成功行數
     */
    int UL() throws SQLException;
    /**
     * 更新數據庫資料, 然後資料查詢結果跳下一行
     * @return 成功行數
     * @param sqlCmd SQL命令
     */
    int UL(String sqlCmd) throws SQLException;
    /**
     * 更新數據庫資料, 然後資料查詢結果跳下一行
     * @return 成功行數
     * @param sqlBuilder SQL命令建構器
     */
    int UL(SQLBuilder sqlBuilder) throws SQLException;


    /**
     * 取得{@link ResultSet}以供資料取值用
     * @return SQL查詢結果,可能為null
     */
    ResultSet R();
    /**
     * 取得{@link ResultSet}以供資料取值用
     * @return SQL查詢結果,可能為null
     */
    ResultSet result();


    /**
     * 取得符合行數
     * @return 符合行數,失敗則-1
     */
    int A();
    /**
     * 取得符合行數
     * @return 符合行數,失敗則-1
     */
    int amount();


    /**
     * 資料查詢結果跳下一行
     * @return 是否還擁有下一行
     */
    boolean N() throws SQLException;
    /**
     * 資料查詢結果跳下一行
     * @return 是否還擁有下一行
     */
    boolean next() throws SQLException;


    /**
     * 資料查詢結果跳最初行
     */
    void B() throws SQLException;
    /**
     * 資料查詢結果跳最初行
     */
    void before() throws SQLException;


    /**
     * 取得欄位持有的資料
     * @param table 欄位
     * @param <T> 資料類型
     * @return 持有的資料
     */
    <T> T G(Field<T> table) throws SQLException;
    /**
     * 取得欄位持有的資料
     * @param table 欄位
     * @param <T> 資料類型
     * @param def 預設值
     * @return 持有的資料
     */
    <T> T G(Field<T> table, T def) throws SQLException;
    /**
     * 取得欄位持有的資料
     * @param table 欄位
     * @param <T> 資料類型
     * @return 持有的資料
     */
    <T> T get(Field<T> table) throws SQLException;
    /**
     * 取得欄位持有的資料
     * @param table 欄位
     * @param <T> 資料類型
     * @param def 預設值
     * @return 持有的資料
     */
    <T> T get(Field<T> table, T def) throws SQLException;


    /**
     * 取得欄位持有的資料, 然後關閉
     * @param table 欄位
     * @param <T> 資料類型
     * @return 持有的資料
     */
    <T> T GC(Field<T> table) throws SQLException;
    /**
     * 取得欄位持有的資料, 然後關閉
     * @param table 欄位
     * @param <T> 資料類型
     * @param def 預設值
     * @return 持有的資料
     */
    <T> T GC(Field<T> table, T def) throws SQLException;
    /**
     * 取得欄位持有的資料, 然後關閉
     * @param table 欄位
     * @param <T> 資料類型
     * @return 持有的資料
     */
    <T> T getThenClose(Field<T> table) throws SQLException;
    /**
     * 取得欄位持有的資料, 然後關閉
     * @param table 欄位
     * @param <T> 資料類型
     * @param def 預設值
     * @return 持有的資料
     */
    <T> T getThenClose(Field<T> table, T def) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    Object getObject(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    String getString(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    long getLong(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    int getInt(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    double getDouble(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    float getFloat(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    Time getTime(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    Timestamp getTimestamp(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    boolean getBoolean(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    int getBooleanByInt(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    boolean getBooleanFromString(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    byte getByte(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    short getShort(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    Date getDate(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    byte[] getBytes(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    byte[] getHex(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    UUID getUUID(String field) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    <T extends Enum<T>> T getEnum(String field, T[] reference) throws SQLException;

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    <T extends Enum<T>> Set<T> getEnumSet(String field, T[] reference) throws SQLException;




    /**
     * 設置SQL命令
     * @param sqlCmd SQL命令
     */
    @Deprecated
    void setSQL(String sqlCmd);
    void setSQL(SQLBuilder sqlBuilder);
    /**
     * 取得SQL命令
     * @return SQL命令,可能為null
     */
    @Deprecated
    String getSQL();


    /**
     * 關閉連線,不可再進行數據庫操作
     * 不使用SQL時,一定要使用
     * 否則可能記憶體溢出
     */
    void C() throws SQLException;
    void close() throws SQLException;


    /**
     * 釋放回傳資料
     */
    void L(); // 快速使用
    void liberate();

    /**
     * 只關閉結果控制庫
     */
    void CR() throws SQLException;
    void closeResultSet() throws SQLException;

    boolean isAvailable() throws SQLException;


    /**
     * return 取得自動遞增的值, 可能為 null
     */
    Integer getAutoIncrementInt() throws SQLException;
    /**
     * return 取得自動遞增的值, 可能為 null
     */
    Long getAutoIncrementLong() throws SQLException;


    /**
     * @param handle 不受理例外錯誤回傳
     */
    static void IE(IndifferentExceptionHandle handle) {
        indifferentException(handle);
    }
    static void indifferentException(IndifferentExceptionHandle handle) {
        try {
            handle.run();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    interface IndifferentExceptionHandle {
        void run() throws SQLException;
    }
}