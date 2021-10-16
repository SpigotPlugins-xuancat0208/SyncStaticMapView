package xuan.cat.syncstaticmapview.code;

import com.comphenix.protocol.events.PacketEvent;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.translation.Translator;
import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;
import xuan.cat.syncstaticmapview.api.branch.BranchPacket;
import xuan.cat.syncstaticmapview.api.data.MapRedirect;
import xuan.cat.syncstaticmapview.code.data.ConfigData;
import xuan.cat.syncstaticmapview.code.data.MapDataCache;
import xuan.cat.syncstaticmapview.code.data.MapRedirectsCache;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    /** 快取地圖資料 */
    private final Map<Integer, MapDataCache> mapDataCaches = new ConcurrentHashMap<>();
    /** 快取權限資料 */
    private final Map<Integer, MapRedirectsCache> mapRedirectsCaches = new ConcurrentHashMap<>();
    /** 顯示完畢的地圖編號 */
    private final Map<Player, Set<Integer>> endShowMapId = new ConcurrentHashMap<>();
    /** 排隊顯示的地圖編號 */
    private final Map<Player, Set<Integer>> queueShowMapId = new ConcurrentHashMap<>();
    private volatile boolean asyncTickRunning = false;
    private volatile boolean asyncUrlTickRunning = false;
    /** 上一次請求更新 */
    private Map<Integer, Date> markUpdatesLast = new HashMap<>();
    /** 排隊全局更新 */
    private final Set<Integer> queueSyncUpdate = ConcurrentHashMap.newKeySet();
    /** 分散處理避免過量訪問網址 **/
    private final List<Runnable> queueSpeedLimitURL = new ArrayList<>();
    /** 等待網址處裡的延遲時間 */
    private int delaySpeedLimitURL = 0;
    /** 玩家個別速率限制 */
    private final Map<Player, Long> playerCoolingTime = new ConcurrentHashMap<>();
    /** 語言文件 */
    private final Map<Locale, JsonObject> langFileMap = new ConcurrentHashMap<>();
    /** 預設語言文件 */
    private final JsonObject langFileDefault = loadLang(Locale.ENGLISH);


    public MapServer(Plugin plugin, ConfigData configData, MapDatabase mapDatabase, BranchMapConversion branchMapConversion, BranchMapColor branchMapColor, BranchMinecraft branchMinecraft, BranchPacket branchPacket) {
        this.plugin = plugin;
        this.configData = configData;
        this.branchMapConversion = branchMapConversion;
        this.branchMapColor = branchMapColor;
        this.mapDatabase = mapDatabase;
        this.branchMinecraft = branchMinecraft;
        this.branchPacket = branchPacket;
        BukkitScheduler scheduler = Bukkit.getScheduler();
        bukkitTasks.add(scheduler.runTaskTimer(plugin, this::syncTick, 0, 10));
        bukkitTasks.add(scheduler.runTaskTimerAsynchronously(plugin, this::asyncTick, 0, 10));
        bukkitTasks.add(scheduler.runTaskTimerAsynchronously(plugin, this::asyncUrlTick, 0, 20));
    }


    private void syncTick() {
        // 全局更新
        if (queueSyncUpdate.size() > 0) {
            List<ItemFrame> itemFrameList = new ArrayList<>();
            Bukkit.getWorlds().forEach(world -> itemFrameList.addAll(world.getEntitiesByClass(ItemFrame.class)));
            queueSyncUpdate.forEach(mapId -> {
                itemFrameList.forEach(itemFrame -> {
                    ItemStack item = itemFrame.getItem();
                    if (item.getType() == Material.FILLED_MAP && branchMinecraft.getMapId(item) == -mapId) {
                        branchMinecraft.getTracking(itemFrame).forEach(player -> {
                            endShowMapId.getOrDefault(player, new HashSet<>(1)).remove(mapId);
                            queueShowMapId.getOrDefault(player, new HashSet<>(1)).add(mapId);
                        });
                    }
                });
                queueSyncUpdate.remove(mapId);
            });
        }

        // 更新背包地圖
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
            mapRedirectsCaches.forEach((mapId, mapRedirectsCache) -> {
                if (mapRedirectsCache.vitality <= System.currentTimeMillis())
                    mapRedirectsCaches.remove(mapId);
            });

            // 檢查請求更新
            mapDatabase.expiredMapUpdate();
            Map<Integer, Date> markUpdates = mapDatabase.getMapUpdates();
            markUpdates.forEach((mapId, markTime) -> {
                Date lastTime = markUpdatesLast.get(mapId);
                if (lastTime == null || lastTime.getTime() != markTime.getTime()) {
                    mapRedirectsCaches.remove(mapId);
                    queueSyncUpdate.add(mapId);
                }
            });
            markUpdatesLast = markUpdates;

            // 發送地圖資料
            queueShowMapId.forEach((player, mapIds) -> mapIds.removeIf(mapId -> {
                MapRedirectsCache mapRedirectsCache = cacheMapRedirects(mapId);
                int targetMapID = mapId;
                for (MapRedirect redirect : mapRedirectsCache.redirects) {
                   if (player.hasPermission(redirect.getPermission())) {
                       targetMapID = redirect.getRedirectId();
                       break;
                   }
                }
                MapDataCache mapDataCache = cacheMapData(targetMapID);
                if (mapDataCache.data != null) {
                    branchPacket.sendMapView(player, -mapId, mapDataCache.data);
                    endShowMapId.getOrDefault(player, new HashSet<>(1)).add(mapId);
                }

                return true;
            }));

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        asyncTickRunning = false;
    }


    private void asyncUrlTick() {
        if (asyncUrlTickRunning)
            return;
        asyncUrlTickRunning = true;

        try {
            if (delaySpeedLimitURL <= 0) {
                Runnable runnable = null;
                synchronized (queueSpeedLimitURL) {
                    if (queueSpeedLimitURL.size() > 0) {
                        runnable = queueSpeedLimitURL.remove(0);
                    }
                }
                if (runnable != null) {
                    delaySpeedLimitURL = configData.urlRateLimit;
                    runnable.run();
                }
            } else {
                delaySpeedLimitURL--;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        asyncUrlTickRunning = false;
    }


    public void processURL(Runnable runnable) {
        synchronized (queueSpeedLimitURL) {
            queueSpeedLimitURL.add(runnable);
        }
    }


    public MapRedirectsCache cacheMapRedirects(int mapId) {
        return mapRedirectsCaches.computeIfAbsent(mapId, key -> {
            try {
                return new MapRedirectsCache(mapDatabase.getMapRedirects(key), System.currentTimeMillis() + configData.cacheVitalityTime);
            } catch (Exception exception) {
                exception.printStackTrace();
                return new MapRedirectsCache(new ArrayList<>(), System.currentTimeMillis() + configData.cacheVitalityTime);
            }
        });
    }
    public MapDataCache cacheMapData(int mapId) {
        return mapDataCaches.computeIfAbsent(mapId, key -> {
            try {
                return new MapDataCache(mapDatabase.loadMapData(key), System.currentTimeMillis() + configData.cacheVitalityTime);
            } catch (Exception exception) {
                exception.printStackTrace();
                return new MapDataCache(null, System.currentTimeMillis() + configData.cacheVitalityTime);
            }
        });
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
                if (!endShowMapId.getOrDefault(player, new HashSet<>(1)).contains(mapId))
                    queueShowMapId.getOrDefault(player, new HashSet<>(1)).add(mapId);
            }
        }
    }



    public boolean markCoolingTime(Player player, int cooling) {
        Long oldTime = playerCoolingTime.get(player);
        long nowTime = System.currentTimeMillis();
        if (oldTime == null || oldTime < nowTime) {
            playerCoolingTime.put(player, nowTime + cooling);
            return true;
        } else {
            return false;
        }
    }


    /**
     * @param sender 執行人
     * @param key 條目鑰匙
     * @return 語言條目
     */
    public String getLang(CommandSender sender, String key) {
        if (sender instanceof Player) {
            try {
                // 1.16 以上
                return getLang(((Player) sender).locale(), key);
            } catch (NoSuchMethodError noSuchMethodError) {
                return getLang(Translator.parseLocale(((Player) sender).getLocale()), key);
            }
        } else {
            return getLang(Locale.ENGLISH, key);
        }
    }
    /**
     * @param locale 語言類型
     * @param key 條目鑰匙
     * @return 語言條目
     */
    public String getLang(Locale locale, String key) {
        JsonObject lang = langFileMap.computeIfAbsent(locale, v -> loadLang(locale));
        JsonElement element = lang.get(key);
        if (element != null && !element.isJsonNull()) {
            return element.getAsString();
        } else {
            return langFileDefault.get(key).getAsString();
        }
    }
    /**
     * @param locale 語言類型
     * @return 讀取語言文件
     */
    private JsonObject loadLang(Locale locale) {
        URL url = getClass().getClassLoader().getResource("lang/" + locale.toString().toLowerCase(Locale.ROOT) + ".json");
        if (url == null)
            return new JsonObject();
        try {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(true);
            return new Gson().fromJson(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8), JsonObject.class);
        } catch (IOException exception) {
            return new JsonObject();
        }
    }


    public void createCache(Player player) {
        endShowMapId.put(player, ConcurrentHashMap.newKeySet());
        queueShowMapId.put(player, ConcurrentHashMap.newKeySet());
    }

    public void cleanCache(Player player) {
        endShowMapId.remove(player);
        queueShowMapId.remove(player);
        playerCoolingTime.remove(player);
    }


    public void createCache(int mapId) {
        mapRedirectsCaches.remove(mapId);
    }


    /**
     * 關閉視圖伺服器
     */
    public void close() {
        for (BukkitTask task : bukkitTasks)
            task.cancel();
    }
}
