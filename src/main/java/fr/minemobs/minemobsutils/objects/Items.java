package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Items {

    GRAPPLING_HOOK(new ItemBuilder(Material.FISHING_ROD).setLore("Yeah a new grappling hook").setDisplayName("Grappling Hook").setGlow().build()),
    BATTERY(new ItemBuilder(Material.SUNFLOWER).setDisplayName("Battery").setGlow().build()),
    HAMMER(new ItemBuilder(Material.IRON_AXE).setDisplayName(ChatColor.GRAY + "Hammer").setGlow().build()),
    CRAFTING_TABLE_PORTABLE(new ItemBuilder(Material.STICK).setDisplayName(ChatColor.GRAY + "Portable Crafting Table").setCustomModelData(501).setGlow().build()),
    DYNAMITE(new ItemBuilder(Material.STICK).setDisplayName(ChatColor.RED + "Dynamite").setGlow().build()),
    FIREBALL_STAFF(new ItemBuilder(Material.STICK).setDisplayName(ChatColor.LIGHT_PURPLE + "Fireball Staff").setCustomModelData(1024).setGlow().build()),
    WRENCH(new ItemBuilder(Material.STICK).setDisplayName(ChatColor.GRAY + "Wrench").setCustomModelData(256).setGlow().build()),
    //Ingots
    DRACONIUM_INGOT(new ItemBuilder(Material.BRICK).setDisplayName(ChatColor.LIGHT_PURPLE + "Draconium Ingot").setGlow().build()),
    RUBY(new ItemBuilder(Material.EMERALD).setDisplayName(ChatColor.RED + "Ruby").setGlow().build()),
    CHARGED_DRACONIUM_INGOT(new ItemBuilder(Material.BRICK).setDisplayName(ChatColor.LIGHT_PURPLE + "Charged Draconium Ingot").setGlow().build()),
    //Armors
    DRACONIC_HELMET(new ItemBuilder(Material.LEATHER_HELMET).setColor(Color.PURPLE).setLore(new ArrayList<>(Arrays.asList("Chargable", "Energy: 0 / 100")))
            .setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Helmet").setUnbreakable(true).addProtection(2)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE).build()),
    DRACONIC_CHESTPLATE(new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(Color.PURPLE).setLore(new ArrayList<>(Arrays.asList("Chargable", "Energy: 0 / 100")))
            .setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Chestplate").setUnbreakable(true).addProtection(2)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE).build()),
    DRACONIC_LEGGINGS(new ItemBuilder(Material.LEATHER_LEGGINGS).setColor(Color.PURPLE).setLore(new ArrayList<>(Arrays.asList("Chargable", "Energy: 0 / 100")))
            .setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Leggings").setUnbreakable(true).addProtection(2)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE).build()),
    DRACONIC_BOOTS(new ItemBuilder(Material.LEATHER_BOOTS).setColor(Color.PURPLE).setLore(new ArrayList<>(Arrays.asList("Chargable", "Energy: 0 / 100")))
            .setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Boots").setUnbreakable(true).addProtection(2).addEnchant(Enchantment.FROST_WALKER, 1)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE).build()),
    //Plates
    IRON_PLATE(new ItemBuilder(Material.IRON_INGOT).setDisplayName(ChatColor.GRAY + "Iron Plate").build()),
    GOLD_PLATE(new ItemBuilder(Material.IRON_INGOT).setDisplayName(ChatColor.GOLD + "Gold Plate").build()),
    DIAMOND_PLATE(new ItemBuilder(Material.IRON_INGOT).setDisplayName(ChatColor.AQUA + "Diamond Plate").build()),
    NETHERITE_PLATE(new ItemBuilder(Material.IRON_INGOT).setDisplayName(ChatColor.DARK_GRAY + "Netherite Plate").build()),
    ;

    public final ItemStack stack;

    Items(ItemStack stack) {
        this.stack = stack.clone();
    }

    public static ItemStack[] getAllItems() {
        List<ItemStack> stacks = new ArrayList<>();
        for (Items value : Items.values()) {
            stacks.add(value.stack);
        }
        return stacks.toArray(new ItemStack[0]);
    }
}
