package xuan.cat.syncstaticmapview.code.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xuan.cat.syncstaticmapview.database.api.Database;
import xuan.cat.syncstaticmapview.database.api.sql.DatabaseConnection;
import xuan.cat.syncstaticmapview.database.api.sql.MySQL;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
    private boolean createTableCanPartition;
    private int maximumRowAllowed;
    private int maximumColumnAllowed;
    private int defaultPlayerLimit;
    private int urlRateLimit;
    private int createRateLimit;
    private List<String> allowedUrlSourceList;

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
        if (database == null)
            throw new NullPointerException("config.yml>database");
        MySQL mySQL = Database.createMySQL(database.getString("ip"), database.getInt("port"), database.getString("user"), database.getString("password"));
        String databaseName;
        try {
            databaseName = database.getString("database").toLowerCase(Locale.ROOT);
        } catch (NullPointerException exception) {
            throw new NullPointerException("config.yml>database->database");
        }

        boolean createTableCanPartition = fileConfiguration.getBoolean("create-table-can-partition", true);

        Map<String, String> languagesMap = new HashMap<>();
        ConfigurationSection languages = fileConfiguration.getConfigurationSection("languages");
        if (languages == null)
            throw new NullPointerException("config.yml>languages");
        for (String key : languages.getKeys(false)) {
            languagesMap.put(key, languages.getString(key, ""));
        }

        long cacheVitalityTime = fileConfiguration.getLong("cache-vitality-time", 60000L);
        int maximumRowAllowed = fileConfiguration.getInt("maximum-row-allowed", 8);
        int maximumColumnAllowed = fileConfiguration.getInt("maximum-column-allowed", 8);
        int defaultPlayerLimit = fileConfiguration.getInt("default-player-limit", 400);
        int urlRateLimit = fileConfiguration.getInt("url-rate-limit", 4);
        int createRateLimit = fileConfiguration.getInt("create-rate-limit", 2000);
        List<String> allowedUrlSourceList = fileConfiguration.getStringList("allowed-url-source-list");


        DatabaseConnection databaseConnection = mySQL.getOrCreateDatabase(databaseName);

        this.mySQL = mySQL;
        this.createTableCanPartition = createTableCanPartition;
        this.databaseName = databaseName;
        this.databaseConnection = databaseConnection;
        this.languagesMap = languagesMap;
        this.cacheVitalityTime = cacheVitalityTime;
        this.maximumRowAllowed = maximumRowAllowed;
        this.maximumColumnAllowed = maximumColumnAllowed;
        this.defaultPlayerLimit = defaultPlayerLimit;
        this.urlRateLimit = urlRateLimit;
        this.createRateLimit = createRateLimit;
        this.allowedUrlSourceList = allowedUrlSourceList;
    }


    public DatabaseConnection getDatabaseConnection() throws SQLException {
        return databaseConnection.isConnected() ? databaseConnection : (databaseConnection = mySQL.getOrCreateDatabase(databaseName));
    }

    public long getCacheVitalityTime() {
        return cacheVitalityTime;
    }

    public boolean isCreateTableCanPartition() {
        return createTableCanPartition;
    }

    public int getDefaultPlayerLimit() {
        return defaultPlayerLimit;
    }

    public int getMaximumColumnAllowed() {
        return maximumColumnAllowed;
    }

    public int getMaximumRowAllowed() {
        return maximumRowAllowed;
    }

    public int getUrlRateLimit() {
        return urlRateLimit;
    }

    public List<String> getAllowedUrlSourceList() {
        return allowedUrlSourceList;
    }

    public int getCreateRateLimit() {
        return createRateLimit;
    }
}
