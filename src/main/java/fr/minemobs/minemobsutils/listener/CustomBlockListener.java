package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.customblock.CustomBlock;
import fr.minemobs.minemobsutils.event.CustomBlockBreakEvent;
import fr.minemobs.minemobsutils.event.CustomBlockInteractEvent;
import fr.minemobs.minemobsutils.event.CustomBlockPlaceEvent;
import fr.minemobs.minemobsutils.objects.Items;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CustomBlockListener implements Listener {

    public static final Map<NamespacedKey, CustomBlock> blocks = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomBlockPlaced(CustomBlockPlaceEvent event) {
        CustomBlock block = event.getCustomBlock();
        block.setBlock(event.getBlock().getLocation());
        block.setLoc(event.getBlock().getLocation());
        blocks.put(block.getKey(), block);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomBlockBreak(CustomBlockBreakEvent event) {
        for(ItemStack stack : event.getCustomBlock().getDrop()){
            event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), stack);
        }
        event.setExpToDrop(event.getCustomBlock().getXp());
        blocks.remove(event.getCustomBlock().getKey());
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
        blocks.remove(event.getCustomBlock().getKey());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getType() != Material.SPAWNER || event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
        if(!spawner.getPersistentDataContainer().has(CustomBlock.CMD_KEY, PersistentDataType.INTEGER)) {
            return;
        }
        int cmd = spawner.getPersistentDataContainer().get(CustomBlock.CMD_KEY, PersistentDataType.INTEGER);
        Optional<CustomBlock> cb = CustomBlockListener.blocks.values().stream()
                .filter(customBlock -> customBlock.getCustomModelData() == cmd && customBlock.getLoc().equals(event.getBlock().getLocation())).findFirst();
        if (cb.isEmpty()) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new CustomBlockBreakEvent(event.getBlock(), event.getPlayer(), cb.get()));
            }
        }.runTaskLater(MinemobsUtils.getInstance(), 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomBlockPlaced(BlockPlaceEvent event) {
        if(event.getBlock().getType() != Material.LIME_GLAZED_TERRACOTTA || !event.getItemInHand().hasItemMeta() || !event.getItemInHand().getItemMeta().hasDisplayName() ||
                !event.getItemInHand().getItemMeta().hasCustomModelData()) return;
        Optional<CustomBlock> opBlock = CustomBlock.getRegisteredBlocks().stream()
                .filter(block -> ItemStackUtils.isSameItem(event.getItemInHand(), block.getItem()) &&
                        event.getItemInHand().getItemMeta().hasCustomModelData() &&
                        Objects.equals(block.getItem().getItemMeta().getCustomModelData(), event.getItemInHand().getItemMeta().getCustomModelData())).findFirst();
        if(opBlock.isEmpty()) {
            return;
        }
        CustomBlock block = opBlock.get();
        block.setLoc(event.getBlock().getLocation());
        event.setCancelled(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setBlock(block.getLoc());
                blocks.put(block.getKey(), block);
                CustomBlockPlaceEvent e = new CustomBlockPlaceEvent(event.getBlockPlaced(), event.getBlockReplacedState(),
                        event.getBlockAgainst(), event.getItemInHand(), event.getPlayer(), event.canBuild(),
                        event.getHand(), block);
                if(event.getPlayer().getGameMode() == GameMode.SURVIVAL) event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
                Bukkit.getPluginManager().callEvent(e);
            }
        }.runTaskLater(MinemobsUtils.getInstance(), 2);
    }
}
