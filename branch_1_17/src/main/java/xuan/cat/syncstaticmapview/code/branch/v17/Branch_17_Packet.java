package xuan.cat.syncstaticmapview.code.branch.v17;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutMap;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.level.saveddata.maps.WorldMap;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.branch.BranchPacket;
import xuan.cat.syncstaticmapview.api.data.MapData;

import java.util.ArrayList;

public final class Branch_17_Packet implements BranchPacket {
    public void sendPacket(Player player, Packet<?> packet) {
        try {
            PlayerConnection container = ((CraftPlayer) player).getHandle().b;
            container.sendPacket(packet);
        } catch (IllegalArgumentException ignored) {
        }
    }

    public void sendMapView(Player player, int mapId, MapData mapData) {
        sendPacket(player, new PacketPlayOutMap(mapId, (byte) 0, false, new ArrayList<>(), new WorldMap.b(0, 0, 128, 128, mapData.getPixels())));
    }
}
