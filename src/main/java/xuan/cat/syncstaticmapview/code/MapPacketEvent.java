package xuan.cat.syncstaticmapview.code;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MapPacketEvent extends PacketAdapter {
    private final MapServer mapServer;


    public MapPacketEvent(Plugin plugin, MapServer mapServer) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.REMOVE_ENTITY_EFFECT);
        this.mapServer    = mapServer;
    }


    public void onPacketSending(PacketEvent event) {
        if (event.isCancelled() || event.isReadOnly())
            return;

        Player player      = event.getPlayer();
        PacketType      packetType  = event.getPacketType();

        if (packetType == PacketType.Play.Server.SPAWN_ENTITY) {
            // 發送地圖資料
            mapServer.trySendMap(player, event);
        }
    }

    public void onPacketReceiving(PacketEvent event) {
    }
}
