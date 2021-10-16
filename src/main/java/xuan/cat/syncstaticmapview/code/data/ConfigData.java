package xuan.cat.syncstaticmapview.code.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xuan.cat.syncstaticmapview.database.Database;
import xuan.cat.syncstaticmapview.database.sql.DatabaseConnection;
import xuan.cat.syncstaticmapview.database.sql.MySQL;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public final class ConfigData {
    private FileConfiguration fileConfiguration;
    private final JavaPlugin plugin;
    private MySQL mySQL;
    private String databaseName;
    private DatabaseConnection databaseConnection;
    public long cacheVitalityTime;
    public boolean createTableCanPartition;
    public int maximumRowAllowed;
    public int maximumColumnAllowed;
    public int defaultPlayerLimit;
    public int urlRateLimit;
    public int createRateLimit;
    public List<String> allowedUrlSourceList;
    public String itemSingleTag;
    public String itemMultipleTag;


    public ConfigData(JavaPlugin plugin, FileConfiguration fileConfiguration) throws SQLException {
        this.plugin = plugin;
        this.fileConfiguration = fileConfiguration;
        load();
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
        long cacheVitalityTime = fileConfiguration.getLong("cache-vitality-time", 60000L);
        int maximumRowAllowed = fileConfiguration.getInt("maximum-row-allowed", 8);
        int maximumColumnAllowed = fileConfiguration.getInt("maximum-column-allowed", 8);
        int defaultPlayerLimit = fileConfiguration.getInt("default-player-limit", 400);
        int urlRateLimit = fileConfiguration.getInt("url-rate-limit", 4);
        int createRateLimit = fileConfiguration.getInt("create-rate-limit", 2000);
        List<String> allowedUrlSourceList = fileConfiguration.getStringList("allowed-url-source-list");
        String itemSingleTag = fileConfiguration.getString("create-rate-limit", "{map:%mapview.id%}");
        String itemMultipleTag = fileConfiguration.getString("create-rate-limit", "{display:{Lore:['{\"extra\":[{\"italic\":false,\"color\":\"white\",\"text\":\"%mapview.column%-%mapview.row%\"}],\"text\":\"\"}']},map:%mapview.id%}");


        DatabaseConnection databaseConnection = mySQL.getOrCreateDatabase(databaseName);

        this.mySQL = mySQL;
        this.createTableCanPartition = createTableCanPartition;
        this.databaseName = databaseName;
        this.databaseConnection = databaseConnection;
        this.cacheVitalityTime = cacheVitalityTime;
        this.maximumRowAllowed = maximumRowAllowed;
        this.maximumColumnAllowed = maximumColumnAllowed;
        this.defaultPlayerLimit = defaultPlayerLimit;
        this.urlRateLimit = urlRateLimit;
        this.createRateLimit = createRateLimit;
        this.allowedUrlSourceList = allowedUrlSourceList;
        this.itemSingleTag = itemSingleTag;
        this.itemMultipleTag = itemMultipleTag;
    }


    public DatabaseConnection getDatabaseConnection() throws SQLException {
        return databaseConnection.isConnected() ? databaseConnection : (databaseConnection = mySQL.getOrCreateDatabase(databaseName));
    }
}
