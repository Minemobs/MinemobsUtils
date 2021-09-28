package fr.minemobs.minemobsutils.utils;

import fr.minemobs.minemobsutils.nms.versions.customblock.CustomBlock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomBlockBuilder {

    private final Material mat;
    private final int cmd;
    private final List<ItemStack> drops = new ArrayList<>();
    private int xp = 0;

    public CustomBlockBuilder(Material mat, int customModelData) {
        this.mat = mat;
        this.cmd = customModelData;
    }

    public CustomBlockBuilder(int customModelData) {
        this(Material.LIME_GLAZED_TERRACOTTA, customModelData);
    }

    public CustomBlockBuilder setXp(int xp) {
        this.xp = xp;
        return this;
    }

    public CustomBlockBuilder addDrop(ItemStack... drops) {
        return addDrop(Arrays.asList(drops));
    }

    public CustomBlockBuilder addDrop(List<ItemStack> drops) {
        this.drops.addAll(drops);
        return this;
    }

    /*public CustomBlock build() {
        try {
            Optional<Class<? extends CustomBlock>> customBlockClass = getCustomBlockClass();
            if(!customBlockClass.isPresent()) return null;
            Constructor<? extends CustomBlock> constructor = customBlockClass.get().getConstructor(Material.class, int.class, int.class, org.bukkit.inventory.ItemStack[].class);
            return constructor.newInstance(mat, cmd, xp, drops.toArray(new ItemStack[0]));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Optional<Class<? extends CustomBlock>> getCustomBlockClass() {
        return ReflectionUtils.getClass("fr.minemobs.minemobsutils.nms.versions.customblock", CustomBlock.class)
                .stream().filter(clazz -> clazz.getSimpleName().endsWith(ReflectionUtils.getServerVersion().substring(1))).findFirst();
    }*/

    public CustomBlock build() {
        return new CustomBlock(mat, cmd, xp, drops.toArray(new ItemStack[0]));
    }
}