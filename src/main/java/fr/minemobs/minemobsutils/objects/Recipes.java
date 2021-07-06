package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public enum Recipes {

    //Ingots
    DRACONIUM_INGOT(new ShapedRecipe(new NamespacedKey(MinemobsUtils.getInstance(), "draconium_ingot"), Items.DRACONIUM_INGOT.stack)
            .shape("EEE", "EDE", "EEE")
            .setIngredient('E', Material.ENDER_EYE)
            .setIngredient('D', Material.DIAMOND_BLOCK)),
    //Tools
    HAMMER(new ShapedRecipe(new NamespacedKey(MinemobsUtils.getInstance(), "hammer"), Items.HAMMER.stack)
    .shape("III", "ISI", " S ")
    .setIngredient('I', new RecipeChoice.MaterialChoice(Material.IRON_BLOCK))
    .setIngredient('S', new RecipeChoice.MaterialChoice(Material.STICK))),
    //Armors
    DRACONIUM_HELMET(new ShapedRecipe(new NamespacedKey(MinemobsUtils.getInstance(), "draconium_helmet"), Items.DRACONIC_HELMET.stack)
            .shape("EEE", "E E", "   ")
            .setIngredient('E', new RecipeChoice.ExactChoice(Items.DRACONIUM_INGOT.stack))),
    DRACONIUM_CHESTPLATE(new ShapedRecipe(new NamespacedKey(MinemobsUtils.getInstance(), "draconium_chestplate"), Items.DRACONIC_CHESTPLATE.stack)
            .shape("E E", "EEE", "EEE")
            .setIngredient('E', new RecipeChoice.ExactChoice(Items.DRACONIUM_INGOT.stack))),
    DRACONIUM_LEGGINGS(new ShapedRecipe(new NamespacedKey(MinemobsUtils.getInstance(), "draconium_leggings"), Items.DRACONIC_LEGGINGS.stack)
            .shape("EEE", "E E", "E E")
            .setIngredient('E', new RecipeChoice.ExactChoice(Items.DRACONIUM_INGOT.stack))),
    DRACONIUM_BOOTS(new ShapedRecipe(new NamespacedKey(MinemobsUtils.getInstance(), "draconium_boots"), Items.DRACONIC_BOOTS.stack)
            .shape("   ", "E E", "E E")
            .setIngredient('E', new RecipeChoice.ExactChoice(Items.DRACONIUM_INGOT.stack))),
    //Other
    BATTERY(new ShapedRecipe(new NamespacedKey(MinemobsUtils.getInstance(), "battery"), Items.BATTERY.stack)
    .shape(" E ", "IGI", "IRI")
    .setIngredient('I', new RecipeChoice.ExactChoice(new ItemStack(Items.IRON_PLATE.stack)))
    .setIngredient('R', new RecipeChoice.MaterialChoice(Material.REDSTONE_BLOCK))
    .setIngredient('G', new RecipeChoice.MaterialChoice(Material.GOLD_INGOT))
    .setIngredient('E', new RecipeChoice.MaterialChoice(Material.EMERALD))),

    IRON_PLATE(new ShapelessRecipe(new NamespacedKey(MinemobsUtils.getInstance(), "iron_plate"), Items.IRON_PLATE.stack)
            .addIngredient(Material.IRON_BLOCK)
            .addIngredient(new RecipeChoice.ExactChoice(Items.HAMMER.stack))),
    ;

    private final ShapedRecipe recipe;
    private final ShapelessRecipe shapelessRecipe;

    Recipes(ShapedRecipe recipe) {
        this.recipe = recipe;
        this.shapelessRecipe = null;
    }

    Recipes(ShapelessRecipe shapelessRecipe) {
        this.shapelessRecipe = shapelessRecipe;
        this.recipe = null;
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }

    public ShapelessRecipe getShapelessRecipe() {
        return shapelessRecipe;
    }
}
