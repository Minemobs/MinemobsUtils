package fr.minemobs.minemobsutils.event;

import fr.minemobs.minemobsutils.customblock.CustomBlock;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomBlockInteractEvent extends PlayerInteractEvent {

    private final CustomBlock customBlock;

    public CustomBlockInteractEvent(@NotNull Player who, @Nullable ItemStack item, @Nullable Block clickedBlock, @NotNull BlockFace clickedFace, CustomBlock customBlock) {
        super(who, Action.RIGHT_CLICK_BLOCK, item, clickedBlock, clickedFace);
        this.customBlock = customBlock;
    }

    public CustomBlockInteractEvent(@NotNull Player who, @Nullable ItemStack item, @Nullable Block clickedBlock, @NotNull BlockFace clickedFace, @Nullable EquipmentSlot hand,
                                    CustomBlock customBlock) {
        super(who, Action.RIGHT_CLICK_BLOCK, item, clickedBlock, clickedFace, hand);
        this.customBlock = customBlock;
    }

    public CustomBlock getCustomBlock() {
        return customBlock;
    }
}
