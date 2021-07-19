package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.enchants.CustomEnchantmentWrapper;
import fr.minemobs.minemobsutils.utils.ItemBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CustomEnchants {

    TELEPATHY(new CustomEnchantmentWrapper("telepathy", "Telepathy", 1, EnchantmentTarget.BREAKABLE)),
    ZEUS(new CustomEnchantmentWrapper("zeus", "Zeus", 1, EnchantmentTarget.WEAPON)),
    TEAM_TREE(new CustomEnchantmentWrapper("team_tree", "Team Tree", 1, EnchantmentTarget.WEAPON)),
    EXPLOSION(new CustomEnchantmentWrapper("explosion", "Explosion", 1, EnchantmentTarget.WEAPON)),
    HAMMER(new CustomEnchantmentWrapper("hammer", "Hammer", 1, EnchantmentTarget.TOOL)),
    FURNACE(new CustomEnchantmentWrapper("furnace", "Furnace", 1, EnchantmentTarget.TOOL)),
    ;

    public final Enchantment enchantment;

    CustomEnchants(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public static void register() {
        for (CustomEnchants value : CustomEnchants.values()) {
            if(!Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(value.enchantment)) registerEnchantment(value.enchantment);
        }
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            registered = false;
            e.printStackTrace();
        }
        if(registered) {
            MinemobsUtils.getInstance().getLogger().info(MinemobsUtils.ebheader + "Enchantments registered");
        }
    }

    public static ItemStack[] toEnchantedBook() {
        List<ItemStack> stacks = new ArrayList<>();
        for (CustomEnchants value : CustomEnchants.values()) {
            stacks.add(new ItemBuilder(Material.ENCHANTED_BOOK).setGlow().setDisplayName(StringUtils.capitalize(value.enchantment.getKey().getKey())).build());
        }
        return stacks.toArray(new ItemStack[0]);
    }
}
