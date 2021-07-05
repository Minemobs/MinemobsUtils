package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public enum Items {

    GRAPPLING_HOOK(new ItemBuilder(Material.BOW).setLore("It's literally a grappling hook").setDisplayName("Grappling Hook").setGlow().build()),
    //Armors
    DRACONIC_HELMET(new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Helmet").setUnbreakable(true).addProtection(15).addItemFlag(ItemFlag.HIDE_ENCHANTS).build()),
    DRACONIC_CHESTPLATE(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Chestplate").setUnbreakable(true).addProtection(15).addItemFlag(ItemFlag.HIDE_ENCHANTS)
            .build()),
    DRACONIC_LEGGINGS(new ItemBuilder(Material.DIAMOND_LEGGINGS).setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Leggings").setUnbreakable(true).addProtection(15).addItemFlag(ItemFlag.HIDE_ENCHANTS).build()),
    DRACONIC_BOOTS(new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Boots").setUnbreakable(true).addProtection(15).addItemFlag(ItemFlag.HIDE_ENCHANTS).build()),
    ;

    public final ItemStack stack;

    Items(ItemStack stack) {
        this.stack = stack;
    }
}
