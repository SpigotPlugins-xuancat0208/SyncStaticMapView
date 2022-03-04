package xuan.cat.syncstaticmapview.api.branch;

import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.data.MapData;

public interface BranchPacket {
    void sendMapView(Player player, int mapId, MapData mapData);
}
