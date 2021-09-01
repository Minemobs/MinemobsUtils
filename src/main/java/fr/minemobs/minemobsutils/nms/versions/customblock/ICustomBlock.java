package fr.minemobs.minemobsutils.nms.versions.customblock;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public abstract class ICustomBlock implements Cloneable {

    private final int customModelData;
    private final List<ItemStack> drop;
    private final int xp;
    private PersistentDataContainer data;
    private Location loc;

    public ICustomBlock(int customModelData, List<ItemStack> drop, int xp) {
        this.customModelData = customModelData;
        this.drop = drop;
        this.xp = xp;
    }

    public ICustomBlock(int customModelData, int xp, ItemStack... drop) {
        this(customModelData, Arrays.asList(drop), xp);
    }

    public ICustomBlock(int customModelData, List<ItemStack> drop) {
        this(customModelData, drop, 0);
    }

    protected ICustomBlock(int customModelData, ItemStack... drop) {
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

    @Nullable
    public PersistentDataContainer getData() {
        return data;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void setData(PersistentDataContainer data) {
        this.data = data;
    }

    public abstract void setBlock(Location loc);

    @Override
    public ICustomBlock clone() {
        try {
            return (ICustomBlock) super.clone();
        } catch (CloneNotSupportedException e) {
            MinemobsUtils.getInstance().getLogger().severe(e.getMessage());
        }
        return null;
    }
}
