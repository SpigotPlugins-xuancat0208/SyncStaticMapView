package xuan.cat.syncstaticmapview.api.branch;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface BranchMinecraft {
    Entity getEntityFromId(World world, int entityId);

    boolean isDisableCopy(ItemStack item);

    int getMapId(ItemStack item);
//    ItemStack setMapId(ItemStack item, int mapId);

    /**
     * @return 正在追蹤的玩家清單
     */
    List<Player> getTracking(Entity entity);

    ItemStack saveItemNBT(ItemStack item, String nbt) throws CommandSyntaxException;

    void injectPlayer(Player player);
}
