package fr.minemobs.minemobsutils.listener;

import com.google.common.collect.ImmutableList;
import fr.minemobs.minemobsutils.objects.CustomEnchants;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantmentListener implements Listener {

    @EventHandler
    private void telepathyBlockBreakListener(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if(ItemStackUtils.isAirOrNull(inv.getItemInMainHand())) return;
        if(!inv.getItemInMainHand().hasItemMeta()) return;
        if(!inv.getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.TELEPATHY.enchantment)) return;
        if(inv.getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.FURNACE.enchantment)) return;
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if(inv.firstEmpty() == -1) return;
        if(event.getBlock().getState() instanceof Container) return;
        event.setDropItems(false);
        Collection<ItemStack> drops = event.getBlock().getDrops(inv.getItemInMainHand());
        if(drops.isEmpty()) return;
        inv.addItem(drops.iterator().next());
    }

    @EventHandler
    private void telepathyEntityDieListener(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();
        if(player == null) return;
        PlayerInventory inv = player.getInventory();
        if(ItemStackUtils.isAirOrNull(inv.getItemInMainHand())) return;
        if(!inv.getItemInMainHand().hasItemMeta()) return;
        if(!inv.getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.TELEPATHY.enchantment)) return;
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if(inv.firstEmpty() == -1) return;
        player.giveExp(event.getDroppedExp());
        event.setDroppedExp(0);
        event.getDrops().forEach(inv::addItem);
        event.getDrops().clear();
    }

    @EventHandler
    private void onHitLightBoltListener(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;
        Player player = (Player) event.getDamager();
        PlayerInventory inv = player.getInventory();
        if(ItemStackUtils.isAirOrNull(inv.getItemInMainHand())) return;
        if(!inv.getItemInMainHand().hasItemMeta()) return;
        if(!inv.getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.ZEUS.enchantment)) return;
        player.getWorld().strikeLightning(event.getEntity().getLocation());
    }

    @EventHandler
    private void onHitTeamTreeListener(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;
        Player player = (Player) event.getDamager();
        PlayerInventory inv = player.getInventory();
        if(ItemStackUtils.isAirOrNull(inv.getItemInMainHand())) return;
        if(!inv.getItemInMainHand().hasItemMeta()) return;
        if(!inv.getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.TEAM_TREE.enchantment)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        player.getWorld().generateTree(entity.getLocation(), TreeType.BIG_TREE);
        entity.addPotionEffect(PotionEffectType.JUMP.createEffect(10 * 20, 255));
        entity.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(10 * 20, 255));
        entity.addPotionEffect(PotionEffectType.SLOW.createEffect(10 * 20, 255));
    }

    @EventHandler
    private void onHitExplosionListener(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;
        Player player = (Player) event.getDamager();
        PlayerInventory inv = player.getInventory();
        if(ItemStackUtils.isAirOrNull(inv.getItemInMainHand())) return;
        if(!inv.getItemInMainHand().hasItemMeta()) return;
        if(!inv.getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.EXPLOSION.enchantment)) return;
        player.getWorld().createExplosion(event.getEntity().getLocation(), 4f, true, false, event.getEntity());
    }

    @EventHandler
    private void hammerBlockBreakListener(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if(ItemStackUtils.isAirOrNull(inv.getItemInMainHand())) return;
        if(!inv.getItemInMainHand().hasItemMeta()) return;
        if(!inv.getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.HAMMER.enchantment)) return;
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        Location loc = event.getBlock().getLocation();
        for(int x = -1;x <= 1;x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block block = loc.getWorld().getBlockAt(
                            loc.getBlockX() + x,
                            loc.getBlockY() + y,
                            loc.getBlockZ() + z);
                    if (block.getType().isBlock() && block.getType().isSolid() && !getInvalidBlocks().contains(block.getType()) &&
                            !block.getDrops(inv.getItemInMainHand()).isEmpty()) {
                        if(inv.getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.TELEPATHY.enchantment)) {
                            if(inv.firstEmpty() == -1) return;
                            inv.addItem(block.getDrops(inv.getItemInMainHand()).stream().findFirst().orElse(new ItemStack(Material.AIR)));
                            block.setType(Material.AIR);
                        } else {
                            block.breakNaturally(inv.getItemInMainHand());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void furnaceEvent(BlockDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if(ItemStackUtils.isAirOrNull(inv.getItemInMainHand())) return;
        if(!inv.getItemInMainHand().hasItemMeta()) return;
        if(!inv.getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.FURNACE.enchantment)) return;
        //Iterator<Recipe> recipes = Bukkit.getServer().recipeIterator();
        List<Recipe> recipes = ImmutableList.copyOf(Bukkit.recipeIterator());
        /*while (recipes.hasNext()) {
            Recipe rec = recipes.next();
            if(!(rec instanceof FurnaceRecipe)) return;
            FurnaceRecipe recipe = (FurnaceRecipe) rec;
            for (Item item : event.getItems()) {
                if(recipe.getInputChoice().test(item.getItemStack())) {
                    ItemStack drop = recipe.getResult();
                    drop.setAmount(item.getItemStack().getAmount());
                    event.getItems().remove(item);
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(.5, .5, .5), drop);
                }
            }
        }*/
        for (Recipe _recipe : recipes.stream().filter(recipe -> recipe instanceof FurnaceRecipe).collect(Collectors.toList())) {
            FurnaceRecipe recipe = (FurnaceRecipe) _recipe;
            for (Item item : event.getItems().stream().filter(item -> recipe.getInputChoice().test(item.getItemStack())).collect(Collectors.toList())) {
                ItemStack drop = recipe.getResult();
                drop.setAmount(item.getItemStack().getAmount());
                event.getItems().remove(item);
                if(inv.getItemInMainHand().getItemMeta().hasEnchant(CustomEnchants.TELEPATHY.enchantment)) {
                    if(inv.firstEmpty() == -1) {
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(.5, .5, .5), drop);
                        return;
                    }
                    inv.addItem(drop);
                    return;
                }
            }
        }
    }

    private List<Material> getInvalidBlocks() {
        return Arrays.asList(Material.LAVA, Material.WATER, Material.BEDROCK, Material.NETHER_PORTAL, Material.END_PORTAL, Material.END_PORTAL_FRAME, Material.TALL_GRASS,
                Material.SEAGRASS, Material.GRASS);
    }

}
