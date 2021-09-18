package xuan.cat.syncstaticmapview.api.branch;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface BranchMinecraft {
    Entity getEntityFromId(World world, int entityId);

    int getMapId(ItemStack item);
    ItemStack setMapId(ItemStack item, int mapId);
}
