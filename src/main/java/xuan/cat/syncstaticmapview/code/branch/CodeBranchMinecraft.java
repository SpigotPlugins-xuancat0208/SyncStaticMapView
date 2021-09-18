package xuan.cat.syncstaticmapview.code.branch;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;

public final class CodeBranchMinecraft implements BranchMinecraft {
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
}
