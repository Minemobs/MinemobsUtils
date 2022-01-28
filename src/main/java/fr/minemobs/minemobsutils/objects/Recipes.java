package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public enum Recipes {

    //Ingots
    DRACONIUM_INGOT(new ShapedRecipe(MinemobsUtils.getKey("draconium_ingot"), Items.DRACONIUM_INGOT.stack)
            .shape("EEE", "EDE", "EEE")
            .setIngredient('E', Material.ENDER_EYE)
            .setIngredient('D', Material.DIAMOND_BLOCK)),
    CHARGED_DRACONIUM_INGOT(new AnvilRecipe(Items.DRACONIUM_INGOT.stack, Material.DRAGON_BREATH, Items.CHARGED_DRACONIUM_INGOT.stack)),
    //Tools
    HAMMER(new ShapedRecipe(MinemobsUtils.getKey("hammer"), Items.HAMMER.stack)
    .shape("III", "ISI", " S ")
    .setIngredient('I', new RecipeChoice.MaterialChoice(Material.IRON_BLOCK))
    .setIngredient('S', new RecipeChoice.MaterialChoice(Material.STICK))),
    //Armors
    DRACONIUM_HELMET(new ShapedRecipe(MinemobsUtils.getKey("draconium_helmet"), Items.DRACONIC_HELMET.stack)
            .shape("EEE", "E E", "   ")
            .setIngredient('E', new RecipeChoice.ExactChoice(Items.CHARGED_DRACONIUM_INGOT.stack))),
    DRACONIUM_CHESTPLATE(new ShapedRecipe(MinemobsUtils.getKey("draconium_chestplate"), Items.DRACONIC_CHESTPLATE.stack)
            .shape("E E", "EEE", "EEE")
            .setIngredient('E', new RecipeChoice.ExactChoice(Items.CHARGED_DRACONIUM_INGOT.stack))),
    DRACONIUM_LEGGINGS(new ShapedRecipe(MinemobsUtils.getKey("draconium_leggings"), Items.DRACONIC_LEGGINGS.stack)
            .shape("EEE", "E E", "E E")
            .setIngredient('E', new RecipeChoice.ExactChoice(Items.CHARGED_DRACONIUM_INGOT.stack))),
    DRACONIUM_BOOTS(new ShapedRecipe(MinemobsUtils.getKey("draconium_boots"), Items.DRACONIC_BOOTS.stack)
            .shape("   ", "E E", "E E")
            .setIngredient('E', new RecipeChoice.ExactChoice(Items.CHARGED_DRACONIUM_INGOT.stack))),
    //Weapons
    FIREBALL_STAFF(new ShapedRecipe(MinemobsUtils.getKey("fireball_staff"), Items.FIREBALL_STAFF.stack)
            .shape("FDF", " S ", " S ")
            .setIngredient('S', Material.STICK)
            .setIngredient('D', Material.DISPENSER)
            .setIngredient('F', Material.FIRE_CHARGE)),
    GUN(new ShapedRecipe(MinemobsUtils.getKey("gun"), Items.GUN.stack)
            .shape(" PR", "NSP", "PPP")
            .setIngredient('R', new RecipeChoice.MaterialChoice(Material.REDSTONE))
            .setIngredient('N', new RecipeChoice.MaterialChoice(Material.IRON_NUGGET))
            .setIngredient('P', new RecipeChoice.ExactChoice(Items.IRON_PLATE.stack))
            .setIngredient('S', new RecipeChoice.MaterialChoice(Material.STICK))),
    //AMMO
    GUN_AMMO(new ShapedRecipe(MinemobsUtils.getKey("gun_ammo"), Items.GUN_AMMO.stack)
            .shape(" G ", "GPG", " G ")
            .setIngredient('G', new RecipeChoice.MaterialChoice(Material.GUNPOWDER))
            .setIngredient('P', new RecipeChoice.ExactChoice(Items.IRON_PLATE.stack))),
    FIRE_GUN_AMMO(new ShapedRecipe(MinemobsUtils.getKey("fire_gun_ammo"), Items.FIRE_GUN_AMMO.stack)
            .shape(" F ", "FAF", " F ")
            .setIngredient('A', new RecipeChoice.ExactChoice(Items.GUN_AMMO.stack))
            .setIngredient('F', new RecipeChoice.MaterialChoice(Material.FIRE_CHARGE))),
    //Other
    BATTERY(new ShapedRecipe(MinemobsUtils.getKey("battery"), Items.BATTERY.stack)
    .shape(" E ", "IGI", "IRI")
    .setIngredient('I', new RecipeChoice.ExactChoice(new ItemStack(Items.IRON_PLATE.stack)))
    .setIngredient('R', new RecipeChoice.MaterialChoice(Material.REDSTONE_BLOCK))
    .setIngredient('G', new RecipeChoice.MaterialChoice(Material.GOLD_INGOT))
    .setIngredient('E', new RecipeChoice.MaterialChoice(Material.EMERALD))),

    PORTABLE_CRAFTING_TABLE(new ShapelessRecipe(MinemobsUtils.getKey("portable_crafting_table"), Items.CRAFTING_TABLE_PORTABLE.stack)
            .addIngredient(Material.STICK)
            .addIngredient(Material.CRAFTING_TABLE)),
    DYNAMITE(new ShapelessRecipe(MinemobsUtils.getKey("dynamite"), Items.DYNAMITE.stack)
            .addIngredient(2, Material.STRING)
            .addIngredient(4, Material.TNT)),
    ;

    private final ShapedRecipe recipe;
    private final ShapelessRecipe shapelessRecipe;
    private final AnvilRecipe anvilRecipe;

    Recipes(ShapedRecipe recipe) {
        this.recipe = recipe;
        this.shapelessRecipe = null;
        this.anvilRecipe = null;
    }

    Recipes(ShapelessRecipe shapelessRecipe) {
        this.shapelessRecipe = shapelessRecipe;
        this.recipe = null;
        this.anvilRecipe = null;
    }

    Recipes(AnvilRecipe anvilRecipe) {
        this.shapelessRecipe = null;
        this.recipe = null;
        this.anvilRecipe = anvilRecipe;
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }

    public ShapelessRecipe getShapelessRecipe() {
        return shapelessRecipe;
    }

    public AnvilRecipe getAnvilRecipe() {
        return anvilRecipe;
    }
}
