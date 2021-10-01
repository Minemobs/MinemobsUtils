package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.customblock.CustomBlock;
import fr.minemobs.minemobsutils.event.CustomBlockBreakEvent;
import fr.minemobs.minemobsutils.event.CustomBlockInteractEvent;
import fr.minemobs.minemobsutils.event.CustomBlockPlaceEvent;
import fr.minemobs.minemobsutils.objects.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomBlockListener implements Listener {

    public static final List<CustomBlock> blocks = new ArrayList<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomBlockPlaced(CustomBlockPlaceEvent event) {
        CustomBlock block = event.getCustomBlock();
        block.setBlock(event.getBlock().getLocation());
        block.setLoc(event.getBlock().getLocation());
        blocks.add(block);
    }

    @EventHandler
    public void onCustomBlockBreak(CustomBlockBreakEvent event) {
        for(ItemStack stack : event.getCustomBlock().getDrop()){
            event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), stack);
        }
        event.setExpToDrop(event.getCustomBlock().getXp());
        blocks.remove(event.getCustomBlock());
    }

    @EventHandler
    public void onInteract(CustomBlockInteractEvent event) {
        if(!event.getItem().isSimilar(Items.WRENCH.stack)) return;
        event.getPlayer().sendMessage("Is this item in the list ? " + blocks.contains(event.getCustomBlock()));
    }
}
