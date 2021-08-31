package fr.minemobs.minemobsutils.event;

import fr.minemobs.minemobsutils.objects.Blocks;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CustomBlockPlaceEvent extends BlockPlaceEvent implements Cancellable {

    private final Blocks customBlock;

    public CustomBlockPlaceEvent(@NotNull Block placedBlock, @NotNull BlockState replacedBlockState, @NotNull Block placedAgainst, @NotNull ItemStack itemInHand,
                                 @NotNull Player thePlayer, boolean canBuild, @NotNull EquipmentSlot hand, @NotNull Blocks customBlock) {
        super(placedBlock, replacedBlockState, placedAgainst, itemInHand, thePlayer, canBuild, hand);
        this.customBlock = customBlock;
    }

    public Blocks getCustomBlock() {
        return customBlock;
    }
}
