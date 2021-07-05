package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.enchants.CustomEnchantmentWrapper;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum CustomEnchants {

    TELEPATHY(new CustomEnchantmentWrapper("telepathy", "Telepathy", 1)),
    ZEUS(new CustomEnchantmentWrapper("zeus", "Zeus", 1)),
    TEAM_TREE(new CustomEnchantmentWrapper("teamtree", "Team Tree", 1)),
    EXPLOSION(new CustomEnchantmentWrapper("explosion", "Explosion", 1)),
    HAMMER(new CustomEnchantmentWrapper("hammer", "Hammer", 1)),
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
}
