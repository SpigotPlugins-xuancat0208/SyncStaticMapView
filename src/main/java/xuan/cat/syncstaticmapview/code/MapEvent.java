package xuan.cat.syncstaticmapview.code;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;
import xuan.cat.syncstaticmapview.api.branch.packet.PacketSpawnEntityEvent;

public final class MapEvent implements Listener {
    private final Plugin            plugin;
    private final MapServer         mapServer;
    private final BranchMinecraft   branchMinecraft;


    public MapEvent(Plugin plugin, MapServer mapServer, BranchMinecraft branchMinecraft) {
        this.plugin             = plugin;
        this.mapServer          = mapServer;
        this.branchMinecraft    = branchMinecraft;
    }


    /**
     * @param event 玩家登入
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        mapServer.createCache(player);
        branchMinecraft.injectPlayer(event.getPlayer());
    }

    /**
     * @param event 玩家登出
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        mapServer.cleanCache(player);
    }

    /**
     * @param event 實體生成
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(PacketSpawnEntityEvent event) {
        Player player = event.getPlayer();
        mapServer.trySendMap(player, event.getEntityId());
    }
}
