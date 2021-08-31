package fr.minemobs.minemobsutils.objects;

import com.google.common.collect.ImmutableList;
import fr.minemobs.minemobsutils.nms.versions.customblock.ICustomBlock;
import fr.minemobs.minemobsutils.utils.ItemBuilder;
import fr.minemobs.minemobsutils.utils.ReflectionUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum Blocks {

    RUBY_ORE(Objects.requireNonNull(customBlock(1, Items.RUBY.stack))),
    //MILK_GENERATOR(Objects.requireNonNull(customBlock(2, Items.RUBY.stack))),
    ;

    public final ICustomBlock block;
    public final ItemStack stack;

    Blocks(ICustomBlock block) {
        this.block = block.clone();
        this.stack = new ItemBuilder(Material.COMMAND_BLOCK).setDisplayName(WordUtils.capitalize(this.toString().replaceAll("_", " ").toLowerCase()))
                .setCustomModelData(block.getCustomModelData()).build();
    }

    public static ImmutableList<ItemStack> getAllBlockItems() {
        return ImmutableList.copyOf(Arrays.stream(values()).map(Blocks::getStack).collect(Collectors.toList()));
    }

    public ICustomBlock getBlock() {
        return block;
    }

    public ItemStack getStack() {
        return stack;
    }

    private static ICustomBlock customBlock(int customModelData, int xp, ItemStack... drop) {
        try {
            Class<? extends ICustomBlock> customBlockClass = ReflectionUtils.getClass("fr.minemobs.minemobsutils.nms.versions.customblock", ICustomBlock.class)
                    .stream().filter(clazz -> clazz.getSimpleName().endsWith(ReflectionUtils.getServerVersion().substring(1))).findFirst().get();
            Constructor<? extends ICustomBlock> constructor = customBlockClass.getConstructor(int.class, int.class, ItemStack[].class);
            return constructor.newInstance(customModelData, xp, drop);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ICustomBlock customBlock(int customModelData, ItemStack... drop) {
        return customBlock(customModelData, 0, drop);
    }

    private static ICustomBlock customBlock(int customModelData, List<ItemStack> drop) {
        return customBlock(customModelData, 0, drop);
    }

    private static ICustomBlock customBlock(int customModelData, int xp, List<ItemStack> drop) {
        return customBlock(customModelData, xp, drop.toArray(new ItemStack[0]));
    }
}
