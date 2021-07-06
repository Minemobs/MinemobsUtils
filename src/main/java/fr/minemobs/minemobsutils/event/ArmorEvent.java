package fr.minemobs.minemobsutils.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ArmorEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack[] playerArmor;

    public ArmorEvent(@NotNull Player player) {
        super(player);
        this.playerArmor = player.getInventory().getArmorContents();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public ItemStack[] getPlayerArmor() {
        return playerArmor;
    }
}
