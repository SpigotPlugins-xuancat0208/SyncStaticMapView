package xuan.cat.syncstaticmapview.code.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xuan.cat.syncstaticmapview.database.api.Database;
import xuan.cat.syncstaticmapview.database.api.sql.DatabaseConnection;
import xuan.cat.syncstaticmapview.database.api.sql.MySQL;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class ConfigData {
    private FileConfiguration fileConfiguration;
    private final JavaPlugin plugin;
    private Map<String, String> languagesMap = new HashMap<>();
    private MySQL mySQL;
    private String databaseName;
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
        String databaseName;
        DatabaseConnection databaseConnection;
        if (database == null)
            throw new NullPointerException("config.yml>database");
        mySQL = Database.createMySQL(database.getString("ip"), database.getInt("port"), database.getString("user"), database.getString("password"));
        databaseName = database.getString("database").toLowerCase(Locale.ROOT);
        databaseConnection = mySQL.getOrCreateDatabase(databaseName);

        Map<String, String> languagesMap = new HashMap<>();
        ConfigurationSection languages = fileConfiguration.getConfigurationSection("languages");
        if (languages == null)
            throw new NullPointerException("config.yml>languages");
        for (String key : languages.getKeys(false)) {
            languagesMap.put(key, languages.getString(key, ""));
        }
        long cacheVitalityTime = fileConfiguration.getLong("cache-vitality-time", 60000L);


        this.mySQL = mySQL;
        this.databaseName = databaseName;
        this.databaseConnection = databaseConnection;
        this.languagesMap = languagesMap;
        this.cacheVitalityTime = cacheVitalityTime;
    }


    public DatabaseConnection getDatabaseConnection() throws SQLException {
        return databaseConnection.isConnected() ? databaseConnection : (databaseConnection = mySQL.getOrCreateDatabase(databaseName));
    }

    public long getCacheVitalityTime() {
        return cacheVitalityTime;
    }
}
