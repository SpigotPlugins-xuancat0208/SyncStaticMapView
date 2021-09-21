package xuan.cat.syncstaticmapview.code;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xuan.cat.syncstaticmapview.api.branch.BranchMapColor;
import xuan.cat.syncstaticmapview.api.branch.BranchMapConversion;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;
import xuan.cat.syncstaticmapview.api.branch.BranchPacket;
import xuan.cat.syncstaticmapview.code.branch.CodeBranchMapColor;
import xuan.cat.syncstaticmapview.code.branch.CodeBranchMapConversion;
import xuan.cat.syncstaticmapview.code.branch.CodeBranchMinecraft;
import xuan.cat.syncstaticmapview.code.branch.CodeBranchPacket;
import xuan.cat.syncstaticmapview.code.command.Command;
import xuan.cat.syncstaticmapview.code.command.CommandSuggest;
import xuan.cat.syncstaticmapview.code.data.ConfigData;

import java.io.*;

public final class Index extends JavaPlugin {

    private static ProtocolManager      protocolManager;
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
            if        (bukkitVersion.matches("1\\.17.*\\-R0\\.1.*")) {
                // 1.17
                branchMapColor      = new CodeBranchMapColor();
                branchMapConversion = new CodeBranchMapConversion(branchMapColor, branchMapConversion);
                branchMinecraft     = new CodeBranchMinecraft();
                branchPacket        = new CodeBranchPacket();
            } else {
                throw new NullPointerException("Unsupported MC version");
            }

            protocolManager = ProtocolLibrary.getProtocolManager();

            saveDefaultConfig();
            saveResource("config_en.yml",       false);
            saveResource("config_zh-tw.yml",    false);
            saveResource("config_zh-cn.yml",    false);

            configData      = new ConfigData(this, getConfig());
            mapDatabase     = new MapDatabase(configData, branchMapConversion, branchMapColor);
            mapServer       = new MapServer(this, configData, mapDatabase, branchMapConversion, branchMapColor, branchMinecraft, branchPacket);

        Bukkit.getPluginManager().registerEvents(new MapEvent(this, mapServer), this);
        protocolManager.addPacketListener(new MapPacketEvent(plugin, mapServer));

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
        mapServer.close();
        try {
            configData.getDatabaseConnection().disconnect();
        } catch (Exception exception) {
        }
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = this.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + this.getFile());
            } else {
                File outFile = new File(this.getDataFolder(), resourcePath);
                int lastIndex = resourcePath.lastIndexOf(47);
                File outDir = new File(this.getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (outFile.exists() && !replace) {
                    } else {
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException var10) {
                    //this.logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }
}
