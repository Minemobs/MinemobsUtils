package fr.minemobs.minemobsutils.event;

import fr.minemobs.minemobsutils.customblock.CustomBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class CustomBlockBreakEvent extends BlockBreakEvent {

    private final CustomBlock customBlock;

    public CustomBlockBreakEvent(@NotNull Block theBlock, @NotNull Player player, CustomBlock customBlock) {
        super(theBlock, player);
        this.customBlock = customBlock;
    }

    public CustomBlock getCustomBlock() {
        return customBlock;
    }
}
