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
        if(!(event.getEntity() instanceof FallingBlock) || !((FallingBlock) event.getEntity()).getBlockData().getMaterial().toString().endsWith("ANVIL")) return;
        FallingBlock block = (FallingBlock) event.getEntity();
        Block b = block.getWorld().getBlockAt(block.getLocation().clone().subtract(0, 1, 0));
        if(b.isEmpty() || b.isLiquid() || b.isPassable()) return;
        if(b.getType() == Material.IRON_BLOCK) {
            b.setType(Material.AIR);
            block.getWorld().dropItemNaturally(b.getLocation(), new ItemBuilder(Items.IRON_PLATE.stack).setAmount(3).build());
        }
    }
}
