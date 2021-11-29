package xuan.cat.syncstaticmapview.api.branch;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.data.MapData;

public interface BranchPacket {
    int readEntityIdSpawn(PacketContainer container);

    void sendMapView(Player player, int mapId, MapData mapData);
}
