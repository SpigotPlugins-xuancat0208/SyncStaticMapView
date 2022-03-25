package xuan.cat.syncstaticmapview.code.branch.v17;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.branch.packet.PacketSpawnEntityEvent;

import java.lang.reflect.Field;

public final class Branch_17_ProxyPlayerConnection {
    public static boolean read(Player player, Packet<?> packet) {
        return true;
    }


    private static Field field_PacketPlayOutSpawnEntity_entityId;
    static {
        try {
            field_PacketPlayOutSpawnEntity_entityId = PacketPlayOutSpawnEntity.class.getDeclaredField("c");
            field_PacketPlayOutSpawnEntity_entityId.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static boolean write(Player player, Packet<?> packet) {
        try {
            if (packet instanceof PacketPlayOutSpawnEntity) {
                PacketSpawnEntityEvent event = new PacketSpawnEntityEvent(player, field_PacketPlayOutSpawnEntity_entityId.getInt(packet));
                Bukkit.getPluginManager().callEvent(event);
                return !event.isCancelled();
            } else {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }
}
