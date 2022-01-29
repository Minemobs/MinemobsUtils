package fr.minemobs.minemobsutils.utils;

import fr.minemobs.minemobsutils.objects.Items;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author minemobs
 */
public class ItemStackUtils {

    private ItemStackUtils() {}

    public static final ItemStack EMPTY = new ItemStack(Material.AIR);

    public static boolean isAnArmor(ItemStack is) {
        return is.getType().name().endsWith("_HELMET") || is.getType().name().endsWith("_CHESTPLATE") || is.getType().name().endsWith("_LEGGINGS") ||
                is.getType().name().endsWith("_BOOTS");
    }

    /**
     *
     * @param first The first item
     * @param second The second item
     * @return true if the two items are the same
     * @deprecated Use {@link ItemStackUtils#isSameItem(ItemStack, ItemStack)} instead
     * @since 1.1
     */
    @Deprecated(since = "1.1", forRemoval = true)
    public static boolean isSimilar(ItemStack first, ItemStack second) {
        if(isAirOrNull(first) || isAirOrNull(second)) return false;
        ItemMeta im1 = first.getItemMeta();
        ItemMeta im2 = second.getItemMeta();
        if(im1 instanceof Damageable dmg1 && im2 instanceof Damageable dmg2) {
            dmg1.setDamage(dmg2.getDamage());
        }
        first.setItemMeta(im1);
        second.setItemMeta(im2);
        return first.isSimilar(second);
    }

    /**
     * This method use the lore to check if both items have the same name.
     * This method only works on custom items.
     * @param first The first item
     * @param second The second item
     * @return true if the two items are the same
     * @since 1.4
     */
    public static boolean isSameItem(ItemStack first, ItemStack second) {
        if(!hasLore(first) || !hasLore(second)) return false;
        ItemMeta im1 = first.clone().getItemMeta();
        ItemMeta im2 = second.clone().getItemMeta();
        String lore = im1.getLore().get(im1.getLore().size() - 1);
        String lore2 = im2.getLore().get(im2.getLore().size() - 1);
        if(!ChatColor.stripColor(lore).startsWith("minemobsutils:") || !ChatColor.stripColor(lore2).startsWith("minemobsutils:")) return false;
        return lore.equals(lore2);
    }

    @Nullable
    public static Items getItemFromItemStack(ItemStack is) {
        if(!hasLore(is)) return null;
        String lore = ChatColor.stripColor(is.getItemMeta().getLore().get(is.getItemMeta().getLore().size() - 1));
        if(!lore.startsWith("minemobsutils:")) return null;
        return Items.valueOf(lore.toUpperCase().replace("MINEMOBSUTILS:",""));
    }

    public static boolean isAirOrNull(ItemStack item){
        return item == null || item.getType().isAir();
    }

    public static boolean isNotAirNorNull(ItemStack is) {
        return !isAirOrNull(is);
    }

    public static boolean hasLore(ItemStack is) {
        return !isAirOrNull(is) && is.hasItemMeta() && is.getItemMeta().hasLore();
    }

    public static Material randomBanner() {
        List<Material> banners = Arrays.stream(Material.values()).filter(banner -> banner.name().endsWith("_BANNER") && !banner.name().endsWith("_WALL_BANNER") &&
                        !banner.name().startsWith("LEGACY")).toList();
        return banners.get(new Random().nextInt(banners.size()));
    }

    public static Material randomSkull() {
        List<Material> skulls = Arrays.stream(Material.values()).filter(material -> (!material.name().startsWith("PISTON") && material.name().endsWith("_HEAD") && !material.name().endsWith("_WALL_HEAD")) ||
                (material.name().endsWith("_SKULL") && !material.name().startsWith("LEGACY") && !material.name().endsWith("_WALL_SKULL"))).toList();
        return skulls.get(new Random().nextInt(skulls.size()));
    }
}
