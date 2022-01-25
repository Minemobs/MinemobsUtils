package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.objects.Items;
import fr.minemobs.minemobsutils.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class WorldListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFallingBlockLand(EntityChangeBlockEvent event) {
        if(!(event.getEntity() instanceof FallingBlock block) || !((FallingBlock) event.getEntity()).getBlockData().getMaterial().toString().endsWith("ANVIL")) return;
        Block b = block.getWorld().getBlockAt(block.getLocation().clone().subtract(0, 1, 0));
        if(b.isEmpty() || b.isLiquid() || b.isPassable()) return;
        switch (b.getType()) {
            case COPPER_BLOCK -> giveItem(Items.COPPER_PLATE, b, block);
            case IRON_BLOCK -> giveItem(Items.IRON_PLATE, b, block);
            case AMETHYST_BLOCK -> giveItem(Items.AMETHYST_PLATE, b, block);
            case GOLD_BLOCK -> giveItem(Items.GOLD_PLATE, b, block);
            case DIAMOND_BLOCK -> giveItem(Items.DIAMOND_PLATE, b, block);
            case NETHERITE_BLOCK -> giveItem(Items.NETHERITE_PLATE, b, block);
        }
    }

    private void giveItem(Items items, Block b, FallingBlock block) {
        b.setType(Material.AIR);
        block.getWorld().dropItemNaturally(b.getLocation(), new ItemBuilder(items.stack).setAmount(3).build());
    }
}
