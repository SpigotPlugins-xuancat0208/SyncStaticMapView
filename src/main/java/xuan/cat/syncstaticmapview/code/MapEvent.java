package xuan.cat.syncstaticmapview.code;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public final class MapEvent implements Listener {
    private final Plugin    plugin;
    private final MapServer mapServer;


    public MapEvent(Plugin plugin, MapServer mapServer) {
        this.plugin     = plugin;
        this.mapServer  = mapServer;
    }


    /**
     * @param event 玩家登入
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        mapServer.createCache(player);
    }

    /**
     * @param event 玩家登出
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        mapServer.cleanCache(player);
    }
}
