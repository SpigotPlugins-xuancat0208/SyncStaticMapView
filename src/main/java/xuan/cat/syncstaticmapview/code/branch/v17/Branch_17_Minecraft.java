package xuan.cat.syncstaticmapview.code.branch.v17;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xuan.cat.syncstaticmapview.api.branch.BranchMinecraft;

import java.util.List;

public final class Branch_17_Minecraft implements BranchMinecraft {
    @Override
    public Entity getEntityFromId(World world, int entityId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisableCopy(ItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMapId(ItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Player> getTracking(Entity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemStack saveItemNBT(ItemStack item, String nbt) throws CommandSyntaxException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void injectPlayer(Player player) {
        throw new UnsupportedOperationException();
    }
}
