package xuan.cat.syncstaticmapview.code.branch.v19;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.channel.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Branch_19_Minecraft implements BranchMinecraft {
    private final Field field_CraftItemStack_handle;
    private final Field field_ItemStack_tag;


   public Branch_19_Minecraft() throws NoSuchFieldException {
       field_CraftItemStack_handle = CraftItemStack.class.getDeclaredField("handle"                                      );
       field_ItemStack_tag = ItemStack.class.getDeclaredField("v"); // TODO tag
       field_CraftItemStack_handle.setAccessible(true);
       field_ItemStack_tag.setAccessible(true);
   }


    /**
     * 參考 XuanCatAPI.CodeExtendWorld.getEntity
     */
    public org.bukkit.entity.Entity getEntityFromId(World world, int entityId) {
        Entity entity = ((CraftWorld) world).getHandle().getEntity(entityId);
        return entity != null ? entity.getBukkitEntity() : null;
    }


    /**
     * 參考 XuanCatAPI.CodeNBTCompound.getBoolean
     */
    public boolean isDisableCopy(org.bukkit.inventory.ItemStack item) {
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        try {
            ItemStack itemNMS = (ItemStack) field_CraftItemStack_handle.get(craftItem);
            CompoundTag nbt = (CompoundTag) field_ItemStack_tag.get(itemNMS);
            return nbt.getBoolean("AntiMapCopy");
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }


    /**
     * 參考 XuanCatAPI.CodeNBTCompound.getInt
     */
    public int getMapId(org.bukkit.inventory.ItemStack item) {
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        try {
            ItemStack itemNMS = (ItemStack) field_CraftItemStack_handle.get(craftItem);
            CompoundTag nbt = (CompoundTag) field_ItemStack_tag.get(itemNMS);
            return nbt.getInt("map");
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    /**
     * 參考 XuanCatAPI.CodeExtendEntity.getTracking
     */
    public List<Player> getTracking(org.bukkit.entity.Entity entity) {
        List<Player> playerList = new ArrayList<>();
        ChunkMap.TrackedEntity tracker =((CraftEntity) entity).getHandle().tracker;
        if (tracker != null) {
            for (ServerPlayerConnection seen : tracker.seenBy) {
                playerList.add(seen.getPlayer().getBukkitEntity());
            }
        }
        return playerList;
    }

    /**
     * 參考 XuanCatAPI.CodeNBT.parseTag
     */
    public org.bukkit.inventory.ItemStack saveItemNBT(org.bukkit.inventory.ItemStack item, String nbt) throws CommandSyntaxException {
        CompoundTag tag = TagParser.parseTag(nbt);
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        try {
            ItemStack itemNMS = (ItemStack) field_CraftItemStack_handle.get(craftItem);
            field_ItemStack_tag.set(itemNMS, tag);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return craftItem;
    }

    public void injectPlayer(Player player) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        ServerGamePacketListenerImpl connection = entityPlayer.connection;
        Channel channel = connection.connection.channel;
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addAfter("packet_handler", "sync_static_map_view_write", new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (msg instanceof Packet) {
                    if (!Branch_19_ProxyPlayerConnection.write(player, (Packet<?>) msg))
                        return;
                }
                super.write(ctx, msg, promise);
            }
        });
        pipeline.addAfter("encoder", "sync_static_map_view_read", new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof Packet) {
                    if (!Branch_19_ProxyPlayerConnection.read(player, (Packet<?>) msg))
                        return;
                }
                super.channelRead(ctx, msg);
            }
        });
    }
}
