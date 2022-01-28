package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.objects.item.ItemInfo;
import fr.minemobs.minemobsutils.objects.item.ProjectileInfo;
import fr.minemobs.minemobsutils.utils.Cooldown;
import fr.minemobs.minemobsutils.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public enum Items {

    GRAPPLING_HOOK(new ItemBuilder(Material.FISHING_ROD).setLore("Yeah a new grappling hook").setDisplayName("Grappling Hook").setGlow().build()),
    BATTERY(new ItemBuilder(Material.SUNFLOWER).setDisplayName("Battery").setGlow().build()),
    CRAFTING_TABLE_PORTABLE(new ItemBuilder(Material.STICK).setDisplayName(ChatColor.GRAY + "Portable Crafting Table").setCustomModelData(501).setGlow().build()),
    DYNAMITE(new ItemBuilder(Material.STICK).setDisplayName(ChatColor.RED + "Dynamite").setGlow().build()),
    //STAFFS
    FIREBALL_STAFF(new ItemBuilder(Material.STICK).setDisplayName(ChatColor.LIGHT_PURPLE + "Fireball Staff").setCustomModelData(1024).setGlow().build()),
    WITHER_STAFF(new ItemBuilder(Material.STICK).setDisplayName(ChatColor.BLACK + "Wither Staff").setCustomModelData(1025).setGlow().onInteract(event -> {
        if(event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if(Cooldown.isInCooldown(event.getPlayer().getUniqueId(), Cooldown.CooldownType.WITHER_STAFF)) {
            event.getPlayer().sendMessage(Cooldown.cooldownMessage(event.getPlayer().getUniqueId(), Cooldown.CooldownType.WITHER_STAFF));
            return;
        }
        WitherSkull skull = event.getPlayer().launchProjectile(WitherSkull.class);
        skull.setCharged(true);
        skull.setDirection(event.getPlayer().getEyeLocation().getDirection());
        skull.setBounce(true);

        if(!event.getPlayer().hasPermission(MinemobsUtils.pluginID + ".ignorecooldown")) {
            Cooldown cooldown = new Cooldown(event.getPlayer().getUniqueId(), Cooldown.CooldownType.WITHER_STAFF);
            cooldown.start();
        }
    }).build()),
    //Tools
    HAMMER(new ItemBuilder(Material.IRON_AXE).setDisplayName(ChatColor.GRAY + "Hammer").setGlow().build()),
    WRENCH(new ItemBuilder(Material.STICK).setDisplayName(ChatColor.GRAY + "Wrench").setCustomModelData(256).setGlow().build()),
    //Ingots
    DRACONIUM_INGOT(new ItemBuilder(Material.BRICK).setDisplayName(ChatColor.LIGHT_PURPLE + "Draconium Ingot").build()),
    RUBY(new ItemBuilder(Material.EMERALD).setDisplayName(ChatColor.RED + "Ruby").setGlow().build()),
    CHARGED_DRACONIUM_INGOT(new ItemBuilder(Material.BRICK).setDisplayName(ChatColor.LIGHT_PURPLE + "Charged Draconium Ingot").setGlow().build()),
    //Armors
    DRACONIC_HELMET(new ItemBuilder(Material.LEATHER_HELMET).setColor(Color.PURPLE).setLore(new ArrayList<>(Arrays.asList("Chargable", "Energy: 0 / 100")))
            .setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Helmet").setUnbreakable(true).addProtection(2)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE).build()),
    DRACONIC_CHESTPLATE(new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(Color.PURPLE).setLore(new ArrayList<>(Arrays.asList("Chargable", "Energy: 0 / 100")))
            .setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Chestplate").setUnbreakable(true).addProtection(2)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE).build()),
    DRACONIC_LEGGINGS(new ItemBuilder(Material.LEATHER_LEGGINGS).setColor(Color.PURPLE).setLore(new ArrayList<>(Arrays.asList("Chargable", "Energy: 0 / 100")))
            .setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Leggings").setUnbreakable(true).addProtection(2)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE).build()),
    DRACONIC_BOOTS(new ItemBuilder(Material.LEATHER_BOOTS).setColor(Color.PURPLE).setLore(new ArrayList<>(Arrays.asList("Chargable", "Energy: 0 / 100")))
            .setDisplayName(ChatColor.LIGHT_PURPLE + "Draconic Boots").setUnbreakable(true).addProtection(2).addEnchant(Enchantment.FROST_WALKER, 1)
            .addItemFlag(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_UNBREAKABLE).build()),
    GUN(new ItemBuilder(Material.IRON_HORSE_ARMOR).setLore("A gun that goes VROOOOOOOOM!").setDisplayName(ChatColor.GRAY + "Gun").setGlow().build()),
    //Ammo
    GUN_AMMO(new ItemBuilder(Material.IRON_NUGGET).setDisplayName(ChatColor.GRAY + "Gun Ammo").setGlow().build(), new ProjectileInfo() {
        @Override
        public float damage() {
            return 5;
        }

        @Override
        public Items[] validWeapons() {
            return new Items[]{Items.GUN};
        }
    }),
    FIRE_GUN_AMMO(new ItemBuilder(Material.IRON_NUGGET).setDisplayName(ChatColor.RED + "Fire Gun Ammo").setGlow().build(), new ProjectileInfo() {

        @Override
        public Items[] validWeapons() {
            return new Items[]{Items.GUN};
        }

        @Override
        public boolean isFlammable() {
            return true;
        }

        @Override
        public float damage() {
            return 5;
        }
    }),
    //Plates
    COPPER_PLATE(new ItemBuilder(Material.IRON_INGOT).setDisplayName(ChatColor.GOLD + "Copper Plate").build()),
    IRON_PLATE(new ItemBuilder(Material.IRON_INGOT).setDisplayName(ChatColor.GRAY + "Iron Plate").build()),
    AMETHYST_PLATE(new ItemBuilder(Material.IRON_INGOT).setDisplayName(ChatColor.LIGHT_PURPLE + "Amethyst Plate").build()),
    GOLD_PLATE(new ItemBuilder(Material.IRON_INGOT).setDisplayName(ChatColor.YELLOW + "Gold Plate").build()),
    DIAMOND_PLATE(new ItemBuilder(Material.IRON_INGOT).setDisplayName(ChatColor.AQUA + "Diamond Plate").build()),
    NETHERITE_PLATE(new ItemBuilder(Material.IRON_INGOT).setDisplayName(ChatColor.DARK_GRAY + "Netherite Plate").build()),
    ;

    public final ItemStack stack;
    @Nullable
    public final ItemInfo info;

    Items(ItemStack stack) {
        this.stack = stack.clone();
        this.info = null;
    }

    Items(ItemStack stack, @NotNull ItemInfo info) {
        this.stack = stack.clone();
        this.info = info;
    }

    public static boolean hasItemInfo(Items item) {
        return item.info != null;
    }

    public static Items[] getAllItemsWithItemInfo(Class<? extends ItemInfo> infos) {
        return Arrays.stream(Items.values()).filter(Items::hasItemInfo).filter(item -> infos.isAssignableFrom(item.info.getClass())).toArray(Items[]::new);
    }

    public ItemStack getStack() {
        return stack;
    }

    public static ItemStack[] getAllItems() {
        return Arrays.stream(Items.values()).map(Items::getStack).toArray(ItemStack[]::new);
    }

}
