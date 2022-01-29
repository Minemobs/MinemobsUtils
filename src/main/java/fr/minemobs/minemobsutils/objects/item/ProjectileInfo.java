package fr.minemobs.minemobsutils.objects.item;

import fr.minemobs.minemobsutils.objects.Items;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ProjectileInfo extends ItemInfo {

    static ProjectileInfo getInfo(Items item) {
        return (ProjectileInfo) item.info;
    }

    public float damage();
    public default boolean isFlammable() {
        return false;
    }
    public Items[] validWeapons();

    public default void onHit(Player player, @Nullable ItemStack ammo, LivingEntity target) {}

}
