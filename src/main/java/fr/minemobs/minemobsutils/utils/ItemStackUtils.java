package fr.minemobs.minemobsutils.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * @author minemobs
 */
public class ItemStackUtils {

    public static final ItemStack EMPTY = new ItemStack(Material.AIR);
    private static final Material[] armors = new Material[]{Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
    Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
    Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
    Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
    Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS};
    private static final Material[] banners = new Material[]{Material.WHITE_BANNER, Material.ORANGE_BANNER, Material.MAGENTA_BANNER, Material.LIGHT_BLUE_BANNER, Material.YELLOW_BANNER, Material.LIME_BANNER,
            Material.PINK_BANNER, Material.GRAY_BANNER, Material.LIGHT_GRAY_BANNER, Material.CYAN_BANNER, Material.PURPLE_BANNER, Material.BLUE_BANNER, Material.BROWN_BANNER, Material.GREEN_BANNER, Material.RED_BANNER,
            Material.BLACK_BANNER};
    private static final Material[] skulls = new Material[]{Material.CREEPER_HEAD, Material.DRAGON_HEAD, Material.PLAYER_HEAD, Material.ZOMBIE_HEAD, Material.WITHER_SKELETON_SKULL, Material.SKELETON_SKULL};

    public static boolean isAnArmor(ItemStack is) {
        return Arrays.stream(armors).anyMatch(material -> material == is.getType());
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
        return banners[new Random().nextInt(banners.length)];
    }

    public static Material randomSkull(){
        return skulls[new Random().nextInt(skulls.length)];
    }
}
