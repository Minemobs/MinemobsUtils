package fr.minemobs.minemobsutils.objects.item;

import fr.minemobs.minemobsutils.objects.Items;

public interface ProjectileInfo extends ItemInfo {

    public float damage();
    public default boolean isFlammable() {
        return false;
    }
    public Items[] validWeapons();

}
