package xuan.cat.syncstaticmapview.code.branch.v17;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.bukkit.World;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Branch_17_Minecraft implements BranchMinecraft {
    private final Field field_CraftItemStack_handle;
    private final Field field_ItemStack_tag;


   public Branch_17_Minecraft() throws NoSuchFieldException {
       field_CraftItemStack_handle = CraftItemStack.class.getDeclaredField("handle");
       field_ItemStack_tag = net.minecraft.world.item.ItemStack.class.getDeclaredField("u");
       field_CraftItemStack_handle.setAccessible(true);
       field_ItemStack_tag.setAccessible(true);
   }


    public org.bukkit.entity.Entity getEntityFromId(World world, int entityId) {
        Entity entity = ((CraftWorld) world).getHandle().getEntity(entityId);
        return entity != null ? entity.getBukkitEntity() : null;
    }


    public boolean isDisableCopy(org.bukkit.inventory.ItemStack item) {
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        try {
            ItemStack itemNMS = (ItemStack) field_CraftItemStack_handle.get(craftItem);
            NBTTagCompound nbt = (NBTTagCompound) field_ItemStack_tag.get(itemNMS);
            return nbt.getBoolean("AntiMapCopy");
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }


    public int getMapId(org.bukkit.inventory.ItemStack item) {
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        try {
            ItemStack itemNMS = (ItemStack) field_CraftItemStack_handle.get(craftItem);
            NBTTagCompound nbt = (NBTTagCompound) field_ItemStack_tag.get(itemNMS);
            return nbt.getInt("map");
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
    }
    public org.bukkit.inventory.ItemStack setMapId(org.bukkit.inventory.ItemStack item, int mapId) {
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        try {
            ItemStack itemNMS = (ItemStack) field_CraftItemStack_handle.get(craftItem);
            NBTTagCompound nbt = (NBTTagCompound) field_ItemStack_tag.get(itemNMS);
            if (nbt == null) {
                nbt = new NBTTagCompound();
                field_ItemStack_tag.set(itemNMS, nbt);
            }
            nbt.setInt("map", mapId);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return craftItem;
    }


    public List<Player> getTracking(org.bukkit.entity.Entity entity) {
        WorldServer worldServer = ((CraftWorld) entity.getWorld()).getHandle();
        List<Player> playerList = new ArrayList<>();
        ChunkProviderServer providerServer = worldServer.getChunkProvider();
        PlayerChunkMap playerChunkMap = providerServer.a;
        Int2ObjectMap<PlayerChunkMap.EntityTracker> entityTrackerMap = playerChunkMap.G;
        PlayerChunkMap.EntityTracker entityTracker = entityTrackerMap.get(entity.getEntityId());
        if (entityTracker != null) {
            Set<ServerPlayerConnection> trackedPlayers = entityTracker.f;
            if (trackedPlayers != null) {
                for (ServerPlayerConnection connection : trackedPlayers) {
                    EntityPlayer entityPlayer = connection.d();
                    playerList.add(entityPlayer.getBukkitEntity());
                }
            }
        }
        return playerList;
    }
}
