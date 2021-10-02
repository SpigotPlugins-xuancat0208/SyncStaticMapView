package xuan.cat.syncstaticmapview.database.sql;

import xuan.cat.syncstaticmapview.database.sql.builder.Field;
import xuan.cat.syncstaticmapview.database.sql.builder.Variable;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.*;
import java.util.*;


/**
 * SQL操作器
 */
public final class SQL {
    private         String      sql         = null; // SQL命令
    private final   Statement   statement;          // 資料庫控制庫
    private         ResultSet   resultSet   = null; // 返回結果控制庫
    private         int         amount      = -2;   // 查詢數量


    SQL(Statement statement) {
        this.statement = statement;
    }


    /**
     * 查詢數據庫資料
     * @return 是否成功
     * @param command SQL命令
     */
    public boolean Q(String command) throws SQLException {
        sql = command;
        return question();
    }
    /**
     * 查詢數據庫資料
     * @return 是否成功
     */
    public boolean Q() throws SQLException {
        return question();
    }
    /**
     * 查詢數據庫資料, 然後關閉
     * @return 是否成功
     */
    public boolean QC() throws SQLException {
        boolean r = question();
        C();
        return r;
    }

    /**
     * 查詢數據庫資料, 然後關閉
     * @return 是否成功
     * @param command SQL命令建構器
     */
    public boolean QC(SQLCommand command) throws SQLException {
        sql = command.toCommand();
        return QC();
    }


    /**
     * 查詢數據庫資料
     * @return 是否成功
     */
    public boolean question() throws SQLException {
        try {
            if (sql         == null)
                throw new NullPointerException(); // 不能為空值
            if (statement   == null)
                return false; // 不能為空值

            // 取得資料結果
            closeResultSet(); // 關閉返回結果控制庫
            resultSet = statement.executeQuery(sql); // 查詢

            // 取得資料數目
            resultSet.last(); // 跳到最後一行資料
            amount = resultSet.getRow(); // 取得行數
            resultSet.beforeFirst(); // 回歸初始行數

            return amount >= 1; // 返回是否有查詢到資料

        } catch (Exception ex) {
            // 出錯
            closeResultSet(); // 關閉返回結果控制庫
            amount = -1;
            throw ex;
        }
    }


    /**
     * 更新數據庫資料
     */
    public void U() throws SQLException {
        update();
    }
    /**
     * 更新數據庫資料, 然後關閉
     * @return 成功行數
     */
    public int UC() throws SQLException {
        int r = update();
        C();
        return r;
    }

    /**
     * 更新數據庫資料, 然後關閉
     * @param command SQL命令
     */
    public void
    UC(String command) throws SQLException {
        sql = command;
        UC();
    }


    /**
     * 更新數據庫資料
     * @return 成功行數
     */
    public int update() throws SQLException {
        try {
            if (sql       == null)
                throw new NullPointerException(); // 不能為空值
            if (statement == null)
                return -1; // 不能為空值

            closeResultSet(); // 關閉返回結果控制庫
            amount = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS); // 更新資料

            return amount; // 返回成功行數

        } catch (Exception ex) {
            // 出錯
            closeResultSet(); // 關閉返回結果控制庫
            amount = -1;
            throw ex;
        }
    }


    /**
     * 資料查詢結果跳下一行
     * @return 是否還擁有下一行
     */
    public boolean N() throws SQLException {
        return next();
    }
    /**
     * 資料查詢結果跳下一行
     * @return 是否還擁有下一行
     */
    public boolean next() throws SQLException {
        if (resultSet == null)
            return false; // 還沒有使用查詢過
        return resultSet.next();
    }


    /**
     * 取得欄位持有的資料
     * @param table 欄位
     * @param <T> 資料類型
     * @return 持有的資料
     */
    public <T> T get(Field<T> table) throws SQLException {
        return table.get(this);
    }
    /**
     * 取得欄位持有的資料
     * @param table 欄位
     * @param <T> 資料類型
     * @param def 預設值
     * @return 持有的資料
     */
    public <T> T get(Field<T> table, T def) throws SQLException {
        return table.get(this, def);
    }


    /**
     * 取得欄位持有的資料, 然後關閉
     * @param table 欄位
     * @param <T> 資料類型
     * @return 持有的資料
     */
    public <T> T getThenClose(Field<T> table) throws SQLException {
        return table.getThenClose(this);
    }


    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public Object getObject(String table) throws SQLException {
        return resultSet.getObject(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public String getString(String table) throws SQLException {
        return resultSet.getString(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public long getLong(String table) throws SQLException {
        return resultSet.getLong(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public int getInt(String table) throws SQLException {
        return resultSet.getInt(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public double getDouble(String table) throws SQLException {
        return resultSet.getDouble(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public float getFloat(String table) throws SQLException {
        return resultSet.getFloat(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public Time getTime(String table) throws SQLException {
        return resultSet.getTime(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public Timestamp getTimestamp(String table) throws SQLException {
        return resultSet.getTimestamp(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public boolean getBoolean(String table) throws SQLException {
        return resultSet.getBoolean(table);
    }
    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public int getBooleanByInt(String table) throws SQLException {
        if ( resultSet.getBoolean(table) ) {
            return 1;
        } else {
            return 0;
        }
    }
    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public boolean getBooleanFromString(String table) throws SQLException {
        return Boolean.valueOf(true).toString().equals(resultSet.getString(table));
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public byte getByte(String table) throws SQLException {
        return resultSet.getByte(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public short getShort(String table) throws SQLException {
        return resultSet.getShort(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public Date getDate(String table) throws SQLException {
        return resultSet.getDate(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public byte[] getBytes(String table) throws SQLException {
        return resultSet.getBytes(table);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public byte[] getHex(String table) throws SQLException {
        return Variable.HEX(resultSet.getString("HEX(`" + table + "`)"));
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public UUID getUUID(String table) throws SQLException {
        byte[]  data    = resultSet.getBytes(table);
        long    msb     = 0L;
        long    lsb     = 0L;
        int     read;
        for(read = 0; read < 8; ++read)
            msb = msb << 8 | (long)(data[read] & 255);
        for(read = 8; read < 16; ++read)
            lsb = lsb << 8 | (long)(data[read] & 255);
        return new UUID(msb, lsb);
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public <T extends Enum<T>> T getEnum(String table, T[] reference) throws SQLException {
        String value = resultSet.getString(table);
        if (value == null)
            return null;
        for (T r : reference)
            if (r.toString().equals(value))
                return r;
        return null;
    }

    /**
     * 不推薦使用
     * @deprecated
     */
    @Deprecated
    public <T extends Enum<T>> Set<T> getEnumSet(String table, T[] reference) throws SQLException {
        String value = resultSet.getString(table);
        if (value == null)
            return new HashSet<>(0);

        List<String>    split   = SQLTool.split(value, ',', StandardCharsets.UTF_8, 256);
        Set<T>          list    = new LinkedHashSet<>(split.size());

        for (String s : split)
            for (T r : reference)
                if (r.toString().equals(s))
                    list.add(r);

        return list;
    }


    /**
     * 設置SQL命令
     * @param command SQL命令
     */
    @Deprecated
    public void setSQL(String command) {
        sql = command;
    }
    /**
     * 設置SQL命令
     * @param command SQL命令建構器
     */
    public void setSQL(SQLCommand command) {
        sql = command.toCommand();
    }


    /**
     * 取得SQL命令
     * @return SQL命令,可能為null
     */
    @Deprecated
    public String getSQL() {
        return sql;
    }


    /**
     * 關閉連線,不可再進行數據庫操作
     * 不使用SQL時,一定要使用
     * 否則可能記憶體溢出
     */
    public void C() throws SQLException {
        close();
    }
    /**
     * 關閉連線,不可再進行數據庫操作
     * 不使用SQL時,一定要使用
     * 否則可能記憶體溢出
     */
    public void close() throws SQLException {
        CR();
        if (statement != null && !statement.isClosed())
            statement.close(); // 關閉連線
    }


    /**
     * 只關閉結果控制庫
     */
    public void CR() throws SQLException {
        closeResultSet();
    }
    /**
     * 只關閉結果控制庫
     */
    public void closeResultSet() throws SQLException {
        if (resultSet != null && !resultSet.isClosed())
            resultSet.close(); // 關閉查詢
        resultSet = null;
    }


    /**
     * return 取得自動遞增的值, 可能為 null
     */
    public Integer getAutoIncrementInt() throws SQLException {
        if (statement != null) {
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys != null && generatedKeys.next())
                return generatedKeys.getInt(1);
        }
        return null;
    }


    interface IndifferentExceptionHandle {
        void run() throws SQLException;
    }
}