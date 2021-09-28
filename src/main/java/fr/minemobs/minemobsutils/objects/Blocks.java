package fr.minemobs.minemobsutils.objects;

import com.google.common.collect.ImmutableList;
import fr.minemobs.minemobsutils.nms.versions.customblock.CustomBlock;
import fr.minemobs.minemobsutils.utils.CustomBlockBuilder;
import fr.minemobs.minemobsutils.utils.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Blocks {

    TEMPLATE(new CustomBlockBuilder(5555).build()),
    RUBY_ORE(new CustomBlockBuilder(1).addDrop(Items.RUBY.stack).build()),
    MILK_GENERATOR(new CustomBlockBuilder(2).addDrop(Items.RUBY.stack).build()),
    ;

    public final CustomBlock block;
    public final ItemStack stack;

    Blocks(CustomBlock block) {
        this.block = block.clone();
        this.stack = new ItemBuilder(Material.LIME_GLAZED_TERRACOTTA).setDisplayName(WordUtils.capitalize(this.toString().replaceAll("_", " ").toLowerCase()))
                .setCustomModelData(block.getCustomModelData()).build();
    }

    public static ImmutableList<ItemStack> getAllBlockItems() {
        return ImmutableList.copyOf(Arrays.stream(values()).map(Blocks::getStack).collect(Collectors.toList()));
    }

    public CustomBlock getBlock() {
        return block;
    }

    public ItemStack getStack() {
        return stack;
    }
}
