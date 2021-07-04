package fr.minemobs.minemobsutils.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Items {

    GRAPPLING_HOOK(new ItemBuilder(Material.BOW).setLore("It's literally a grappling hook").setDisplayName("Grappling Hook").setGlow().build()),
    ;

    public final ItemStack stack;

    Items(ItemStack stack) {
        this.stack = stack;
    }
}
