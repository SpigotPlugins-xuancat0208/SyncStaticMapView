package xuan.cat.syncstaticmapview.database.code.sql;

import xuan.cat.syncstaticmapview.database.api.sql.SQL;
import xuan.cat.syncstaticmapview.database.api.sql.SQLBuilder;
import xuan.cat.syncstaticmapview.database.api.sql.builder.Field;
import xuan.cat.syncstaticmapview.database.code.sql.builder.CodeFunction;
import xuan.cat.syncstaticmapview.database.code.sql.builder.CodeVariable;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.*;
import java.util.*;


/**
 * 資料庫查詢
 */
public final class CodeSQL implements SQL {
    private         String      sql         = null; // SQL命令
    private final   Statement   statement;          // 資料庫控制庫
    private         ResultSet   resultSet   = null; // 返回結果控制庫
    private         int         amount      = -2;   // 查詢數量


    CodeSQL(Statement statement) {
        this.statement = statement;
    }


    public boolean Q(String sqlCmd) throws SQLException {
        sql = sqlCmd;
        return question();
    }
    public boolean Q() throws SQLException {
        return question();
    }
    public boolean QC() throws SQLException {
        boolean r = question();
        C();
        return r;
    }

    public boolean Q(SQLBuilder sqlBuilder) throws SQLException {
        sql = sqlBuilder.asString();
        return question();
    }

    public boolean QC(SQLBuilder sqlBuilder) throws SQLException {
        sql = sqlBuilder.asString();
        return QC();
    }


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


    public void U() throws SQLException {
        update();
    }
    public int UC() throws SQLException {
        int r = update();
        C();
        return r;
    }

    public void UC(String sqlCmd) throws SQLException {
        sql = sqlCmd;
        UC();
    }


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


    public boolean N() throws SQLException {
        return next();
    }
    public boolean next() throws SQLException {
        if (resultSet == null)
            return false; // 還沒有使用查詢過
        return resultSet.next();
    }


    public <T> T get(Field<T> table) throws SQLException {
        return table.get(this);
    }
    public <T> T get(Field<T> table, T def) throws SQLException {
        return table.get(this, def);
    }


    public <T> T getThenClose(Field<T> table) throws SQLException {
        return table.getThenClose(this);
    }


    public Object getObject(String table) throws SQLException {
        return resultSet.getObject(table);
    }

    public String getString(String table) throws SQLException {
        return resultSet.getString(table);
    }

    public long getLong(String table) throws SQLException {
        return resultSet.getLong(table);
    }

    public int getInt(String table) throws SQLException {
        return resultSet.getInt(table);
    }

    public double getDouble(String table) throws SQLException {
        return resultSet.getDouble(table);
    }

    public float getFloat(String table) throws SQLException {
        return resultSet.getFloat(table);
    }

    public Time getTime(String table) throws SQLException {
        return resultSet.getTime(table);
    }

    public Timestamp getTimestamp(String table) throws SQLException {
        return resultSet.getTimestamp(table);
    }

    public boolean getBoolean(String table) throws SQLException {
        return resultSet.getBoolean(table);
    }
    public int getBooleanByInt(String table) throws SQLException {
        if ( resultSet.getBoolean(table) ) {
            return 1;
        } else {
            return 0;
        }
    }
    public boolean getBooleanFromString(String table) throws SQLException {
        return Boolean.valueOf(true).toString().equals(resultSet.getString(table));
    }

    public byte getByte(String table) throws SQLException {
        return resultSet.getByte(table);
    }

    public short getShort(String table) throws SQLException {
        return resultSet.getShort(table);
    }

    public Date getDate(String table) throws SQLException {
        return resultSet.getDate(table);
    }

    public byte[] getBytes(String table) throws SQLException {
        return resultSet.getBytes(table);
    }

    public byte[] getHex(String table) throws SQLException {
        return CodeVariable.HEX(resultSet.getString("HEX(`" + table + "`)"));
    }

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

    public <T extends Enum<T>> T getEnum(String table, T[] reference) throws SQLException {
        String value = resultSet.getString(table);
        if (value == null)
            return null;
        for (T r : reference)
            if (r.toString().equals(value))
                return r;
        return null;
    }

    public <T extends Enum<T>> Set<T> getEnumSet(String table, T[] reference) throws SQLException {
        String value = resultSet.getString(table);
        if (value == null)
            return new HashSet<>(0);

        List<String>    split   = CodeFunction.split(value, ',', StandardCharsets.UTF_8, 256);
        Set<T>          list    = new LinkedHashSet<>(split.size());

        for (String s : split)
            for (T r : reference)
                if (r.toString().equals(s))
                    list.add(r);

        return list;
    }


    public void setSQL(String sqlCmd) {
        sql = sqlCmd;
    }
    public void setSQL(SQLBuilder sqlBuilder) {
        sql = sqlBuilder.asString();
    }


    public String getSQL() {
        return sql;
    }


    public void C() throws SQLException {
        close();
    }
    public void close() throws SQLException {
        CR();
        if (statement != null && !statement.isClosed())
            statement.close(); // 關閉連線
    }


    public void CR() throws SQLException {
        closeResultSet();
    }
    public void closeResultSet() throws SQLException {
        if (resultSet != null && !resultSet.isClosed())
            resultSet.close(); // 關閉查詢
        resultSet = null;
    }


    public Integer getAutoIncrementInt() throws SQLException {
        if (statement != null) {
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys != null && generatedKeys.next())
                return generatedKeys.getInt(1);
        }
        return null;
    }


}