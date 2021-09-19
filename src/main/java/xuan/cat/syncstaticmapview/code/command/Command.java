package xuan.cat.syncstaticmapview.code.command;

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
import xuan.cat.syncstaticmapview.code.data.CodeMapData;
import xuan.cat.syncstaticmapview.code.data.ConfigData;
import xuan.cat.syncstaticmapview.code.data.MapRedirectEntry;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class Command implements CommandExecutor {
    private final Plugin                plugin;
    private final MapDatabase           mapDatabase;
    private final MapServer             mapServer;
    private final ConfigData            configData;
    private final BranchMapConversion   branchMapConversion;
    private final BranchMapColor        branchMapColor;
    private final BranchMinecraft       branchMinecraft;

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
            sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
        } else {
            switch (parameters[0]) {
                case "reload":
                    // 重新讀取配置文件
                    if (!sender.hasPermission("command.map.*") && !sender.hasPermission("command.map.reload")) {
                        // 無權限
                        sender.sendMessage(ChatColor.RED + configData.getLanguage("no_permission"));
                    } else {
                        try {
                            configData.reload();
                            // 已重新讀取設定
                            sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("reread_configuration_successfully"));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // 重新讀取設定時發生錯誤
                            sender.sendMessage(ChatColor.RED + configData.getLanguage("reread_configuration_error"));
                        }
                    }
                    break;

                case "get":
                    // 重新讀取配置文件
                    if (!sender.hasPermission("command.map.*") && !sender.hasPermission("command.map.get")) {
                        // 無權限
                        sender.sendMessage(ChatColor.RED + configData.getLanguage("no_permission"));
                    } else if (!(sender instanceof Player)) {
                        // 只能以玩家身分執行此指令
                        sender.sendMessage(ChatColor.RED + configData.getLanguage("only_be_used_by_player"));
                    } else {
                        Player player = (Player) sender;
                        if (parameters.length >= 2) {
                            try {
                                int mapId = Integer.parseInt(parameters[1]);
                                if (mapDatabase.existMapData(mapId)) {
                                    giveMapItem(player, mapId);
                                    // 已給予: ?
                                    sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("given") + mapId);
                                } else {
                                    // 地圖資料不存在: ?
                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("map_data_not_exist") + mapId);
                                }
                            } catch (NumberFormatException exception) {
                                // 參數不是數字
                                sender.sendMessage(ChatColor.RED + configData.getLanguage("parameter_not_number") + parameters[1]);
                            } catch (SQLException exception) {
                                // 資料庫錯誤
                                sender.sendMessage(ChatColor.RED + configData.getLanguage("database_error"));
                                exception.printStackTrace();
                            }
                        } else {
                            // 缺少參數
                            sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
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
                                if (!sender.hasPermission("command.map.*") && !sender.hasPermission("command.map.create.*") && !sender.hasPermission("command.map.create.url")) {
                                    // 無權限
                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("no_permission"));
                                } else {
                                    if (parameters.length >= 4) {
                                        String stitched = stitchedRight(parameters, 3);
                                        if (stitched.length() == 0) {
                                            // 缺少參數
                                            sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
                                        } else {
                                            try {
                                                URL url = new URL(stitched);
                                                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                                    try {
                                                        cropImageSave(sender, ImageIO.read(url), parameters[2]);
                                                    } catch (SQLException exception) {
                                                        // 資料庫錯誤
                                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("database_error"));
                                                        exception.printStackTrace();
                                                    } catch (IOException exception) {
                                                        // 圖片下載失敗
                                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("image_download_failed") + exception.getMessage());
                                                    }
                                                });
                                            } catch (MalformedURLException exception) {
                                                // 輸入的參數不是網址
                                                sender.sendMessage(ChatColor.RED + configData.getLanguage("parameter_not_url") + stitched);
                                            }
                                        }
                                    } else {
                                        // 缺少參數
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
                                    }
                                }
                                break;
                            case "file":
                                if (!sender.hasPermission("command.map.*") && !sender.hasPermission("command.map.create.*") && !sender.hasPermission("command.map.create.file")) {
                                    // 無權限
                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("no_permission"));
                                } else {
                                    if (parameters.length >= 4) {
                                        String stitched = stitchedRight(parameters, 3);
                                        if (stitched.length() == 0) {
                                            // 缺少參數
                                            sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
                                        } else {
                                            File file = new File(stitched);
                                            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                                try {
                                                    cropImageSave(sender, ImageIO.read(file), parameters[2]);
                                                } catch (SQLException exception) {
                                                    // 資料庫錯誤
                                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("database_error"));
                                                    exception.printStackTrace();
                                                } catch (IOException exception) {
                                                    // 圖片讀取失敗
                                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("image_read_failed") + exception.getMessage());
                                                }
                                            });
                                        }
                                    } else {
                                        // 缺少參數
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
                                    }
                                }
                                // 文件
                                break;
                            case "hand":
                                if (!sender.hasPermission("command.map.*") && !sender.hasPermission("command.map.create.*") && !sender.hasPermission("command.map.create.hand")) {
                                    // 無權限
                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("no_permission"));
                                } else if (!(sender instanceof Player)) {
                                    // 只能以玩家身分執行此指令
                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("only_be_used_by_player"));
                                } else {
                                    Player player = (Player) sender;
                                    ItemStack item = player.getInventory().getItemInMainHand();
                                    if (item.getType() == Material.FILLED_MAP) {
                                        try {
                                            MapMeta mapMeta = (MapMeta) item.getItemMeta();
                                            MapView mapView = mapMeta.hasMapView() ? mapMeta.getMapView() : null;
                                            if (mapView != null) {
                                                int mapId = mapDatabase.addMapData(branchMapConversion.ofBukkit(mapView));
                                                // 創建完畢, 資料庫地圖編號為:
                                                sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("created_successfully") + mapId);
                                                giveMapItem(player, mapId);
                                            } else {
                                                // 此地圖沒有原始資料
                                                sender.sendMessage(ChatColor.RED + configData.getLanguage("map_no_original_data"));
                                            }
                                        } catch (SQLException exception) {
                                            // 資料庫錯誤
                                            sender.sendMessage(ChatColor.RED + configData.getLanguage("database_error"));
                                            exception.printStackTrace();
                                        }
                                    } else {
                                        // 手上拿著的物品不是地圖
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("not_filled_map_on_hand"));
                                    }
                                }
                                // 手上
                                break;
                            default:
                                // 未知的參數類型
                                sender.sendMessage(ChatColor.RED + configData.getLanguage("unknown_parameter_type") + " " + parameters[1]);
                                break;
                        }
                    } else {
                        // 缺少參數
                        sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
                    }
                    break;

                case "redirect":
                    // 重定向
                    if (parameters.length >= 3) {
                        try {
                            int mapId = Integer.parseInt(parameters[1]);
                            switch (parameters[2]) {
                                case "list":
                                    if (!sender.hasPermission("command.map.*") && !sender.hasPermission("command.map.redirect.*") && !sender.hasPermission("command.map.redirect.list")) {
                                        // 無權限
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("no_permission"));
                                    } else if (!mapDatabase.existMapData(mapId)) {
                                        // 地圖資料不存在: ?
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("map_data_not_exist") + mapId);
                                    } else {
                                        List<MapRedirectEntry> redirectEntries = mapServer.cacheMapRedirects(mapId).redirects;
                                        if (redirectEntries.size() == 0) {
                                            // 沒有設置任何的重定向
                                            sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("not_set_any_redirect"));
                                        } else {
                                            // 重定向清單
                                            sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("redirect_list"));
                                            for (MapRedirectEntry redirect : redirectEntries) {
                                                sender.sendMessage(" - " + ChatColor.YELLOW + redirect.getPriority() + ": " + redirect.getPermission() + " => " + redirect.getRedirectId());
                                            }
                                        }
                                    }
                                    break;

                                case "set":
                                    if (!sender.hasPermission("command.map.*") && !sender.hasPermission("command.map.redirect.*") && !sender.hasPermission("command.map.redirect.set")) {
                                        // 無權限
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("no_permission"));
                                    } else if (!mapDatabase.existMapData(mapId)) {
                                        // 地圖資料不存在: ?
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("map_data_not_exist") + mapId);
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
                                                        sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("redirect_set_successfully"));
                                                    } else {
                                                        // 地圖資料不存在
                                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("map_data_not_exist") + redirect.getRedirectId());
                                                    }
                                                } catch (NumberFormatException exception) {
                                                    // 參數不是數字
                                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("parameter_not_number") + parameters[5]);
                                                }
                                            } catch (NumberFormatException exception) {
                                                // 參數不是數字
                                                sender.sendMessage(ChatColor.RED + configData.getLanguage("parameter_not_number") + parameters[3]);
                                            }
                                        } else {
                                            // 缺少參數
                                            sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
                                        }
                                    }
                                    break;

                                case "delete":
                                    if (!sender.hasPermission("command.map.*") && !sender.hasPermission("command.map.redirect.*") && !sender.hasPermission("command.map.redirect.delete")) {
                                        // 無權限
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("no_permission"));
                                    } else if (!mapDatabase.existMapData(mapId)) {
                                        // 地圖資料不存在: ?
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("map_data_not_exist") + mapId);
                                    } else {
                                        if (parameters.length >= 4) {
                                            mapDatabase.removeMapRedirect(mapId, parameters[3]);
                                            mapServer.createCache(mapId);
                                            mapDatabase.markMapUpdate(mapId);
                                            // 重導向刪除成功
                                            sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("redirect_delete_successfully"));
                                        } else {
                                            // 缺少參數
                                            sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
                                        }
                                    }
                                    break;

                                case "delete_all":
                                    if (!sender.hasPermission("command.map.*") && !sender.hasPermission("command.map.redirect.*") && !sender.hasPermission("command.map.redirect.delete_all")) {
                                        // 無權限
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("no_permission"));
                                    } else if (!mapDatabase.existMapData(mapId)) {
                                        // 地圖資料不存在: ?
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("map_data_not_exist") + mapId);
                                    } else {
                                        mapDatabase.removeMapRedirect(mapId);
                                        mapServer.createCache(mapId);
                                        mapDatabase.markMapUpdate(mapId);
                                        // 重導向刪除成功
                                        sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("redirect_delete_successfully"));
                                    }
                                    break;

                                default:
                                    // 未知的參數類型
                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("unknown_parameter_type") + " " + parameters[0]);
                                    break;
                            }
                        } catch (NumberFormatException exception) {
                            // 參數不是數字
                            sender.sendMessage(ChatColor.RED + configData.getLanguage("parameter_not_number") + parameters[1]);
                        } catch (SQLException exception) {
                            // 資料庫錯誤
                            sender.sendMessage(ChatColor.RED + configData.getLanguage("database_error"));
                            exception.printStackTrace();
                        }
                    } else {
                        // 缺少參數
                        sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
                    }
                    break;

                default:
                    // 未知的參數類型
                    sender.sendMessage(ChatColor.RED + configData.getLanguage("unknown_parameter_type") + " " + parameters[0]);
                    break;
            }
        }

        return true;
    }


    public void giveMapItem(Player player, int mapId) {
        if (player.isOnline())
            for (ItemStack item : player.getInventory().addItem(branchMinecraft.setMapId(new ItemStack(Material.FILLED_MAP), -mapId)).values())
                player.getWorld().dropItem(player.getLocation(), item).setOwner(player.getUniqueId());
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

    public void cropImageSave(CommandSender sender, BufferedImage sourceImage, String saveRatio) throws SQLException {
        int revisionWidth = sourceImage.getWidth();
        int revisionHeight = sourceImage.getHeight();

        if (saveRatio == null || saveRatio.length() == 0)
            saveRatio = "1:1";
        String[] saveRatios = saveRatio.split(":", 2);
        if (saveRatios.length != 2) {
            // 參數不是長寬比:
            sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("parameter_not_aspect_ratio") + saveRatio);
        } else {
            try {
                int spaceRow = saveRatios[0].length() == 0 ? (int) Math.ceil((double) revisionWidth / 128.0 - 2.0) : Integer.parseInt(saveRatios[0]);
                int spaceWidth = spaceRow * 128;
                try {
                    int spaceColumn = saveRatios[1].length() == 0 ? (int) Math.ceil((double) revisionHeight / 128.0 - 2.0) : Integer.parseInt(saveRatios[1]);
                    int spaceHeight = spaceColumn * 128;

                    if (revisionWidth < spaceWidth) {
                        double redress = (double) revisionWidth / (double) spaceWidth;
                        revisionWidth = spaceWidth;
                        revisionHeight = (int) (revisionHeight * redress);
                    } else if (revisionHeight < spaceHeight) {
                        double redress = (double) revisionHeight / (double) spaceHeight;
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

                    Set<Integer> createIds = new LinkedHashSet<>();
                    for (int readRow = 0 ; readRow < spaceRow ; readRow++) {
                        for (int readColumn = 0 ; readColumn < spaceColumn ; readColumn++) {
                            MapData mapData = new CodeMapData(branchMapColor, branchMapConversion);
                            for (int x = 0 ; x < 128 ; x++)
                                for (int y = 0 ; y < 128 ; y++)
                                    mapData.setColor(x, y, new Color(spaceImage.getRGB(readRow << 7 | x, readColumn << 7 | y), true));
                            int mapId = mapDatabase.addMapData(mapData);
                            createIds.add(mapId);
                        }
                    }

                    StringBuilder stringIds = new StringBuilder();

                    for (int createId : createIds) {
                        if (stringIds.length() != 0)
                            stringIds.append(',').append(' ');
                        stringIds.append(createId);
                    }

                    // 創建完畢, 資料庫地圖編號為:
                    sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("created_successfully") + stringIds);
                    if (sender instanceof Player) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            for (int createId : createIds) {
                                giveMapItem((Player) sender, createId);
                            }
                        });
                    }

                } catch (NumberFormatException exception) {
                    // 參數不是數字
                    sender.sendMessage(ChatColor.RED + configData.getLanguage("parameter_not_number") + saveRatios[1]);
                }
            } catch (NumberFormatException exception) {
                // 參數不是數字
                sender.sendMessage(ChatColor.RED + configData.getLanguage("parameter_not_number") + saveRatios[0]);
            }
        }
    }
}
