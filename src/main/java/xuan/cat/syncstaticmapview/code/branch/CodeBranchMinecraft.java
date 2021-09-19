package xuan.cat.syncstaticmapview.code.branch;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class CodeBranchMinecraft implements BranchMinecraft {
   private final Field field_Entity_tracker;


   public CodeBranchMinecraft() throws NoSuchFieldException {
       field_Entity_tracker = net.minecraft.world.entity.Entity.class.getDeclaredField("tracker");
       field_Entity_tracker.setAccessible(true);
   }


    public Entity getEntityFromId(World world, int entityId) {
        net.minecraft.world.entity.Entity entity = ((CraftWorld) world).getHandle().getEntity(entityId);
        return entity != null ? entity.getBukkitEntity() : null;
    }


    public int getMapId(ItemStack item) {
        NBTTagCompound nbt = CraftItemStack.asCraftCopy(item).handle.u;
        return nbt.getInt("map");
    }
    public ItemStack setMapId(ItemStack item, int mapId) {
        CraftItemStack craftItem = CraftItemStack.asCraftCopy(item);
        NBTTagCompound nbt = craftItem.handle.u;
        if (nbt == null)
            nbt = craftItem.handle.u = new NBTTagCompound();
        nbt.setInt("map", mapId);
        return craftItem;
    }


    public List<Player> getTracking(Entity entity) {
        List<Player>                    playerList      = new ArrayList<>();
        try {
            PlayerChunkMap.EntityTracker    entityTracker   = (PlayerChunkMap.EntityTracker) field_Entity_tracker.get(((CraftEntity) entity).getHandle());
            if (entityTracker != null) {
                Set<ServerPlayerConnection> trackedPlayers  = entityTracker.f;
                for (ServerPlayerConnection connection : trackedPlayers) {
                    EntityPlayer entityPlayer    = connection.d();
                    playerList.add(entityPlayer.getBukkitEntity());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return playerList;
    }
}
