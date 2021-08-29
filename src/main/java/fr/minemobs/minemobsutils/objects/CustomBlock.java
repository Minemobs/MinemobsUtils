package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CustomBlock implements Cloneable {

    private final int customModelData;
    private final List<ItemStack> drop;
    private final int xp;

    public CustomBlock(int customModelData, List<ItemStack> drop, int xp) {
        this.customModelData = customModelData;
        this.drop = drop;
        this.xp = xp;
    }

    public CustomBlock(int customModelData, int xp, ItemStack... drop) {
        this(customModelData, Arrays.asList(drop), xp);
    }

    public CustomBlock(int customModelData, List<ItemStack> drop) {
        this(customModelData, drop, 0);
    }

    public CustomBlock(int customModelData, ItemStack... drop) {
        this(customModelData, 0, drop);
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public List<ItemStack> getDrop() {
        return drop;
    }

    public int getXp() {
        return xp;
    }

    @Override
    public CustomBlock clone() {
        try {
            return (CustomBlock) super.clone();
        } catch (CloneNotSupportedException e) {
            MinemobsUtils.getInstance().getLogger().severe(e.getMessage());
        }
        return null;
    }
}
