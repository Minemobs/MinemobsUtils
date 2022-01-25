package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.customblock.CustomBlock;
import fr.minemobs.minemobsutils.event.CustomBlockBreakEvent;
import fr.minemobs.minemobsutils.event.CustomBlockInteractEvent;
import fr.minemobs.minemobsutils.event.CustomBlockPlaceEvent;
import fr.minemobs.minemobsutils.objects.Items;
import fr.minemobs.minemobsutils.utils.SchematicsUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.structure.Structure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomBlockListener implements Listener {

    public static final List<CustomBlock> blocks = new ArrayList<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomBlockPlaced(CustomBlockPlaceEvent event) {
        CustomBlock block = event.getCustomBlock();
        block.setBlock(event.getBlock().getLocation());
        block.setLoc(event.getBlock().getLocation());
        blocks.add(block);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomBlockBreak(CustomBlockBreakEvent event) {
        for(ItemStack stack : event.getCustomBlock().getDrop()){
            event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), stack);
        }
        event.setExpToDrop(event.getCustomBlock().getXp());
        blocks.remove(event.getCustomBlock());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onHouseGenBreak(CustomBlockBreakEvent event) {
        if(event.getCustomBlock().getCustomModelData() != 8) return; //if it's not HOUSE_GENERATOR then it will ignore the rest of the code
        try {
            // TODO: 10/10/2021 Fix this
            Structure s = MinemobsUtils.getInstance().getServer().getStructureManager().loadStructure(new File(SchematicsUtils.schematicFolder, "houuse.nbt"));
            s.place(event.getCustomBlock().getLoc(), false, StructureRotation.COUNTERCLOCKWISE_90, Mirror.NONE, 0, 1, new Random());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(CustomBlockInteractEvent event) {
        if(!event.getItem().isSimilar(Items.WRENCH.stack)) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        event.getClickedBlock().breakNaturally();
        if(event.getPlayer().isSneaking()) {
            Bukkit.getScheduler().runTaskLater(MinemobsUtils.getInstance(),
                    () -> Bukkit.getPluginManager().callEvent(new CustomBlockBreakEvent(event.getClickedBlock(), event.getPlayer(), event.getCustomBlock())),
                    2);
        }
        blocks.remove(event.getCustomBlock());
    }
}
