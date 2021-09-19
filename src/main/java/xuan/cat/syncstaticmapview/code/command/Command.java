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
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;
import xuan.cat.syncstaticmapview.code.MapDatabase;
import xuan.cat.syncstaticmapview.code.MapServer;
import xuan.cat.syncstaticmapview.code.data.ConfigData;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

public final class Command implements CommandExecutor {
    private final Plugin                plugin;
    private final MapDatabase           mapDatabase;
    private final MapServer             mapServer;
    private final ConfigData            configData;
    private final BranchMapConversion   branchMapConversion;
    private final BranchMinecraft       branchMinecraft;

    public Command(Plugin plugin, MapDatabase mapDatabase, MapServer mapServer, ConfigData configData, BranchMapConversion branchMapConversion, BranchMinecraft branchMinecraft) {
        this.plugin                 = plugin;
        this.mapDatabase            = mapDatabase;
        this.mapServer              = mapServer;
        this.configData             = configData;
        this.branchMapConversion    = branchMapConversion;
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
                                    // 已給予
                                    sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("given"));
                                } else {
                                    // 地圖資料不存在
                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("map_data_not_exist"));
                                }
                            } catch (NumberFormatException exception) {
                                // 參數不是數字
                                sender.sendMessage(ChatColor.RED + configData.getLanguage("parameter_not_number"));
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
                                    try {
                                        String stitched = stitchedRight(parameters, 2);
                                        if (stitched.length() == 0) {
                                            // 缺少參數
                                            sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
                                        } else {
                                            URL url = new URL(stitched);
                                            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                                try {
                                                    int mapId = mapDatabase.addMapData(branchMapConversion.ofImage(ImageIO.read(url)));
                                                    // 創建完畢, 資料庫地圖編號為:
                                                    sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("created_successfully") + mapId);
                                                    if (sender instanceof Player) {
                                                        Bukkit.getScheduler().runTask(plugin, () -> giveMapItem((Player) sender, mapId));
                                                    }
                                                } catch (SQLException exception) {
                                                    // 資料庫錯誤
                                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("database_error"));
                                                    exception.printStackTrace();
                                                } catch (IOException exception) {
                                                    // 圖片下載失敗
                                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("image_download_failed" + exception.getMessage()));
                                                }
                                            });
                                        }
                                    } catch (MalformedURLException exception) {
                                        // 輸入的參數不是網址
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("parameter_not_url"));
                                    }
                                }
                                break;
                            case "file":
                                if (!sender.hasPermission("command.map.*") && !sender.hasPermission("command.map.create.*") && !sender.hasPermission("command.map.create.file")) {
                                    // 無權限
                                    sender.sendMessage(ChatColor.RED + configData.getLanguage("no_permission"));
                                } else {
                                    String stitched = stitchedRight(parameters, 2);
                                    if (stitched.length() == 0) {
                                        // 缺少參數
                                        sender.sendMessage(ChatColor.RED + configData.getLanguage("missing_parameters"));
                                    } else {
                                        File file = new File(stitched);
                                        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                            try {
                                                int mapId = mapDatabase.addMapData(branchMapConversion.ofImage(ImageIO.read(file)));
                                                // 創建完畢, 資料庫地圖編號為:
                                                sender.sendMessage(ChatColor.YELLOW + configData.getLanguage("created_successfully") + mapId);
                                                if (sender instanceof Player) {
                                                    Bukkit.getScheduler().runTask(plugin, () -> giveMapItem((Player) sender, mapId));
                                                }
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

    private static String stitchedRight(String[] parameters, int start) {
        StringBuilder builder = new StringBuilder();
        for (int index = start ; index < parameters.length ; index++) {
            if (builder.length() > 0)
                builder.append(' ');
            builder.append(parameters[index]);
        }
        return builder.toString();
    }
}