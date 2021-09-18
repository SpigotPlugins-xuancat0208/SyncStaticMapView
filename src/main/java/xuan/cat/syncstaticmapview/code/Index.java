package xuan.cat.syncstaticmapview.code;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class Index extends JavaPlugin {

    private static ProtocolManager protocolManager;
    private static Plugin           plugin;
//    private static ChunkServer      chunkServer;
//    private static ConfigData       configData;
//    private static BranchPacket     branchPacket;
//    private static BranchMinecraft  branchMinecraft;

    @Override
    public void onEnable() {
        plugin          = this;


        // 檢測版本
        String bukkitVersion = Bukkit.getBukkitVersion();
        if        (bukkitVersion.matches("1\\.17.*\\-R0\\.1.*")) {
            // 1.17
//            branchPacket    = new CodeBranchPacket();
//            branchMinecraft = new CodeBranchMinecraft();
        } else {
            throw new NullPointerException("Unsupported MC version");
        }


        protocolManager = ProtocolLibrary.getProtocolManager();

        Bukkit.getMap(Integer.MAX_VALUE);
        

//        saveDefaultConfig();
//        saveResource("config_en.yml",       false);
//        saveResource("config_zh-tw.yml",    false);
//        saveResource("config_zh-cn.yml",    false);

//        configData      = new ConfigData(this, getConfig());
//        chunkServer     = new ChunkServer(configData, this, branchMinecraft, branchPacket);
//
//        Bukkit.getPluginManager().registerEvents(new ChunkEvent(chunkServer, branchPacket), this);
//        protocolManager.addPacketListener(new ChunkPacketEvent(plugin, chunkServer));
//
//        // 指令
//        PluginCommand command = getCommand("viewdistance");
//        if (command != null) {
//            command.setExecutor(new Command(chunkServer, configData));
//            command.setTabCompleter(new CommandSuggest(chunkServer, configData));
//        }
    }

//    public static ChunkServer getChunkServer() {
//        return chunkServer;
//    }
//
//    public static ConfigData getConfigData() {
//        return configData;
//    }

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
//        chunkServer.close();
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
