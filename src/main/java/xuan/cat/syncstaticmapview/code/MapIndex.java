package xuan.cat.syncstaticmapview.code;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;
import xuan.cat.syncstaticmapview.api.branch.BranchPacket;
import xuan.cat.syncstaticmapview.code.branch.v17.Branch_17_MapColor;
import xuan.cat.syncstaticmapview.code.branch.v17.Branch_17_MapConversion;
import xuan.cat.syncstaticmapview.code.branch.v17.Branch_17_Minecraft;
import xuan.cat.syncstaticmapview.code.branch.v17.Branch_17_Packet;
import xuan.cat.syncstaticmapview.code.branch.v18.Branch_18_MapColor;
import xuan.cat.syncstaticmapview.code.branch.v18.Branch_18_MapConversion;
import xuan.cat.syncstaticmapview.code.branch.v18.Branch_18_Minecraft;
import xuan.cat.syncstaticmapview.code.branch.v18.Branch_18_Packet;
import xuan.cat.syncstaticmapview.code.branch.v19.Branch_19_MapColor;
import xuan.cat.syncstaticmapview.code.branch.v19.Branch_19_MapConversion;
import xuan.cat.syncstaticmapview.code.branch.v19.Branch_19_Minecraft;
import xuan.cat.syncstaticmapview.code.branch.v19.Branch_19_Packet;
import xuan.cat.syncstaticmapview.code.command.Command;
import xuan.cat.syncstaticmapview.code.command.CommandSuggest;
import xuan.cat.syncstaticmapview.code.data.ConfigData;

public final class MapIndex extends JavaPlugin {

    private static Plugin               plugin;
    private static MapServer            mapServer;
    private static ConfigData           configData;
    private static MapDatabase          mapDatabase;
    private static BranchMapConversion  branchMapConversion;
    private static BranchMapColor       branchMapColor;
    private static BranchMinecraft      branchMinecraft;
    private static BranchPacket         branchPacket;

    @Override
    public void onEnable() {
        plugin          = this;

        try {
            // 檢測版本
            String bukkitVersion = Bukkit.getBukkitVersion();
            if (bukkitVersion.matches("^1\\.17\\D.*$")) {
                // 1.17
                branchMapColor      = new Branch_17_MapColor();
                branchMapConversion = new Branch_17_MapConversion(branchMapColor, branchMapConversion);
                branchMinecraft     = new Branch_17_Minecraft();
                branchPacket        = new Branch_17_Packet();
            } else if (bukkitVersion.matches("^1\\.18\\D.*$")) {
                // 1.18
                branchMapColor      = new Branch_18_MapColor();
                branchMapConversion = new Branch_18_MapConversion(branchMapColor, branchMapConversion);
                branchMinecraft     = new Branch_18_Minecraft();
                branchPacket        = new Branch_18_Packet();
            } else if (bukkitVersion.matches("^1\\.19\\D.*$")) {
                // 1.19
                branchMapColor      = new Branch_19_MapColor();
                branchMapConversion = new Branch_19_MapConversion(branchMapColor, branchMapConversion);
                branchMinecraft     = new Branch_19_Minecraft();
                branchPacket        = new Branch_19_Packet();
            } else {
                throw new IllegalArgumentException("Unsupported MC version: " + bukkitVersion);
            }

            saveDefaultConfig();

            configData      = new ConfigData(this, getConfig());
            mapDatabase     = new MapDatabase(configData, branchMapConversion, branchMapColor);
            mapServer       = new MapServer(this, configData, mapDatabase, branchMapConversion, branchMapColor, branchMinecraft, branchPacket);

        Bukkit.getPluginManager().registerEvents(new MapEvent(this, mapServer, branchMinecraft), this);

        // 指令
        PluginCommand command = getCommand("mapview");
        if (command != null) {
            command.setExecutor(new Command(plugin, mapDatabase, mapServer, configData, branchMapConversion, branchMapColor, branchMinecraft));
            command.setTabCompleter(new CommandSuggest(mapServer, configData));
        }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new NullPointerException("onEnable error");
        }
    }

    public static MapServer getMapServer() {
        return mapServer;
    }

    public static BranchMapColor getBranchMapColor() {
        return branchMapColor;
    }

    public static BranchMapConversion getBranchMapConversion() {
        return branchMapConversion;
    }

    public static BranchMinecraft getBranchMinecraft() {
        return branchMinecraft;
    }

    public static BranchPacket getBranchPacket() {
        return branchPacket;
    }

    public static MapDatabase getMapDatabase() {
        return mapDatabase;
    }

    public static ConfigData getConfigData() {
        return configData;
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public void onDisable() {
        if (mapServer != null)
            mapServer.close();
        if (configData != null)
            try {
                configData.getDatabaseConnection().disconnect();
            } catch (Exception exception) {
            }
    }
}
