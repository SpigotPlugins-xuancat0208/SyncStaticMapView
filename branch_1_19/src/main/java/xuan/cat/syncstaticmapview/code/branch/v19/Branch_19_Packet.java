package xuan.cat.syncstaticmapview.code.branch.v19;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.branch.BranchPacket;
import xuan.cat.syncstaticmapview.api.data.MapData;

import java.util.ArrayList;

public final class Branch_19_Packet implements BranchPacket {
    public void sendPacket(Player player, Packet<?> packet) {
        try {
            Connection container = ((CraftPlayer) player).getHandle().connection.connection;
            container.send(packet);
        } catch (IllegalArgumentException ignored) {
        }
    }

    public void sendMapView(Player player, int mapId, MapData mapData) {
        sendPacket(player, new ClientboundMapItemDataPacket(mapId, (byte) 0, false, new ArrayList<>(), new MapItemSavedData.MapPatch(0, 0, 128, 128, mapData.getPixels())));
    }
}
