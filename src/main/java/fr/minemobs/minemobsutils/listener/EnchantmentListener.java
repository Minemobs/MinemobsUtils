package fr.minemobs.minemobsutils.listener;

import com.google.common.collect.ImmutableList;
import fr.minemobs.minemobsutils.objects.CustomEnchants;
import fr.minemobs.minemobsutils.utils.WordUtils;
import fr.sunderia.sunderiautils.SunderiaUtils;
import fr.sunderia.sunderiautils.utils.ItemStackUtils;
import fr.sunderia.sunderiautils.utils.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EnchantmentListener implements Listener {

    private final Map<UUID, List<ItemStack>> soulBoundDrops = new HashMap<>();

    public static boolean hasEnchant(ItemStack stack, CustomEnchants enchants) {
        return ItemStackUtils.hasPersistentDataContainer(stack, SunderiaUtils.key("enchantment-" + enchants.getName()), PersistentDataType.INTEGER);
    }

    public static void removeEnchant(ItemStack stack, CustomEnchants enchants) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.remove(SunderiaUtils.key("enchantment-" + enchants.getName()));
        List<String> lore = meta.getLore();
        String enchantmentLore = ChatColor.GRAY + StringUtils.capitalizeWord(CustomEnchants.SOUL_BOUND.name()) + " I";
        lore.remove(enchantmentLore);
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void trasherBlockBreakListener(BlockDropItemEvent event) {
        PlayerInventory inv = event.getPlayer().getInventory();
        if(ItemStackUtils.isAirOrNull(inv.getItemInMainHand())) return;
        if(!inv.getItemInMainHand().hasItemMeta()) return;
        if(hasEnchant(inv.getItemInMainHand(), CustomEnchants.TRASHER)) return;
        if(event.getBlock().getState() instanceof Container) return;
        List<Item> drops = event.getItems();
        drops.removeIf(drop -> drop.getItemStack().getType() == Material.STONE ||
                drop.getItemStack().getType().name().endsWith("DIRT") || drop.getItemStack().getType() == Material.PODZOL ||
                drop.getItemStack().getType() == Material.NETHERRACK || drop.getItemStack().getType() == Material.DIORITE ||
                drop.getItemStack().getType() == Material.GRANITE ||
                drop.getItemStack().getType() == Material.DIRT_PATH ||
                drop.getItemStack().getType().name().endsWith("_SEEDS") ||
                drop.getItemStack().getType().name().startsWith("SAND"));
    }

    @EventHandler
    private void telepathyBlockBreakListener(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        ItemStack itemInMainHand = inv.getItemInMainHand();
        if(ItemStackUtils.isAirOrNull(itemInMainHand)) return;
        if(!itemInMainHand.hasItemMeta()) return;
        if(!hasEnchant(itemInMainHand, CustomEnchants.TELEPATHY)) return;
        if(hasEnchant(itemInMainHand, CustomEnchants.FURNACE)) return;
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if(inv.firstEmpty() == -1) return;
        if(event.getBlock().getState() instanceof Container) return;
        event.setDropItems(false);
        Collection<ItemStack> drops = event.getBlock().getDrops(itemInMainHand);
        if(drops.isEmpty()) return;
        inv.addItem(drops.iterator().next());
    }

    @EventHandler
    private void telepathyEntityDieListener(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();
        if(player == null) return;
        PlayerInventory inv = player.getInventory();
        ItemStack itemInMainHand = inv.getItemInMainHand();
        if(ItemStackUtils.isAirOrNull(itemInMainHand)) return;
        if(!itemInMainHand.hasItemMeta()) return;
        if(!hasEnchant(itemInMainHand, CustomEnchants.TELEPATHY)) return;
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if(inv.firstEmpty() == -1) return;
        player.giveExp(event.getDroppedExp());
        event.setDroppedExp(0);
        event.getDrops().forEach(inv::addItem);
        event.getDrops().clear();
    }

    @EventHandler
    private void onHitLightBoltListener(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player player)) return;
        if(!(event.getEntity() instanceof LivingEntity e)) return;
        PlayerInventory inv = player.getInventory();
        ItemStack itemInMainHand = inv.getItemInMainHand();
        if(ItemStackUtils.isAirOrNull(itemInMainHand)) return;
        if(!itemInMainHand.hasItemMeta()) return;
        if(!hasEnchant(itemInMainHand, CustomEnchants.ZEUS)) return;
        player.getWorld().strikeLightning(e.getLocation());
    }

    @EventHandler
    private void onHitTeamTreeListener(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player player)) return;
        if(!(event.getEntity() instanceof LivingEntity entity)) return;
        PlayerInventory inv = player.getInventory();
        ItemStack itemInMainHand = inv.getItemInMainHand();
        if(ItemStackUtils.isAirOrNull(itemInMainHand)) return;
        if(!itemInMainHand.hasItemMeta()) return;
        if(!hasEnchant(itemInMainHand, CustomEnchants.TEAM_TREE)) return;
        player.getWorld().generateTree(entity.getLocation(), TreeType.BIG_TREE);
        entity.addPotionEffect(PotionEffectType.JUMP.createEffect(10 * 20, 255));
        entity.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(10 * 20, 255));
        entity.addPotionEffect(PotionEffectType.SLOW.createEffect(10 * 20, 255));
    }

    @EventHandler
    private void onHitExplosionListener(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player player)) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;
        PlayerInventory inv = player.getInventory();
        ItemStack itemInMainHand = inv.getItemInMainHand();
        if(ItemStackUtils.isAirOrNull(itemInMainHand)) return;
        if(!itemInMainHand.hasItemMeta()) return;
        if(!hasEnchant(itemInMainHand, CustomEnchants.EXPLOSION)) return;
        player.getWorld().createExplosion(event.getEntity().getLocation(), 4f, true, false, event.getEntity());
    }

    @EventHandler
    private void hammerBlockBreakListener(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        ItemStack itemInMainHand = inv.getItemInMainHand();
        if(ItemStackUtils.isAirOrNull(itemInMainHand)) return;
        if(!itemInMainHand.hasItemMeta()) return;
        if(!hasEnchant(itemInMainHand, CustomEnchants.HAMMER)) return;
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
                            !block.getDrops(itemInMainHand).isEmpty()) {
                        if(hasEnchant(itemInMainHand, CustomEnchants.TELEPATHY)) {
                            if(inv.firstEmpty() == -1) return;
                            inv.addItem(block.getDrops(itemInMainHand).stream().findFirst().orElse(new ItemStack(Material.AIR)));
                            block.setType(Material.AIR);
                        } else {
                            block.breakNaturally(itemInMainHand);
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
        ItemStack itemInMainHand = inv.getItemInMainHand();
        if(ItemStackUtils.isAirOrNull(itemInMainHand)) return;
        if(!itemInMainHand.hasItemMeta()) return;
        if(!hasEnchant(itemInMainHand, CustomEnchants.FURNACE)) return;
        List<Recipe> recipes = ImmutableList.copyOf(Bukkit.recipeIterator());
        for (Recipe iRecipe : recipes.stream().filter(FurnaceRecipe.class::isInstance).toList()) {
            FurnaceRecipe recipe = (FurnaceRecipe) iRecipe;
            for (Item item : event.getItems().stream().filter(item -> recipe.getInputChoice().test(item.getItemStack())).toList()) {
                ItemStack drop = recipe.getResult();
                drop.setAmount(item.getItemStack().getAmount());
                event.getItems().remove(item);
                if(hasEnchant(itemInMainHand, CustomEnchants.TELEPATHY)) {
                    if(inv.firstEmpty() == -1) {
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(.5, .5, .5), drop);
                        return;
                    }
                    inv.addItem(drop);
                    return;
                }
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(.5, .5, .5), drop);
            }
        }
    }

    //SoulBound Enchant
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        soulBoundDrops.put(event.getEntity().getUniqueId(), Arrays.stream(event.getEntity().getInventory().getContents())
                .filter(Objects::nonNull)
                .filter(itemStack -> hasEnchant(itemStack, CustomEnchants.SOUL_BOUND)).toList());
        event.getDrops().removeAll(soulBoundDrops.get(event.getEntity().getUniqueId()));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        soulBoundDrops.get(event.getPlayer().getUniqueId()).forEach(is -> {
            removeEnchant(is, CustomEnchants.SOUL_BOUND);
            event.getPlayer().getInventory().addItem(is);
        });
    }
    //SoulBound Enchant

    private List<Material> getInvalidBlocks() {
        return Arrays.asList(Material.LAVA, Material.WATER, Material.BEDROCK, Material.NETHER_PORTAL, Material.END_PORTAL, Material.END_PORTAL_FRAME, Material.TALL_GRASS,
                Material.SEAGRASS, Material.GRASS);
    }
}
