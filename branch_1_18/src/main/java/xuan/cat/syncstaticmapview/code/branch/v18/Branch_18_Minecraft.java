package xuan.cat.syncstaticmapview.code.branch.v18;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.channel.*;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Branch_18_Minecraft implements BranchMinecraft {
    private final Field field_CraftItemStack_handle;
    private final Field field_ItemStack_tag;


   public Branch_18_Minecraft() throws NoSuchFieldException {
       field_CraftItemStack_handle = CraftItemStack.class.getDeclaredField("handle");
       field_ItemStack_tag = ItemStack.class.getDeclaredField("u");
       field_CraftItemStack_handle.setAccessible(true);
       field_ItemStack_tag.setAccessible(true);
   }


    /**
     * 參考 XuanCatAPI.CodeExtendWorld.getEntity
     */
    public org.bukkit.entity.Entity getEntityFromId(World world, int entityId) {
        Entity entity = ((CraftWorld) world).getHandle().a(entityId);
        return entity != null ? entity.getBukkitEntity() : null;
    }


    /**
     * 參考 XuanCatAPI.CodeNBTCompound.getBoolean
     */
    public boolean isDisableCopy(org.bukkit.inventory.ItemStack item) {
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        try {
            ItemStack itemNMS = (ItemStack) field_CraftItemStack_handle.get(craftItem);
            NBTTagCompound nbt = (NBTTagCompound) field_ItemStack_tag.get(itemNMS);
            return nbt.q("AntiMapCopy"); // getBoolean
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
            NBTTagCompound nbt = (NBTTagCompound) field_ItemStack_tag.get(itemNMS);
            return nbt.h("map"); // getInt
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
    }
//    public org.bukkit.inventory.ItemStack setMapId(org.bukkit.inventory.ItemStack item, int mapId) {
//        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
//        try {
//            ItemStack itemNMS = (ItemStack) field_CraftItemStack_handle.get(craftItem);
//            NBTTagCompound nbt = (NBTTagCompound) field_ItemStack_tag.get(itemNMS);
//            if (nbt == null) {
//                nbt = new NBTTagCompound();
//                field_ItemStack_tag.set(itemNMS, nbt);
//            }
//            nbt.setInt("map", mapId);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//        return craftItem;
//    }


    /**
     * 參考 XuanCatAPI.CodeExtendEntity.getTracking
     */
    public List<Player> getTracking(org.bukkit.entity.Entity entity) {
        List<Player> playerList = new ArrayList<>();
        for (ServerPlayerConnection seen : ((CraftEntity) entity).getHandle().tracker.f) {
            playerList.add(seen.e().getBukkitEntity());
        }
        return playerList;
    }

    /**
     * 參考 XuanCatAPI.CodeNBT.parseTag
     */
    public org.bukkit.inventory.ItemStack saveItemNBT(org.bukkit.inventory.ItemStack item, String nbt) throws CommandSyntaxException {
        NBTTagCompound tag = MojangsonParser.a(nbt); // parse
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
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PlayerConnection connection = entityPlayer.b;
        NetworkManager networkManager = connection.a;
        Channel channel = networkManager.m;
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addAfter("packet_handler", "sync_static_map_view_write", new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (msg instanceof Packet) {
                    if (!Branch_18_ProxyPlayerConnection.write(player, (Packet<?>) msg))
                        return;
                }
                super.write(ctx, msg, promise);
            }
        });
        pipeline.addAfter("encoder", "sync_static_map_view_read", new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof Packet) {
                    if (!Branch_18_ProxyPlayerConnection.read(player, (Packet<?>) msg))
                        return;
                }
                super.channelRead(ctx, msg);
            }
        });
    }
}
