package fr.minemobs.minemobsutils.objects;

public enum Blocks {

    RUBY_ORE(new CustomBlock(1, Items.RUBY.stack)),
    MILK_GENERATOR(new CustomBlock(2, Items.RUBY.stack)),
    ;

    public final CustomBlock block;

    Blocks(CustomBlock block) {
        this.block = block.clone();
    }

    public CustomBlock getBlock() {
        return block;
    }
}
