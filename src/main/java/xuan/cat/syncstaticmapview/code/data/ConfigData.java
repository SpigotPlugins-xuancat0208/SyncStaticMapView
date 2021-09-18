package xuan.cat.syncstaticmapview.code.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xuan.cat.databasecatmini.api.Database;
import xuan.cat.databasecatmini.api.sql.DatabaseConnection;
import xuan.cat.databasecatmini.api.sql.MySQL;

import java.sql.SQLException;
import java.util.*;

public final class ConfigData {
    private FileConfiguration fileConfiguration;
    private final JavaPlugin plugin;
    private Map<String, String> languagesMap = new HashMap<>();
    private MySQL mySQL;
    private DatabaseConnection databaseConnection;
    private long cacheVitalityTime;

    public ConfigData(JavaPlugin plugin, FileConfiguration fileConfiguration) throws SQLException {
        this.plugin = plugin;
        this.fileConfiguration = fileConfiguration;
        load();
    }

    public String getLanguage(String key) {
        return languagesMap.computeIfAbsent(key, v -> "");
    }

    public void reload() throws SQLException {
        plugin.reloadConfig();
        fileConfiguration = plugin.getConfig();
        load();
    }

    private void load() throws SQLException {
        ConfigurationSection database = fileConfiguration.getConfigurationSection("database");
        MySQL mySQL;
        DatabaseConnection databaseConnection;
        if (database == null)
            throw new NullPointerException("config.yml>database");
        mySQL = Database.createMySQL(database.getString("ip"), database.getInt("port"), database.getString("user"), database.getString("password"));
        databaseConnection = mySQL.getOrCreateDatabase(database.getString("database").toLowerCase(Locale.ROOT));

        Map<String, String> languagesMap = new HashMap<>();
        ConfigurationSection languages = fileConfiguration.getConfigurationSection("languages");
        if (languages == null)
            throw new NullPointerException("config.yml>languages");
        for (String key : languages.getKeys(false)) {
            languagesMap.put(key, languages.getString(key, ""));
        }
        long cacheVitalityTime = fileConfiguration.getLong("cache-vitality-time", 60000L);


        this.mySQL = mySQL;
        this.databaseConnection = databaseConnection;
        this.languagesMap = languagesMap;
        this.cacheVitalityTime = cacheVitalityTime;
    }


    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public long getCacheVitalityTime() {
        return cacheVitalityTime;
    }
}
