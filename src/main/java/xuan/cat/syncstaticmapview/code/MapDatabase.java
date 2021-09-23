package xuan.cat.syncstaticmapview.code;

import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.data.MapData;
import xuan.cat.syncstaticmapview.api.data.MapRedirect;
import xuan.cat.syncstaticmapview.code.data.CodeMapData;
import xuan.cat.syncstaticmapview.code.data.ConfigData;
import xuan.cat.syncstaticmapview.code.data.MapRedirectEntry;
import xuan.cat.syncstaticmapview.database.api.Database;
import xuan.cat.syncstaticmapview.database.api.sql.DatabaseTable;
import xuan.cat.syncstaticmapview.database.api.sql.SQL;
import xuan.cat.syncstaticmapview.database.api.sql.builder.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class MapDatabase {
    private final ConfigData            configData;
    private final BranchMapConversion   branchMapConversion;
    private final BranchMapColor        branchMapColor;


    interface TABLE {
        DatabaseTable table_MapData = Database.atTable("MAP_DATA");

        interface MAP_DATA {
            Field<Long>     field_MapID         = Database.atField(FieldStyle.INT_UNSIGNED, "MapID").autoIncrement(true);
            Field<byte[]>   field_MapPixels     = Database.atField(FieldStyle.BLOB, "MapPixels");
            Field<Date>     field_SaveTime      = Database.atField(FieldStyle.DATETIME, "SaveTime");
            Field<Long>     field_UploaderID    = Database.atField(FieldStyle.INT_UNSIGNED, "UploaderID").defaultValue(0L);
        }

        DatabaseTable table_MapRedirect = Database.atTable("MAP_REDIRECTS");
        interface MAP_REDIRECT {
            Field<Long>     field_MapID         = Database.atField(FieldStyle.INT_UNSIGNED, "MapID");
            Field<Integer>  field_Priority      = Database.atField(FieldStyle.INT, "Priority");
            Field<String>   field_Permission    = Database.atField(FieldStyle.VARCHAR, "Permission").length(255).collate(Collate.utf8mb4_bin);
            Field<Long>     field_RedirectID    = Database.atField(FieldStyle.INT_UNSIGNED, "RedirectID");
        }

        DatabaseTable table_MapUpdate = Database.atTable("MAP_UPDATE");
        interface MAP_UPDATE {
            Field<Long>     field_MapID         = Database.atField(FieldStyle.INT_UNSIGNED, "MapID");
            Field<Date>     field_TimeMark      = Database.atField(FieldStyle.DATETIME, "TimeMark");
        }

        DatabaseTable table_MapStatistics = Database.atTable("MAP_STATISTICS");
        interface MAP_STATISTICS {
            Field<Long>     field_PlayerID      = Database.atField(FieldStyle.INT_UNSIGNED, "PlayerID").autoIncrement(true);
            Field<UUID>     field_PlayerUUID    = Database.atField(FieldStyle.UUID, "PlayerUUID");
            Field<Long>     field_Used          = Database.atField(FieldStyle.INT_UNSIGNED, "Used");
            Field<Long>     field_Capacity      = Database.atField(FieldStyle.INT_UNSIGNED, "Capacity");
        }
    }


    public MapDatabase(ConfigData configData, BranchMapConversion branchMapConversion, BranchMapColor branchMapColor) throws SQLException {
        this.configData             = configData;
        this.branchMapConversion    = branchMapConversion;
        this.branchMapColor         = branchMapColor;

        // MAP_DATA 資料表
        if (!TABLE.table_MapData.existTable(configData.getDatabaseConnection())) {
            TABLE.table_MapData.createTable().collate(Collate.utf8mb4_bin).engine(DatabaseEngine.MyISAM)
                    .field(TABLE.MAP_DATA.field_MapID)
                    .field(TABLE.MAP_DATA.field_MapPixels)
                    .field(TABLE.MAP_DATA.field_SaveTime)
                    .field(TABLE.MAP_DATA.field_UploaderID)
                    .partition(configData.isCreateTableCanPartition() ? TABLE.MAP_DATA.field_MapID.partitionKey().rows(20) : null)
                    .callSQL(configData.getDatabaseConnection())
                    .UC();
        } else {
            try {
                TABLE.table_MapData.alterTable().tableAdd(TABLE.MAP_DATA.field_UploaderID).callSQL(configData.getDatabaseConnection()).UC();
            } catch (SQLException ignored) {
            }
        }

        // MAP_PERMISSION 資料表
        if (!TABLE.table_MapRedirect.existTable(configData.getDatabaseConnection())) {
            TABLE.table_MapRedirect.createTable().collate(Collate.utf8mb4_bin).engine(DatabaseEngine.MyISAM)
                    .field(TABLE.MAP_REDIRECT.field_MapID)
                    .field(TABLE.MAP_REDIRECT.field_Priority)
                    .field(TABLE.MAP_REDIRECT.field_Permission)
                    .field(TABLE.MAP_REDIRECT.field_RedirectID)
                    .index(TABLE.MAP_REDIRECT.field_MapID.index().type(IndexType.INDEX))
                    .partition(configData.isCreateTableCanPartition() ? TABLE.MAP_REDIRECT.field_MapID.partitionKey().rows(4) : null)
                    .callSQL(configData.getDatabaseConnection())
                    .UC();
        }

        // MAP_UPDATE 資料表
        if (!TABLE.table_MapUpdate.existTable(configData.getDatabaseConnection())) {
            TABLE.table_MapUpdate.createTable().collate(Collate.utf8mb4_bin).engine(DatabaseEngine.MEMORY)
                    .field(TABLE.MAP_UPDATE.field_MapID)
                    .field(TABLE.MAP_UPDATE.field_TimeMark)
                    .index(TABLE.MAP_UPDATE.field_MapID.index().type(IndexType.UNIQUE))
                    .callSQL(configData.getDatabaseConnection())
                    .UC();
        }

        // MAP_STATISTICS 資料表
        if (!TABLE.table_MapStatistics.existTable(configData.getDatabaseConnection())) {
            TABLE.table_MapStatistics.createTable().collate(Collate.utf8mb4_bin).engine(DatabaseEngine.MyISAM)
                    .field(TABLE.MAP_STATISTICS.field_PlayerID)
                    .field(TABLE.MAP_STATISTICS.field_PlayerUUID)
                    .field(TABLE.MAP_STATISTICS.field_Used)
                    .field(TABLE.MAP_STATISTICS.field_Capacity)
                    .index(TABLE.MAP_STATISTICS.field_PlayerUUID.index().type(IndexType.UNIQUE))
                    .callSQL(configData.getDatabaseConnection())
                    .UC();
        } else {
            try {
                TABLE.table_MapStatistics.alterTable().tableAdd(TABLE.MAP_STATISTICS.field_PlayerID.clone().isFirst(true)).callSQL(configData.getDatabaseConnection()).UC();
            } catch (SQLException ignored) {
            }
        }
    }


//    public Integer addMapData(MapData mapData) throws SQLException {
//        SQL sql = TABLE.table_MapData.insertData()
//                .insert(TABLE.MAP_DATA.field_MapPixels, zipDataBytes(mapData.getPixels()))
//                .insert(TABLE.MAP_DATA.field_SaveTime, Value.NOW_DATE)
//                .callSQL(configData.getDatabaseConnection());
//        sql.U();
//        Integer incrementInt = sql.getAutoIncrementInt();
//        sql.C();
//        assert incrementInt != null;
//        return incrementInt;
//    }
    public Integer addMapData(MapData mapData, int uploaderId) throws SQLException {
        SQL sql = TABLE.table_MapData.insertData()
                .insert(TABLE.MAP_DATA.field_MapPixels, zipDataBytes(mapData.getPixels()))
                .insert(TABLE.MAP_DATA.field_UploaderID, (long) uploaderId)
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
            sql.C();
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

    public boolean setMapUploaderID(int mapId, int uploaderId) throws SQLException {
        SQL sql = TABLE.table_MapData.updateData()
                .updates(TABLE.MAP_DATA.field_UploaderID, (long) uploaderId)
                .where(w -> w.and(TABLE.MAP_DATA.field_MapID, (long) mapId))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }
    public int getMapUploaderID(int mapId) throws SQLException {
        SQL sql = TABLE.table_MapData.selectData()
                .select(TABLE.MAP_DATA.field_UploaderID)
                .where(w -> w.and(TABLE.MAP_DATA.field_MapID, (long) mapId))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        if (sql.Q()) {
            sql.N();
            return sql.getThenClose(TABLE.MAP_DATA.field_UploaderID).intValue();
        } else {
            sql.C();
            return -2;
        }
    }


    public List<MapRedirect> getMapRedirects(int mapId) throws SQLException {
        List<MapRedirect> list = new ArrayList<>();
        SQL sql = TABLE.table_MapRedirect.selectData()
                .select(TABLE.MAP_REDIRECT.field_Priority)
                .select(TABLE.MAP_REDIRECT.field_Permission)
                .select(TABLE.MAP_REDIRECT.field_RedirectID)
                .where(w -> w.and(TABLE.MAP_REDIRECT.field_MapID, (long) mapId))
                .callSQL(configData.getDatabaseConnection());
        if (sql.Q()) {
            while (sql.N()) {
                list.add(new MapRedirectEntry(sql.get(TABLE.MAP_REDIRECT.field_Priority), sql.get(TABLE.MAP_REDIRECT.field_Permission), sql.get(TABLE.MAP_REDIRECT.field_RedirectID).intValue()));
            }
        }
        sql.C();
        list.sort(Comparator.comparingInt(MapRedirect::getPriority));
        return list;
    }

    public boolean existMapRedirects(int mapId, String permission) throws SQLException {
        SQL sql = TABLE.table_MapRedirect.selectData()
                .select(TABLE.MAP_REDIRECT.field_Priority)
                .where(w -> w.and(TABLE.MAP_REDIRECT.field_MapID, (long) mapId).and(TABLE.MAP_REDIRECT.field_Permission, permission))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.QC();
    }
    public boolean existMapRedirects(int mapId, int priority) throws SQLException {
        SQL sql = TABLE.table_MapRedirect.selectData()
                .select(TABLE.MAP_REDIRECT.field_Priority)
                .where(w -> w.and(TABLE.MAP_REDIRECT.field_MapID, (long) mapId).and(TABLE.MAP_REDIRECT.field_Priority, priority))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.QC();
    }

    public boolean addMapRedirects(int mapId, MapRedirect permissionEntry) throws SQLException {
        SQL sql = TABLE.table_MapRedirect.insertData()
                .insert(TABLE.MAP_REDIRECT.field_MapID, (long) mapId)
                .insert(TABLE.MAP_REDIRECT.field_Permission, permissionEntry.getPermission())
                .insert(TABLE.MAP_REDIRECT.field_Priority, permissionEntry.getPriority())
                .insert(TABLE.MAP_REDIRECT.field_RedirectID, (long) permissionEntry.getRedirectId())
                .callSQL(configData.getDatabaseConnection());
         return sql.UC() > 0;
    }

    public boolean removeMapRedirect(int mapId, String permission) throws SQLException {
        SQL sql = TABLE.table_MapRedirect.deleteData()
                .where(w -> w.and(TABLE.MAP_REDIRECT.field_MapID, (long) mapId).and(TABLE.MAP_REDIRECT.field_Permission, permission))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }
    public boolean removeMapRedirect(int mapId, int priority) throws SQLException {
        SQL sql = TABLE.table_MapRedirect.deleteData()
                .where(w -> w.and(TABLE.MAP_REDIRECT.field_MapID, (long) mapId).and(TABLE.MAP_REDIRECT.field_Priority, priority))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }

    public boolean removeMapRedirect(int mapId) throws SQLException {
        SQL sql = TABLE.table_MapRedirect.deleteData()
                .where(w -> w.and(TABLE.MAP_REDIRECT.field_MapID, (long) mapId))
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }



    public void expiredMapUpdate() throws SQLException {
        SQL sql = TABLE.table_MapUpdate.deleteData()
                .where(w -> w.and(TABLE.MAP_UPDATE.field_TimeMark, WhereJudge.LESS, Value.NOW_DATE))
                .callSQL(configData.getDatabaseConnection());
        sql.UC();
    }
    public boolean markMapUpdate(int mapId) throws SQLException {
        SQL sql = TABLE.table_MapUpdate.insertData()
                .insert(TABLE.MAP_UPDATE.field_MapID, (long) mapId)
                .insert(TABLE.MAP_UPDATE.field_TimeMark, new Value.NOW_TIME_ADD(configData.getCacheVitalityTime() * 2))
                .updates(TABLE.MAP_UPDATE.field_MapID, (long) mapId)
                .updates(TABLE.MAP_UPDATE.field_TimeMark, new Value.NOW_TIME_ADD(configData.getCacheVitalityTime() * 2))
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }
    public Map<Integer, Date> getMapUpdates() throws SQLException {
        Map<Integer, Date> map = new HashMap<>();
        SQL sql = TABLE.table_MapUpdate.selectData()
                .select(TABLE.MAP_UPDATE.field_MapID)
                .select(TABLE.MAP_UPDATE.field_TimeMark)
                .callSQL(configData.getDatabaseConnection());
        if (sql.Q()) {
            while (sql.N()) {
                map.put(sql.get(TABLE.MAP_UPDATE.field_MapID).intValue(), sql.get(TABLE.MAP_UPDATE.field_TimeMark));
            }
        }
        sql.C();
        return map;
    }


    public boolean createStatistics(UUID playerUUID, int capacity, int used) throws SQLException {
        SQL sql = TABLE.table_MapStatistics.insertData()
                .insert(TABLE.MAP_STATISTICS.field_PlayerUUID,  playerUUID)
                .insert(TABLE.MAP_STATISTICS.field_Capacity,    (long) capacity)
                .insert(TABLE.MAP_STATISTICS.field_Used,        (long) used)
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }
    public boolean setStatisticsCapacity(UUID playerUUID, int capacity) throws SQLException {
        SQL sql = TABLE.table_MapStatistics.updateData()
                .updates(TABLE.MAP_STATISTICS.field_Capacity, (long) capacity)
                .where(w -> w.and(TABLE.MAP_STATISTICS.field_PlayerUUID, playerUUID))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }
    public boolean increaseStatisticsCapacity(UUID playerUUID, int capacity) throws SQLException {
        SQL sql = TABLE.table_MapStatistics.updateData()
                .updatesIncrease(TABLE.MAP_STATISTICS.field_Capacity, (long) capacity)
                .where(w -> w.and(TABLE.MAP_STATISTICS.field_PlayerUUID, playerUUID))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }
    public boolean subtractStatisticsCapacity(UUID playerUUID, int capacity) throws SQLException {
        SQL sql = TABLE.table_MapStatistics.updateData()
                .updatesSubtract(TABLE.MAP_STATISTICS.field_Capacity, (long) capacity)
                .where(w -> w.and(TABLE.MAP_STATISTICS.field_PlayerUUID, playerUUID))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }
    public int[] getStatistics(UUID playerUUID) throws SQLException {
        SQL sql = TABLE.table_MapStatistics.selectData()
                .select(TABLE.MAP_STATISTICS.field_Used)
                .select(TABLE.MAP_STATISTICS.field_Capacity)
                .where(w -> w.and(TABLE.MAP_STATISTICS.field_PlayerUUID, playerUUID))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        if (sql.Q()) {
            sql.N();
            return new int[] {sql.get(TABLE.MAP_STATISTICS.field_Used).intValue(), sql.getThenClose(TABLE.MAP_STATISTICS.field_Capacity).intValue()};
        } else {
            sql.C();
            return null;
        }
    }
    public boolean consumeStatisticsUsed(UUID playerUUID, int consume) throws SQLException {
        SQL sql = TABLE.table_MapStatistics.updateData()
                .updatesIncrease(TABLE.MAP_STATISTICS.field_Used, (long) consume)
                .where(w -> w.and(TABLE.MAP_STATISTICS.field_PlayerUUID, playerUUID).and(TABLE.MAP_STATISTICS.field_Capacity, WhereJudge.ABOVE, TABLE.MAP_STATISTICS.field_Used, WhereOperator.INCREASE, (long) consume))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }
    public boolean releaseStatisticsUsed(UUID playerUUID) throws SQLException {
        SQL sql = TABLE.table_MapStatistics.updateData()
                .updatesSubtract(TABLE.MAP_STATISTICS.field_Used, 1L)
                .where(w -> w.and(TABLE.MAP_STATISTICS.field_PlayerUUID, playerUUID).and(TABLE.MAP_STATISTICS.field_Used, WhereJudge.ABOVE, 1L))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.UC() > 0;
    }
    public boolean existStatistics(UUID playerUUID) throws SQLException {
        SQL sql = TABLE.table_MapStatistics.selectData()
                .select(TABLE.MAP_STATISTICS.field_Used)
                .where(w -> w.and(TABLE.MAP_STATISTICS.field_PlayerUUID, playerUUID))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        return sql.QC();
    }

    public int getPlayerId(UUID playerUUID) throws SQLException {
        SQL sql = TABLE.table_MapStatistics.selectData()
                .select(TABLE.MAP_STATISTICS.field_PlayerID)
                .where(w -> w.and(TABLE.MAP_STATISTICS.field_PlayerUUID, playerUUID))
                .limit(1)
                .callSQL(configData.getDatabaseConnection());
        if (sql.Q()) {
            sql.N();
            return sql.getThenClose(TABLE.MAP_STATISTICS.field_PlayerID).intValue();
        } else {
            sql.C();
            return -1;
        }
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
