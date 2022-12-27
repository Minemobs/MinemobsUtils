package fr.minemobs.minemobsutils.objects;

import fr.sunderia.sunderiautils.enchantments.CustomEnchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum CustomEnchants {

    TELEPATHY("telepathy", new CustomEnchantment.CustomEnchantmentBuilder("telepathy").level(1)),
    ZEUS("telepathy", new CustomEnchantment.CustomEnchantmentBuilder("zeus").level(1)),
    TEAM_TREE("telepathy", new CustomEnchantment.CustomEnchantmentBuilder("team_tree").level(1)),
    EXPLOSION("telepathy", new CustomEnchantment.CustomEnchantmentBuilder("explosion").level(1)),
    HAMMER("telepathy", new CustomEnchantment.CustomEnchantmentBuilder("hammer").level(1)),
    FURNACE("telepathy", new CustomEnchantment.CustomEnchantmentBuilder("furnace").level(1)),
    TRASHER("telepathy", new CustomEnchantment.CustomEnchantmentBuilder("trasher").level(1)),
    SOUL_BOUND("telepathy", new CustomEnchantment.CustomEnchantmentBuilder("soul_bound").level(1)),
    ;

    private final CustomEnchantment.CustomEnchantmentBuilder enchantment;
    private final String name;

    CustomEnchants(String name, CustomEnchantment.CustomEnchantmentBuilder enchantment) {
        this.name = name;
        this.enchantment = enchantment;
    }

    public String getName() {
        return name;
    }

    public CustomEnchantment getEnchantment(ItemStack stack) {
        return enchantment.addOnItem(stack);
    }

    public static ItemStack[] toEnchantedBook() {
        List<ItemStack> stacks = new ArrayList<>();
        for (CustomEnchants value : CustomEnchants.values()) {
            ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
            value.getEnchantment(is);
            stacks.add(is);
        }
        return stacks.toArray(ItemStack[]::new);
    }
}