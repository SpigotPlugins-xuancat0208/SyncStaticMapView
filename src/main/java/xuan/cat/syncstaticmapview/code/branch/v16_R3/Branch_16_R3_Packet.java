package xuan.cat.syncstaticmapview.code.branch.v16_R3;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import net.minecraft.server.v1_16_R3.PacketPlayOutMap;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.branch.BranchPacket;
import xuan.cat.syncstaticmapview.api.data.MapData;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public final class Branch_16_R3_Packet implements BranchPacket {
    private final ProtocolManager protocolManager     = ProtocolLibrary.getProtocolManager();


    public int readEntityIdSpawn(PacketContainer container) {
        return container.getIntegers().read(0);
    }


    public void sendPacket(Player player, PacketContainer container) {
        try {
            protocolManager.sendServerPacket(player, container, true);
        } catch (IllegalArgumentException ignored) {
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }


    public void sendMapView(Player player, int mapId, MapData mapData) {
        sendPacket(player, PacketContainer.fromPacket(new PacketPlayOutMap(mapId, (byte) 0, false, false, new ArrayList<>(), mapData.getPixels(), 0, 0, 128, 128)));
    }
}
