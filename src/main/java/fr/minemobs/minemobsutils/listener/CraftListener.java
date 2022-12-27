package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.objects.Recipes;
import fr.sunderia.sunderiautils.utils.ItemStackUtils;
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
import java.util.stream.IntStream;

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
        if(!ItemStackUtils.isToolOrWeapon(event.getCurrentItem().getType())) return;
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
        if(!ItemStackUtils.isToolOrWeapon(player.getInventory().getItemInMainHand().getType())) return;
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if(!meta.hasLore()) return;
        Optional<String> stars = meta.getLore().stream().filter(s -> s.endsWith(ChatColor.YELLOW + " Stars")).findFirst();
        if(stars.isEmpty()) return;
        //Hacky way to do that because I'm dumb
        ((LivingEntity) event.getEntity()).damage(getStars(stars.get()));
    }

    private int enumToInt(Enum<?> _enum) {
        return _enum.ordinal();
    }
    
    private String stars(int nmbOfStars) {
        int missingStars = 5 - nmbOfStars;
        String builder = (ChatColor.GOLD + "⭐").repeat(Math.max(0, nmbOfStars)) +
                (ChatColor.GRAY + "⭐").repeat(Math.max(0, missingStars));
        return builder + ChatColor.RESET;
    }

    private double getStars(String stars) {
        return Stars.values()[(int) stars.chars().map(i -> (char)i).filter(c -> c == '⭐').count()].i;
    }
}
