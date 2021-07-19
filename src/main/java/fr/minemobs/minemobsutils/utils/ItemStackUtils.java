package fr.minemobs.minemobsutils.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author minemobs
 */
public class ItemStackUtils {

    public static final ItemStack EMPTY = new ItemStack(Material.AIR);

    public static boolean isAnArmor(ItemStack is) {
        return is.getType().name().endsWith("_HELMET") || is.getType().name().endsWith("_CHESTPLATE") || is.getType().name().endsWith("_LEGGINGS") ||
                is.getType().name().endsWith("_BOOTS");
    }

    public static boolean isSimilar(ItemStack first, ItemStack second){
        if(isAirOrNull(first) || isAirOrNull(second)) return false;
        ItemMeta im1 = first.getItemMeta();
        ItemMeta im2 = second.getItemMeta();
        if(!(im1 instanceof Damageable && im2 instanceof Damageable)) return false;
        ((Damageable) im1).setDamage(((Damageable) im2).getDamage());
        first.setItemMeta(im1);
        second.setItemMeta(im2);
        return second.isSimilar(first);
    }

    public static boolean isAirOrNull(ItemStack item){
        return item == null || item.getType().isAir();
    }
    
    public static Material randomBanner(){
        List<Material> banners = Arrays.stream(Material.values()).filter(banner -> banner.name().endsWith("_BANNER") && banner.isItem() && !banner.name().startsWith("LEGACY"))
                .collect(Collectors.toList());
        return banners.get(new Random().nextInt(banners.size()));
    }

    public static Material randomSkull(){
        List<Material> skulls = Arrays.stream(Material.values()).filter(material -> material.name().endsWith("_HEAD") || material.name().endsWith("_SKULL") &&
                material.isItem()).collect(Collectors.toList());
        return skulls.get(new Random().nextInt(skulls.size()));
    }
}
