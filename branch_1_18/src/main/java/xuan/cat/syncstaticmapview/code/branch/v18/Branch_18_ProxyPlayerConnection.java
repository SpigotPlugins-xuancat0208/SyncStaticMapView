package xuan.cat.syncstaticmapview.code.branch.v18;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import xuan.cat.syncstaticmapview.api.branch.packet.PacketSpawnEntityEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public final class Branch_18_ProxyPlayerConnection extends PlayerConnection {
    public static final Set<Field> fields_ServerGamePacketListenerImpl = new HashSet<>();
    static {
        for (Field field : PlayerConnection.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                fields_ServerGamePacketListenerImpl.add(field);
            }
        }
    }

    public Branch_18_ProxyPlayerConnection(PlayerConnection connection, EntityPlayer player) {
        super(((CraftServer) Bukkit.getServer()).getServer(), connection.a, player);
        try {
            for (Field field : fields_ServerGamePacketListenerImpl) {
                field.set(this, field.get(connection));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static Field field_PacketPlayOutSpawnEntity_entityId;
    static {
        try {
            field_PacketPlayOutSpawnEntity_entityId = PacketPlayOutSpawnEntity.class.getDeclaredField("c");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void a(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> listener) {
        try {
            if (packet instanceof PacketPlayOutSpawnEntity) {
                PacketSpawnEntityEvent event = new PacketSpawnEntityEvent(getCraftPlayer(), field_PacketPlayOutSpawnEntity_entityId.getInt(packet));
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled())
                    return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.a(packet, listener);
    }
}
