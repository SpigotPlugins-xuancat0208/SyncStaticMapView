package xuan.cat.syncstaticmapview.code;

import xuan.cat.databasecatmini.api.Database;
import xuan.cat.databasecatmini.api.sql.*;
import xuan.cat.databasecatmini.api.sql.builder.*;
import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.data.MapData;
import xuan.cat.syncstaticmapview.code.data.CodeMapData;
import xuan.cat.syncstaticmapview.code.data.ConfigData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class MapDatabase {
    private final ConfigData            configData;
    private final BranchMapConversion   branchMapConversion;
    private final BranchMapColor        branchMapColor;


    interface TABLE {
        DatabaseTable table_MapData = Database.atTable("MAP_DATA");

        interface MAP_DATA {
            Field<Long>     field_MapID     = Database.atField(FieldStyle.INT_UNSIGNED, "MapID").autoIncrement(true);
            Field<byte[]>   field_MapPixels = Database.atField(FieldStyle.BLOB, "MapPixels");
            Field<Date>     field_SaveTime  = Database.atField(FieldStyle.DATETIME, "SaveTime");
        }
    }


    public MapDatabase(ConfigData configData, BranchMapConversion branchMapConversion, BranchMapColor branchMapColor) throws SQLException {
        this.configData             = configData;
        this.branchMapConversion    = branchMapConversion;
        this.branchMapColor         = branchMapColor;

        // LAND_GROUP 資料表
        if (!TABLE.table_MapData.existTable(configData.getDatabaseConnection())) {
            TABLE.table_MapData.createTable().collate(Collate.utf8mb4_bin).engine(DatabaseEngine.MyISAM)
                    .field(TABLE.MAP_DATA.field_MapID)
                    .field(TABLE.MAP_DATA.field_MapPixels)
                    .field(TABLE.MAP_DATA.field_SaveTime)
                    .partition(TABLE.MAP_DATA.field_MapID.partitionHash().rows(50))
                    .callSQL(configData.getDatabaseConnection())
                    .UC();
        }
    }


    public Integer addMapData(MapData mapData) throws SQLException {
        SQL sql = TABLE.table_MapData.insertData()
                .insert(TABLE.MAP_DATA.field_MapPixels, zipDataBytes(mapData.getPixels()))
                .insert(TABLE.MAP_DATA.field_SaveTime, Value.NOW_DATE)
                .callSQL(configData.getDatabaseConnection());
        sql.U();
        Integer incrementInt = sql.getAutoIncrementInt();
        sql.C();
        assert incrementInt != null;
        return incrementInt;
    }

    public MapData loadMapData(int mapId) throws SQLException {
        SQL sql = TABLE.table_MapData.selectData()
                .select(TABLE.MAP_DATA.field_MapPixels)
                .where(w -> w.and(TABLE.MAP_DATA.field_MapID, (long) mapId))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        if (sql.Q()) {
            sql.N();
            return new CodeMapData(branchMapColor, branchMapConversion, unzipDataBytes(sql.getThenClose(TABLE.MAP_DATA.field_MapPixels)));
        } else {
            return null;
        }
    }

    public boolean existMapData(int mapId) throws SQLException {
        SQL sql = TABLE.table_MapData.selectData()
                .select(TABLE.MAP_DATA.field_SaveTime)
                .where(w -> w.and(TABLE.MAP_DATA.field_MapID, (long) mapId))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.QC();
    }

    public boolean saveMapData(int mapId, MapData mapData) throws SQLException {
        SQL sql = TABLE.table_MapData.updateData()
                .updates(TABLE.MAP_DATA.field_MapPixels, zipDataBytes(mapData.getPixels()))
                .updates(TABLE.MAP_DATA.field_SaveTime, Value.NOW_DATE)
                .where(w -> w.and(TABLE.MAP_DATA.field_MapID, (long) mapId))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }

    public boolean removeMapData(int mapId) throws SQLException {
        SQL sql = TABLE.table_MapData.deleteData()
                .where(w -> w.and(TABLE.MAP_DATA.field_MapID, (long) mapId))
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }


    private byte[] zipDataBytes(byte[] bytes) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(output);
            gzip.write(bytes);
            gzip.close();
            return output.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new byte[0];
        }
    }
    private byte[] unzipDataBytes(byte[] data) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ByteArrayInputStream input = new ByteArrayInputStream(data);
            GZIPInputStream ungzip = new GZIPInputStream(input);
            byte[] buffer = new byte[1024];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                output.write(buffer, 0, n);
            }
            ungzip.close();
            input.close();
            return output.toByteArray();

        } catch (Exception ex) {
            ex.printStackTrace();
            return new byte[0];
        }
    }
}
