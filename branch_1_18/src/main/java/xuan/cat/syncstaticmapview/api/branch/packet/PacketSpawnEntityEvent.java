package xuan.cat.syncstaticmapview.api.branch.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public final class PacketSpawnEntityEvent extends PacketEvent {
    private static final HandlerList handlers = new HandlerList();
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final int entityId;

    public PacketSpawnEntityEvent(Player player, int entityId) {
        super(player);
        this.entityId = entityId;
    }

    public int getEntityId() {
        return entityId;
    }
}