package xuan.cat.syncstaticmapview.code;

import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;
import xuan.cat.syncstaticmapview.api.branch.BranchPacket;
import xuan.cat.syncstaticmapview.api.data.MapData;
import xuan.cat.syncstaticmapview.code.data.ConfigData;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class MapServer {
    private final Plugin plugin;
    private final ConfigData configData;
    private final BranchMapConversion branchMapConversion;
    private final BranchMapColor branchMapColor;
    private final MapDatabase mapDatabase;
    private final BranchMinecraft branchMinecraft;
    private final BranchPacket branchPacket;
    private final Set<BukkitTask> bukkitTasks = ConcurrentHashMap.newKeySet();
    /** 快取 */
    private final Map<Integer, MapDataCache> mapDataCaches = new ConcurrentHashMap<>();
    private class MapDataCache {
        public final long vitality = System.currentTimeMillis() + configData.getCacheVitalityTime();
        public final MapData data;

        private MapDataCache(MapData data) {
            this.data = data;
        }
    }
    /** 顯示完畢的地圖編號 */
    private final Map<Player, Set<Integer>> endShowMapId = new ConcurrentHashMap<>();
    /** 排隊顯示的地圖編號 */
    private final Map<Player, Set<Integer>> queueShowMapId = new ConcurrentHashMap<>();
    private volatile boolean asyncTickRunning = false;


    public MapServer(Plugin plugin, ConfigData configData, MapDatabase mapDatabase, BranchMapConversion branchMapConversion, BranchMapColor branchMapColor, BranchMinecraft branchMinecraft, BranchPacket branchPacket) {
        this.plugin = plugin;
        this.configData = configData;
        this.branchMapConversion = branchMapConversion;
        this.branchMapColor = branchMapColor;
        this.mapDatabase = mapDatabase;
        this.branchMinecraft = branchMinecraft;
        this.branchPacket = branchPacket;
        BukkitScheduler scheduler = Bukkit.getScheduler();
        bukkitTasks.add(scheduler.runTaskTimer(plugin, this::syncTick, 0, 1));
        bukkitTasks.add(scheduler.runTaskTimerAsynchronously(plugin, this::asyncTick, 0, 1));
    }


    private void syncTick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().forEach(item -> trySendMap(player, item));
        }
    }


    private void asyncTick() {
        if (asyncTickRunning)
            return;
        asyncTickRunning = true;

        try {
            // 緩存清除
            mapDataCaches.forEach((mapId, mapDataCache) -> {
                if (mapDataCache.vitality <= System.currentTimeMillis())
                    mapDataCaches.remove(mapId);
            });

            // 發送地圖資料
            queueShowMapId.forEach((player, mapIds) -> mapIds.removeIf(mapId -> {
                MapDataCache mapDataCache = mapDataCaches.get(mapId);
                if (mapDataCache == null) {
                    // 讀取
                    try {
                        mapDataCache = new MapDataCache(mapDatabase.loadMapData(mapId));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        mapDataCache = new MapDataCache(null);
                    }
                }

                if (mapDataCache.data != null) {
                    branchPacket.sendMapView(player, -mapId, mapDataCache.data);
                    endShowMapId.getOrDefault(player, new HashSet<>()).add(mapId);
                }

                return true;
            }));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        asyncTickRunning = false;
    }


    public void trySendMap(Player player, PacketEvent event) {
        World world = player.getWorld();
        int entityId = branchPacket.readEntityIdSpawn(event.getPacket());
        Entity entity = branchMinecraft.getEntityFromId(world, entityId);
        if (entity instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) entity;
            trySendMap(player, itemFrame.getItem());
        }
    }

    public void trySendMap(Player player, ItemStack item) {
        if (item != null && item.getType() == Material.FILLED_MAP) {
            int mapId = -branchMinecraft.getMapId(item);
            if (mapId > 0) {
                if (!endShowMapId.getOrDefault(player, new HashSet<>()).contains(mapId))
                    queueShowMapId.getOrDefault(player, new HashSet<>()).add(mapId);
            }
        }
    }



    public void createCache(Player player) {
        endShowMapId.put(player, ConcurrentHashMap.newKeySet());
        queueShowMapId.put(player, ConcurrentHashMap.newKeySet());
    }

    public void cleanCache(Player player) {
        endShowMapId.remove(player);
        queueShowMapId.remove(player);
    }



    /**
     * 關閉視圖伺服器
     */
    public void close() {
        for (BukkitTask task : bukkitTasks)
            task.cancel();
    }
}
