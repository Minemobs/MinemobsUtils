package fr.minemobs.minemobsutils.objects;

import com.google.common.collect.ImmutableList;
import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.customblock.CustomBlock;
import fr.minemobs.minemobsutils.customblock.CustomBlock.Builder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Blocks {

    TEMPLATE(new Builder(MinemobsUtils.getKey("template"), 5555).build()),
    RUBY_ORE(new Builder(MinemobsUtils.getKey("ruby_ore"), 1).setDrops(Items.RUBY.stack).build()),
    //Todo: Fix structure rotation
    //HOUSE_MINIATURE(new CustomBlockBuilder(8).build()),
    ;

    public final CustomBlock block;

    Blocks(CustomBlock block) {
        this.block = block.clone();
    }

    public static ImmutableList<ItemStack> getAllBlockItems() {
        return ImmutableList.copyOf(Arrays.stream(values()).map(Blocks::getBlock).map(CustomBlock::getItem).toList());
    }

    public CustomBlock getBlock() {
        return block;
    }
}
