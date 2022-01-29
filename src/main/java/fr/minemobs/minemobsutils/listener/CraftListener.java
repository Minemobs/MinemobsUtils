package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.objects.AnvilRecipe;
import fr.minemobs.minemobsutils.objects.Recipes;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CraftListener implements Listener {

    private enum Stars {
        ZERO_STARS(0.15),
        ONE_STARS(.5),
        TWO_STARS(1),
        THREE_STARS(1.5),
        FOUR_STARS(2.75),
        FIVE_STARS(3.5);

        public final double i;

        Stars(double i) {
            this.i = i;
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if(!isToolOrWeapon(event.getCurrentItem().getType())) return;
        ItemStack is = event.getCurrentItem();
        Stars star = Stars.values()[new Random().nextInt(Stars.values().length)];
        ItemMeta meta = is.getItemMeta();
        List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
        lore.add(stars(enumToInt(star)) + ChatColor.YELLOW + " Stars");
        lore.add(ChatColor.GREEN + "+ " + ChatColor.RED + star.i + ChatColor.GREEN + " dmg");
        meta.setLore(lore);
        is.setItemMeta(meta);
    }

    //Debug Event
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player player)) return;
        if(!isToolOrWeapon(player.getInventory().getItemInMainHand().getType())) return;
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if(!meta.hasLore()) return;
        if(meta.getLore().stream().noneMatch(s -> s.endsWith(ChatColor.YELLOW + " Stars"))) return;
        //Hacky way to do that cause i'm dumb
        ((LivingEntity) event.getEntity()).damage(getStars(meta.getLore().stream().filter(s -> s.endsWith(ChatColor.YELLOW + " Stars")).findFirst().get()));
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();
        if(ItemStackUtils.isAirOrNull(inv.getItem(0)) || ItemStackUtils.isAirOrNull(inv.getItem(1))) return;
        Optional<AnvilRecipe> recipe = Arrays.stream(Recipes.values()).filter(recipes -> recipes.getAnvilRecipe() != null && recipes.getAnvilRecipe().isEquals(inv))
                .map(Recipes::getAnvilRecipe).findFirst();
        if(recipe.isEmpty()) return;
        event.setResult(recipe.get().getResult());
    }

    @EventHandler
    public void onAnvilClickedResult(InventoryClickEvent event) {
        if(event.getInventory().getType() != InventoryType.ANVIL || event.getRawSlot() != 2) return;
        if(event.getWhoClicked().getInventory().firstEmpty() == -1) return;
        AnvilInventory inv = (AnvilInventory) event.getInventory();
        if (Arrays.stream(Recipes.values()).noneMatch(recipes -> recipes.getAnvilRecipe() != null && recipes.getAnvilRecipe().isEquals(inv))) return;
        event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
        inv.getItem(0).setAmount(inv.getItem(0).getAmount() - 1);
        inv.getItem(1).setAmount(inv.getItem(1).getAmount() - 1);
    }

    private boolean isToolOrWeapon(Material mat) {
        return mat.name().endsWith("_SWORD") || mat.name().endsWith("_PICKAXE") || mat.name().endsWith("_AXE") || mat.name().endsWith("_HOE") || mat.name().endsWith("_SHOVEL");
    }

    private int enumToInt(Enum<?> _enum) {
        return _enum.ordinal();
    }
    
    private String stars(int nmbOfStars) {
        StringBuilder builder = new StringBuilder();
        builder.append((ChatColor.GOLD + "⭐").repeat(Math.max(0, nmbOfStars)));
        int missingStars = 5 - nmbOfStars;
        builder.append((ChatColor.GRAY + "⭐").repeat(Math.max(0, missingStars)));
        return builder.toString() + ChatColor.RESET;
    }

    private double getStars(String stars) {
        char[] chars = stars.toCharArray();
        int i = 0;
        for (char aChar : chars) {
            if(aChar == '⭐') {
                i++;
            }
        }
        return Stars.values()[i].i;
    }
}
