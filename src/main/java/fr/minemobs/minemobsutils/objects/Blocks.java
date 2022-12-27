package fr.minemobs.minemobsutils.objects;

import com.google.common.collect.ImmutableList;
import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.sunderia.sunderiautils.customblock.CustomBlock;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Blocks {

    TEMPLATE(new CustomBlock.Builder(MinemobsUtils.getKey("template"), 5555).build()),
    RUBY_ORE(new CustomBlock.Builder(MinemobsUtils.getKey("ruby_ore"), 1).setDrops(Items.RUBY.stack).build()),
    //Todo: Fix structure rotation
    //HOUSE_MINIATURE(new CustomBlockBuilder(8).build()),
    ;

    private final CustomBlock block;

    Blocks(CustomBlock block) {
        this.block = block.clone();
    }

    public static ImmutableList<ItemStack> getAllBlockItems() {
        return ImmutableList.copyOf(Arrays.stream(values()).map(Blocks::getBlock).map(CustomBlock::getAsItem).toList());
    }

    public CustomBlock getBlock() {
        return block;
    }
}
