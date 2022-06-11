package xuan.cat.syncstaticmapview.code.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;
import xuan.cat.syncstaticmapview.api.data.MapData;
import xuan.cat.syncstaticmapview.code.MapDatabase;
import xuan.cat.syncstaticmapview.code.MapServer;
import xuan.cat.syncstaticmapview.code.data.ConfigData;
import xuan.cat.syncstaticmapview.code.data.MapRedirectEntry;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public final class Command implements CommandExecutor {
    private final Plugin                plugin;
    private final MapDatabase           mapDatabase;
    private final MapServer             mapServer;
    private final ConfigData            configData;
    private final BranchMapConversion   branchMapConversion;
    private final BranchMapColor        branchMapColor;
    private final BranchMinecraft       branchMinecraft;
    private final Pattern               isPlayerUUID            = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    private final Pattern               isPlayerName            = Pattern.compile("^\\w{3,16}$");

    public Command(Plugin plugin, MapDatabase mapDatabase, MapServer mapServer, ConfigData configData, BranchMapConversion branchMapConversion, BranchMapColor branchMapColor, BranchMinecraft branchMinecraft) {
        this.plugin                 = plugin;
        this.mapDatabase            = mapDatabase;
        this.mapServer              = mapServer;
        this.configData             = configData;
        this.branchMapConversion    = branchMapConversion;
        this.branchMapColor         = branchMapColor;
        this.branchMinecraft        = branchMinecraft;
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String message, String[] parameters) {
        if (parameters.length < 1) {
            // 缺少參數
            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
        } else {
            switch (parameters[0]) {
                case "reload":
                    // 重新讀取配置文件
                    if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.reload")) {
                        // 無權限
                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                    } else {
                        try {
                            configData.reload();
                            // 已重新讀取設定
                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.reread_configuration_successfully"));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // 重新讀取設定時發生錯誤
                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.reread_configuration_error"));
                        }
                    }
                    break;

                case "get":
                    // 重新讀取配置文件
                    if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.get_own") && !sender.hasPermission("command.mapview.get_all")) {
                        // 無權限
                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                    } else if (!(sender instanceof Player)) {
                        // 只能以玩家身分執行此指令
                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.only_be_used_by_player"));
                    } else {
                        Player player = (Player) sender;
                        if (parameters.length >= 2) {
                            try {
                                int mapId = Integer.parseInt(parameters[1]);
                                if (mapDatabase.existMapData(mapId)) {
                                    if (mapDatabase.getPlayerId(player.getUniqueId()) != mapDatabase.getMapUploaderID(mapId)) {
                                        if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.get_all")) {
                                            // 你無法獲取此地圖
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.can_not_get_this_map"));
                                        } else {
                                            giveMapItem(player, branchMinecraft.saveItemNBT(new ItemStack(Material.FILLED_MAP), configData.itemSingleTag.replaceAll("%mapview.id%", String.valueOf(-mapId))));
                                            // 已給予: ?
                                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.given") + mapId);
                                        }
                                    } else {
                                        if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.get_own")) {
                                            // 你無法獲取此地圖
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.can_not_get_this_map"));
                                        } else {
                                            giveMapItem(player, branchMinecraft.saveItemNBT(new ItemStack(Material.FILLED_MAP), configData.itemSingleTag.replaceAll("%mapview.id%", String.valueOf(-mapId))));
                                            // 已給予: ?
                                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.given") + mapId);
                                        }
                                    }
                                } else {
                                    // 地圖資料不存在: ?
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.map_data_not_exist") + mapId);
                                }
                            } catch (CommandSyntaxException exception) {
                                sender.sendMessage(ChatColor.RED + "NBT Error: " + exception.getMessage());
                                exception.printStackTrace();
                            } catch (NumberFormatException exception) {
                                // 參數不是數字
                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_number") + parameters[1]);
                            } catch (SQLException exception) {
                                // 資料庫錯誤
                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                exception.printStackTrace();
                            }
                        } else {
                            // 缺少參數
                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                        }
                    }
                    break;

                case "delete":
                    // 重新讀取配置文件
                    if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.delete_own") && !sender.hasPermission("command.mapview.delete_all")) {
                        // 無權限
                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                    } else {
                        if (parameters.length >= 2) {
                            try {
                                int mapId = Integer.parseInt(parameters[1]);
                                if (mapDatabase.existMapData(mapId)) {
                                    int uploaderID = mapDatabase.getMapUploaderID(mapId);

                                    if (sender instanceof Player) {
                                        Player player = (Player) sender;
                                        if (mapDatabase.getPlayerId(player.getUniqueId()) != uploaderID) {
                                            if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.delete_all")) {
                                                // 你無法刪除此地圖
                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.can_not_delete_this_map"));
                                                return true;
                                            }
                                        } else {
                                            if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.delete_own")) {
                                                // 你無法刪除此地圖
                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.can_not_delete_this_map"));
                                                return true;
                                            }
                                        }
                                    }

                                    mapDatabase.releaseStatisticsUsed(uploaderID);
                                    mapDatabase.removeMapData(mapId);
                                    mapDatabase.removeMapRedirect(mapId);
                                    // 已刪除: ?
                                    sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.deleted") + mapId);

                                } else {
                                    // 地圖資料不存在: ?
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.map_data_not_exist") + mapId);
                                }
                            } catch (NumberFormatException exception) {
                                // 參數不是數字
                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_number") + parameters[1]);
                            } catch (SQLException exception) {
                                // 資料庫錯誤
                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                exception.printStackTrace();
                            }
                        } else {
                            // 缺少參數
                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                        }
                    }
                    break;

                case "create":
                    // 創建地圖
                    // 檢查
                    if (parameters.length >= 2) {
                        switch (parameters[1]) {
                            case "url":
                                // 網址
                                if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.create.*") && !sender.hasPermission("command.mapview.create.url")) {
                                    // 無權限
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                } else {
                                    if (parameters.length >= 4) {
                                        String stitched = stitchedRight(parameters, 3);
                                        if (stitched.length() == 0) {
                                            // 缺少參數
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                                        } else {
                                            boolean allowed = false;
                                            if (sender.hasPermission("mapview.ignore_url_allowed")) {
                                                allowed = true;
                                            } else {
                                                for (String allowedUrl : configData.allowedUrlSourceList) {
                                                    if (stitched.startsWith(allowedUrl)) {
                                                        allowed = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (allowed) {
                                                try {
                                                    URL url = new URL(stitched);
                                                    try {
                                                        Consumer<BufferedImage> toAsync = cropImageSave(sender, parameters[2]);
                                                        if (toAsync != null) {
                                                            if (sender instanceof Player && !sender.hasPermission("mapview.ignore_create_rate_limit") && !mapServer.markCoolingTime((Player) sender, configData.createRateLimit)) {
                                                                // 創建的速度太快了, 等一下
                                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.creation_speed_too_fast"));
                                                            } else {
                                                                mapServer.processURL(() -> {
                                                                    try {
                                                                        HttpURLConnection connection    = (HttpURLConnection) url.openConnection();
                                                                        connection.setRequestMethod("GET");
                                                                        connection.setDoInput(true);
                                                                        connection.setDoOutput(false);
                                                                        connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                                                                        connection.setRequestProperty("accept-encoding", "gzip, deflate, br");
                                                                        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36");
                                                                        if (connection.getResponseCode() == 200) {
                                                                            InputStream inputStream = connection.getInputStream();
                                                                            BufferedImage bufferedImage = ImageIO.read(inputStream);
                                                                            inputStream.close();
                                                                            if (bufferedImage != null) {
                                                                                toAsync.accept(bufferedImage);
                                                                            } else {
                                                                                // 圖片下載失敗
                                                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.image_download_failed"));
                                                                            }
                                                                        } else {
                                                                            throw new IOException(connection.getResponseCode() + " " + connection.getResponseMessage());
                                                                        }
                                                                    } catch (IOException exception) {
                                                                        // 圖片下載失敗
                                                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.image_download_failed") + exception.getMessage());
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    } catch (SQLException exception) {
                                                        // 資料庫錯誤
                                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                                        exception.printStackTrace();
                                                    }
                                                } catch (MalformedURLException exception) {
                                                    // 輸入的參數不是網址
                                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_url") + stitched);
                                                }
                                            } else {
                                                // 不允許使用此網址
                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.url_not_allowed"));
                                            }
                                        }
                                    } else {
                                        // 缺少參數
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                                    }
                                }
                                break;

                            case "file":
                                // 文件
                                if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.create.*") && !sender.hasPermission("command.mapview.create.file")) {
                                    // 無權限
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                } else {
                                    if (parameters.length >= 4) {
                                        String stitched = stitchedRight(parameters, 3);
                                        if (stitched.length() == 0) {
                                            // 缺少參數
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                                        } else {
                                            File file = new File(stitched);
                                            try {
                                                Consumer<BufferedImage> toAsync = cropImageSave(sender, parameters[2]);
                                                if (toAsync != null) {
                                                    if (sender instanceof Player && !sender.hasPermission("mapview.ignore_create_rate_limit") && !mapServer.markCoolingTime((Player) sender, configData.createRateLimit)) {
                                                        // 創建的速度太快了, 等一下
                                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.creation_speed_too_fast"));
                                                    } else {
                                                        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                                            try {
                                                                toAsync.accept(ImageIO.read(file));
                                                            } catch (IOException exception) {
                                                                // 圖片讀取失敗
                                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.image_read_failed") + exception.getMessage());
                                                            }
                                                        });
                                                    }
                                                }
                                            } catch (SQLException exception) {
                                                // 資料庫錯誤
                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                                exception.printStackTrace();
                                            }
                                        }
                                    } else {
                                        // 缺少參數
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                                    }
                                }
                                break;

                            case "hand":
                                // 手上
                                if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.create.*") && !sender.hasPermission("command.mapview.create.hand")) {
                                    // 無權限
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                } else if (!(sender instanceof Player)) {
                                    // 只能以玩家身分執行此指令
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.only_be_used_by_player"));
                                } else {
                                    Player player = (Player) sender;
                                    ItemStack item = player.getInventory().getItemInMainHand();
                                    if (item.getType() == Material.FILLED_MAP) {
                                        MapMeta mapMeta = (MapMeta) item.getItemMeta();
                                        MapView mapView = mapMeta.hasMapView() ? mapMeta.getMapView() : null;
                                        if (branchMinecraft.isDisableCopy(item)) {
                                            // 此地圖不可複製
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.map_cannot_copy"));
                                        } else if (mapView != null) {
                                            if (!player.hasPermission("mapview.ignore_create_rate_limit") && !mapServer.markCoolingTime(player, configData.createRateLimit)) {
                                                // 創建的速度太快了, 等一下
                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.creation_speed_too_fast"));
                                            } else {
                                                try {
                                                    if (!mapDatabase.consumeStatisticsUsed(player.getUniqueId(), 1)) {
                                                        int[] statistics = mapDatabase.getStatistics(player.getUniqueId());
                                                        if (statistics != null) {
                                                            if (!player.hasPermission("mapview.ignore_upload_limit")) {
                                                                // 超出允許的數量
                                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.your_upload_has_reached_limit") + statistics[0] + " + 1 / " + statistics[1]);
                                                                return true;
                                                            }
                                                        } else if (1 > configData.defaultPlayerLimit && !player.hasPermission("mapview.ignore_upload_limit")) {
                                                            // 超出允許的數量
                                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.your_upload_has_reached_limit") + " 0 + 1 / " + configData.defaultPlayerLimit);
                                                            return true;
                                                        } else {
                                                            // 加入資料紀錄
                                                            mapDatabase.createStatistics(player.getUniqueId(), configData.defaultPlayerLimit, 1);
                                                        }
                                                    }

                                                    MapData mapData = branchMapConversion.ofBukkit(mapView);
                                                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                                        try {
                                                            int mapId = mapDatabase.addMapData(mapData, mapDatabase.getPlayerId(player.getUniqueId()));
                                                            // 創建完畢, 資料庫地圖編號為:
                                                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.created_successfully") + mapId);
                                                            giveMapItem(player, branchMinecraft.saveItemNBT(new ItemStack(Material.FILLED_MAP), configData.itemSingleTag.replaceAll("%mapview.id%", String.valueOf(-mapId))));
                                                        } catch (CommandSyntaxException exception) {
                                                            sender.sendMessage(ChatColor.RED + "NBT Error: " + exception.getMessage());
                                                            exception.printStackTrace();
                                                        } catch (SQLException exception) {
                                                            // 資料庫錯誤
                                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                                            exception.printStackTrace();
                                                        }
                                                    });

                                                } catch (SQLException exception) {
                                                    // 資料庫錯誤
                                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                                    exception.printStackTrace();
                                                }
                                            }
                                        } else {
                                            // 此地圖沒有原始資料
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.map_no_original_data"));
                                        }
                                    } else {
                                        // 手上拿著的物品不是地圖
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.not_filled_map_on_hand"));
                                    }
                                }
                                break;
                            default:
                                // 未知的參數類型
                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.unknown_parameter_type") + " " + parameters[1]);
                                break;
                        }
                    } else {
                        // 缺少參數
                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                    }
                    break;

                case "redirect":
                    // 重定向
                    if (parameters.length >= 3) {
                        try {
                            int mapId = Integer.parseInt(parameters[1]);
                            switch (parameters[2]) {
                                case "list":
                                    if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.redirect.*") && !sender.hasPermission("command.mapview.redirect.list")) {
                                        // 無權限
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                    } else if (!mapDatabase.existMapData(mapId)) {
                                        // 地圖資料不存在: ?
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.map_data_not_exist") + mapId);
                                    } else {
                                        List<MapRedirectEntry> redirects = mapServer.cacheMapRedirects(mapId).redirects;
                                        if (redirects.size() == 0) {
                                            // 沒有設置任何的重定向
                                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.not_set_any_redirect"));
                                        } else {
                                            // 重定向清單
                                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.redirect_list"));
                                            for (MapRedirectEntry redirect : redirects) {
                                                sender.sendMessage(" - " + ChatColor.YELLOW + redirect.getPriority() + ": " + redirect.getPermission() + " => " + redirect.getRedirectId());
                                            }
                                        }
                                    }
                                    break;

                                case "set":
                                    if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.redirect.*") && !sender.hasPermission("command.mapview.redirect.set")) {
                                        // 無權限
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                    } else if (!mapDatabase.existMapData(mapId)) {
                                        // 地圖資料不存在: ?
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.map_data_not_exist") + mapId);
                                    } else {
                                        if (parameters.length >= 6) {
                                            try {
                                                int priority = Integer.parseInt(parameters[3]);
                                                try {
                                                    int redirectId = Integer.parseInt(parameters[5]);
                                                    MapRedirectEntry redirect = new MapRedirectEntry(priority, parameters[4], redirectId);
                                                    if (mapDatabase.existMapData(redirect.getRedirectId())) {
                                                        mapDatabase.removeMapRedirect(mapId, redirect.getPermission());
                                                        mapDatabase.removeMapRedirect(mapId, redirect.getPriority());
                                                        mapDatabase.addMapRedirects(mapId, redirect);
                                                        mapServer.createCache(mapId);
                                                        mapDatabase.markMapUpdate(mapId);
                                                        // 重導向設置成功
                                                        sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.redirect_set_successfully"));
                                                    } else {
                                                        // 地圖資料不存在
                                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.map_data_not_exist") + redirect.getRedirectId());
                                                    }
                                                } catch (NumberFormatException exception) {
                                                    // 參數不是數字
                                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_number") + parameters[5]);
                                                }
                                            } catch (NumberFormatException exception) {
                                                // 參數不是數字
                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_number") + parameters[3]);
                                            }
                                        } else {
                                            // 缺少參數
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                                        }
                                    }
                                    break;

                                case "delete":
                                    if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.redirect.*") && !sender.hasPermission("command.mapview.redirect.delete")) {
                                        // 無權限
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                    } else if (!mapDatabase.existMapData(mapId)) {
                                        // 地圖資料不存在: ?
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.map_data_not_exist") + mapId);
                                    } else {
                                        if (parameters.length >= 4) {
                                            mapDatabase.removeMapRedirect(mapId, parameters[3]);
                                            mapServer.createCache(mapId);
                                            mapDatabase.markMapUpdate(mapId);
                                            // 重導向刪除成功
                                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.redirect_delete_successfully"));
                                        } else {
                                            // 缺少參數
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                                        }
                                    }
                                    break;

                                case "delete_all":
                                    if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.redirect.*") && !sender.hasPermission("command.mapview.redirect.delete_all")) {
                                        // 無權限
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                    } else if (!mapDatabase.existMapData(mapId)) {
                                        // 地圖資料不存在: ?
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.map_data_not_exist") + mapId);
                                    } else {
                                        mapDatabase.removeMapRedirect(mapId);
                                        mapServer.createCache(mapId);
                                        mapDatabase.markMapUpdate(mapId);
                                        // 重導向刪除成功
                                        sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.redirect_delete_successfully"));
                                    }
                                    break;

                                default:
                                    // 未知的參數類型
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.unknown_parameter_type") + " " + parameters[0]);
                                    break;
                            }
                        } catch (NumberFormatException exception) {
                            // 參數不是數字
                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_number") + parameters[1]);
                        } catch (SQLException exception) {
                            // 資料庫錯誤
                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                            exception.printStackTrace();
                        }
                    } else {
                        // 缺少參數
                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                    }
                    break;

                case "limit":
                    if (parameters.length >= 2) {
                        switch (parameters[1]) {
                            case "set":
                                if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.limit.*") && !sender.hasPermission("command.mapview.limit.set")) {
                                    // 無權限
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                } else if (parameters.length < 4) {
                                    // 缺少參數
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                                } else {
                                    UUID playerUUID = parseNameOrUUID(sender, parameters[2]);
                                    if (playerUUID != null) {
                                        try {
                                            int capacity = Integer.parseInt(parameters[3]);
                                            if (!mapDatabase.setStatisticsCapacity(playerUUID, capacity))
                                                mapDatabase.createStatistics(playerUUID, capacity, 0);
                                            int[] statistics = mapDatabase.getStatistics(playerUUID);
                                            if (statistics == null)
                                                statistics = new int[] {0, 0};
                                            // 設置完畢, 當前的上傳限制: ?
                                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.set_successfully_now_limit") + statistics[0] + " / " + statistics[1]);
                                        } catch (SQLException exception) {
                                            // 資料庫錯誤
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                            exception.printStackTrace();
                                        } catch (NumberFormatException exception) {
                                            // 參數不是數字
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_number") + parameters[4]);
                                        }
                                    }
                                }
                                break;

                            case "increase":
                                if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.limit.*") && !sender.hasPermission("command.mapview.limit.increase")) {
                                    // 無權限
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                } else if (parameters.length < 4) {
                                    // 缺少參數
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                                } else {
                                    UUID playerUUID = parseNameOrUUID(sender, parameters[2]);
                                    if (playerUUID != null) {
                                        try {
                                            int capacity = Integer.parseInt(parameters[3]);
                                            if (!mapDatabase.increaseStatisticsCapacity(playerUUID, capacity))
                                                mapDatabase.createStatistics(playerUUID, configData.defaultPlayerLimit + capacity, 0);
                                            int[] statistics = mapDatabase.getStatistics(playerUUID);
                                            if (statistics == null)
                                                statistics = new int[] {0, configData.defaultPlayerLimit};
                                            // 設置完畢, 當前的上傳限制: ?
                                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.set_successfully_now_limit") + statistics[0] + " / " + statistics[1]);
                                        } catch (SQLException exception) {
                                            // 資料庫錯誤
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                            exception.printStackTrace();
                                        } catch (NumberFormatException exception) {
                                            // 參數不是數字
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_number") + parameters[4]);
                                        }
                                    }
                                }
                                break;

                            case "subtract":
                                if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.limit.*") && !sender.hasPermission("command.mapview.limit.subtract")) {
                                    // 無權限
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                } else if (parameters.length < 4) {
                                    // 缺少參數
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                                } else {
                                    UUID playerUUID = parseNameOrUUID(sender, parameters[2]);
                                    if (playerUUID != null) {
                                        try {
                                            int capacity = Integer.parseInt(parameters[3]);
                                            try {
                                                if (!mapDatabase.subtractStatisticsCapacity(playerUUID, capacity))
                                                    mapDatabase.createStatistics(playerUUID, configData.defaultPlayerLimit - capacity, 0);
                                            } catch (SQLException exception) {
                                                if (!mapDatabase.setStatisticsCapacity(playerUUID, 0))
                                                    mapDatabase.createStatistics(playerUUID, 0, 0);
                                            }
                                            int[] statistics = mapDatabase.getStatistics(playerUUID);
                                            if (statistics == null)
                                                statistics = new int[] {0, configData.defaultPlayerLimit};
                                            // 設置完畢, 當前的上傳限制: ?
                                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.set_successfully_now_limit") + statistics[0] + " / " + statistics[1]);
                                        } catch (SQLException exception) {
                                            // 資料庫錯誤
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                            exception.printStackTrace();
                                        } catch (NumberFormatException exception) {
                                            // 參數不是數字
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_number") + parameters[4]);
                                        }
                                    }
                                }
                                break;

                            case "check":
                                if (parameters.length < 3) {
                                    if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.limit.*") && !sender.hasPermission("command.mapview.limit.check_own")) {
                                        // 無權限
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                    } else if (!(sender instanceof Player)) {
                                        // 只能以玩家身分執行此指令
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.only_be_used_by_player"));
                                    } else {
                                        try {
                                            int[] statistics = mapDatabase.getStatistics(((Player) sender).getUniqueId());
                                            if (statistics == null)
                                                statistics = new int[] {0, configData.defaultPlayerLimit};
                                            // 上傳限制: ?
                                            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.now_limit") + statistics[0] + " / " + statistics[1]);
                                        } catch (SQLException exception) {
                                            // 資料庫錯誤
                                            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                            exception.printStackTrace();
                                        }
                                    }
                                } else {
                                    if (!sender.hasPermission("command.mapview.*") && !sender.hasPermission("command.mapview.limit.*") && !sender.hasPermission("command.mapview.limit.check_all")) {
                                        // 無權限
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.no_permission"));
                                    } else {
                                        UUID playerUUID = parseNameOrUUID(sender, parameters[2]);
                                        if (playerUUID != null) {
                                            try {
                                                int[] statistics = mapDatabase.getStatistics(playerUUID);
                                                if (statistics == null)
                                                    statistics = new int[] {0, configData.defaultPlayerLimit};
                                                // 上傳限制: ?
                                                sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.now_limit") + statistics[0] + " / " + statistics[1]);
                                            } catch (SQLException exception) {
                                                // 資料庫錯誤
                                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                                exception.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                break;
                        }
                    } else {
                        // 缺少參數
                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
                    }
                    break;

                default:
                    // 未知的參數類型
                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.unknown_parameter_type") + " " + parameters[0]);
                    break;
            }
        }

        return true;
    }


    private UUID parseNameOrUUID(CommandSender sender, String parameter) {
        if (parameter == null) {
            // 缺少參數
            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.missing_parameters"));
        } else if (isPlayerUUID.matcher(parameter).matches()) {
            try {
                return UUID.fromString(parameter);
            } catch (IllegalArgumentException exception) {
                // 參數不是玩家名稱或UUID
                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_player_name_or_uuid") + parameter);
            }
        } else if (isPlayerName.matcher(parameter).matches()) {
            Player player = Bukkit.getPlayer(parameter);
            if (player != null) {
                return player.getUniqueId();
            } else {
                // 此玩家沒有在線上, 使用 UUID
                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.player_not_online_use_uuid"));
            }
        } else {
            // 參數不是 玩家名稱 或 UUID
            sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_player_name_or_uuid") + parameter);
        }
        return null;
    }


    private void giveMapItem(Player player, ItemStack item) {
        if (player.isOnline())
            for (ItemStack drop : player.getInventory().addItem(item).values())
                player.getWorld().dropItem(player.getLocation(), drop).setOwner(player.getUniqueId());
    }

    private String stitchedRight(String[] parameters, int start) {
        StringBuilder builder = new StringBuilder();
        for (int index = start ; index < parameters.length ; index++) {
            if (builder.length() > 0)
                builder.append(' ');
            builder.append(parameters[index]);
        }
        return builder.toString();
    }

    private Consumer<BufferedImage> cropImageSave(CommandSender sender, String saveRatio) throws SQLException {
        if (saveRatio == null || saveRatio.length() == 0)
            saveRatio = "1:1";
        String[] saveRatios = saveRatio.split(":", 2);
        if (saveRatios.length != 2) {
            // 參數不是長寬比:
            sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.parameter_not_aspect_ratio") + saveRatio);
        } else {
            try {
                int spaceRow = Math.max(1, saveRatios[0].length() == 0 ? 1 : Integer.parseInt(saveRatios[0]));
                int spaceWidth = spaceRow * 128;
                try {
                    int spaceColumn = Math.max(1, saveRatios[1].length() == 0 ? 1 : Integer.parseInt(saveRatios[1]));
                    int spaceHeight = spaceColumn * 128;
                    if (!sender.hasPermission("mapview.ignore_size_restrictions") && (spaceRow > configData.maximumRowAllowed || spaceColumn > configData.maximumColumnAllowed)) {
                        // 超出允許的尺寸
                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.exceeding_allowed_size") + configData.maximumRowAllowed + ":" + configData.maximumColumnAllowed);
                    } else {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (!mapDatabase.consumeStatisticsUsed(player.getUniqueId(), spaceRow * spaceColumn)) {
                                int[] statistics = mapDatabase.getStatistics(player.getUniqueId());
                                if (statistics != null) {
                                    if (!player.hasPermission("mapview.ignore_upload_limit")) {
                                        // 超出允許的數量
                                        sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.your_upload_has_reached_limit") + statistics[0] + " + " + (spaceRow * spaceColumn) + " / " + statistics[1]);
                                        return null;
                                    }
                                } else if ((spaceRow * spaceColumn) > configData.defaultPlayerLimit && !player.hasPermission("mapview.ignore_upload_limit")) {
                                    // 超出允許的數量
                                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.your_upload_has_reached_limit") + " 0 + " + (spaceRow * spaceColumn) + " / " + configData.defaultPlayerLimit);
                                    return null;
                                } else {
                                    // 加入資料紀錄
                                    mapDatabase.createStatistics(player.getUniqueId(), configData.defaultPlayerLimit, spaceRow * spaceColumn);
                                }
                            }
                        }

                        return (BufferedImage sourceImage) -> {
                            try {
                                int revisionWidth = sourceImage.getWidth();
                                int revisionHeight = sourceImage.getHeight();

                                if (revisionWidth < spaceWidth) {
                                    double redress = (double) spaceWidth / (double) revisionWidth;
                                    revisionWidth = spaceWidth;
                                    revisionHeight = (int) (revisionHeight * redress);
                                }
                                if (revisionHeight < spaceHeight) {
                                    double redress = (double) spaceHeight / (double) revisionHeight;
                                    revisionWidth = (int) (revisionWidth * redress);
                                    revisionHeight = spaceHeight;
                                }

                                if (revisionWidth > spaceWidth) {
                                    double redress = (double) spaceWidth / (double) revisionWidth;
                                    revisionWidth = spaceWidth;
                                    revisionHeight = (int) (revisionHeight * redress);
                                }
                                if (revisionHeight > spaceHeight) {
                                    double redress = (double) spaceHeight / (double) revisionHeight;
                                    revisionWidth = (int) (revisionWidth * redress);
                                    revisionHeight = spaceHeight;
                                }

                                BufferedImage spaceImage = new BufferedImage(spaceWidth, spaceHeight, BufferedImage.TYPE_INT_ARGB);
                                Graphics2D spaceGraphics = spaceImage.createGraphics();
                                spaceGraphics.setComposite(AlphaComposite.Src);
                                spaceGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                spaceGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                                spaceGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                spaceGraphics.drawImage(sourceImage, (spaceWidth - revisionWidth) / 2, (spaceHeight - revisionHeight) / 2, revisionWidth, revisionHeight, null);
                                spaceGraphics.dispose();

                                int uploaderId = sender instanceof Player ? mapDatabase.getPlayerId(((Player) sender).getUniqueId()) : 0;
                                Set<Integer> createIds = new LinkedHashSet<>();
                                List<ItemStack> itemList = new ArrayList<>();
                                try {
                                    for (int readColumn = 0 ; readColumn < spaceColumn ; readColumn++) {
                                        for (int readRow = 0 ; readRow < spaceRow ; readRow++) {
                                            MapData mapData = new MapData(branchMapColor, branchMapConversion);
                                            for (int x = 0 ; x < 128 ; x++)
                                                for (int y = 0 ; y < 128 ; y++)
                                                    mapData.setColor(x, y, new Color(spaceImage.getRGB(readRow << 7 | x, readColumn << 7 | y), true));
                                            int mapId = mapDatabase.addMapData(mapData, uploaderId);
                                            createIds.add(mapId);
                                            if (spaceRow > 1 || spaceColumn > 1) {
                                                itemList.add(branchMinecraft.saveItemNBT(new ItemStack(Material.FILLED_MAP), configData.itemMultipleTag.replaceAll("%mapview.id%", String.valueOf(-mapId)).replaceAll("%mapview.column%", String.valueOf(readColumn + 1)).replaceAll("%mapview.row%", String.valueOf(readRow + 1))));
                                            } else {
                                                itemList.add(branchMinecraft.saveItemNBT(new ItemStack(Material.FILLED_MAP), configData.itemSingleTag.replaceAll("%mapview.id%", String.valueOf(-mapId))));
                                            }
                                        }
                                    }
                                } catch (CommandSyntaxException exception) {
                                    sender.sendMessage(ChatColor.RED + "NBT Error: " + exception.getMessage());
                                    exception.printStackTrace();
                                }

                                StringBuilder stringIds = new StringBuilder();

                                for (int createId : createIds) {
                                    if (stringIds.length() != 0)
                                        stringIds.append(',').append(' ');
                                    stringIds.append(createId);
                                }

                                // 創建完畢, 資料庫地圖編號為:
                                sender.sendMessage(ChatColor.YELLOW + mapServer.lang.get(sender, "command.created_successfully") + stringIds);
                                if (sender instanceof Player) {
                                    Bukkit.getScheduler().runTask(plugin, () -> {
                                        for (ItemStack item : itemList)
                                            giveMapItem((Player) sender, item);
                                    });
                                }
                            } catch (SQLException exception) {
                                // 資料庫錯誤
                                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.database_error"));
                                exception.printStackTrace();
                            }
                        };
                    }
                } catch (NumberFormatException exception) {
                    // 參數不是數字
                    sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_number") + saveRatios[1]);
                }
            } catch (NumberFormatException exception) {
                // 參數不是數字
                sender.sendMessage(ChatColor.RED + mapServer.lang.get(sender, "command.parameter_not_number") + saveRatios[0]);
            }
        }
        return null;
    }
}
