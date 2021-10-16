package xuan.cat.syncstaticmapview.code.branch.v14_R1;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Branch_14_R1_Minecraft implements BranchMinecraft {
    private final Field field_CraftItemStack_handle;
    private final Field field_ItemStack_tag;


    public Branch_14_R1_Minecraft() throws NoSuchFieldException {
        field_CraftItemStack_handle = CraftItemStack.class.getDeclaredField("handle");
        field_ItemStack_tag = ItemStack.class.getDeclaredField("tag");
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


    public List<Player> getTracking(org.bukkit.entity.Entity entity) {
        WorldServer worldServer = ((CraftWorld) entity.getWorld()).getHandle();
        List<Player> playerList = new ArrayList<>();
        ChunkProviderServer providerServer = worldServer.getChunkProvider();
        PlayerChunkMap playerChunkMap = providerServer.playerChunkMap;
        Int2ObjectMap<PlayerChunkMap.EntityTracker> entityTrackerMap = playerChunkMap.trackedEntities;
        PlayerChunkMap.EntityTracker entityTracker = entityTrackerMap.get(entity.getEntityId());
        if (entityTracker != null) {
            Map<EntityPlayer, Boolean> trackedPlayers = entityTracker.trackedPlayerMap;
            if (trackedPlayers != null) {
                for (Map.Entry<EntityPlayer, Boolean> entry : trackedPlayers.entrySet()) {
                    if (entry.getValue()) {
                        playerList.add(entry.getKey().getBukkitEntity());
                    }
                }
            }
        }
        return playerList;
    }


    public org.bukkit.inventory.ItemStack saveItemNBT(org.bukkit.inventory.ItemStack item, String nbt) throws CommandSyntaxException {
        NBTTagCompound tag = MojangsonParser.parse(nbt);
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        try {
            ItemStack itemNMS = (ItemStack) field_CraftItemStack_handle.get(craftItem);
            field_ItemStack_tag.set(itemNMS, tag);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return craftItem;
    }
}
