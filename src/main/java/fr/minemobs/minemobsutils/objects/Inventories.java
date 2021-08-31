package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.InventoryBuilder;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum Inventories {

    CUSTOM_ENCHANT_GIVER(new InventoryBuilder(ChatColor.RED + "Custom Enchant Giver").setRows(3).setCancelled().addItems(CustomEnchants.toEnchantedBook()).onClick(event -> {
        if(ItemStackUtils.isAirOrNull(event.getCurrentItem())) return;
        ItemStack is = event.getWhoClicked().getInventory().getItemInMainHand();
        ItemMeta meta = is.getItemMeta();
        CustomEnchants value = CustomEnchants.valueOf(event.getCurrentItem().getItemMeta().getDisplayName().toUpperCase());
        List<String> lore = new ArrayList<>();
        if(meta.hasLore()) lore = meta.getLore();
        String enchantmentLore = ChatColor.GRAY + StringUtils.capitalize(value.enchantment.getKey().getKey()) + " I";
        if(meta.hasEnchants() && meta.hasEnchant(value.enchantment)) {
            meta.removeEnchant(value.enchantment);
            lore.remove(enchantmentLore);
        } else {
            if(!value.enchantment.canEnchantItem(is)) {
                event.getWhoClicked().sendMessage(MinemobsUtils.ebheader + "You cannot enchant this item with " + value.enchantment.getKey().getKey());
                return;
            }
            meta.addEnchant(value.enchantment, 1, true);
            lore.add(enchantmentLore);
        }
        meta.setLore(lore);
        is.setItemMeta(meta);
    }).build()),
    CUSTOM_ITEMS_GIVER(new InventoryBuilder(ChatColor.RED + "Custom Items Giver", 3).addItems(Items.getAllItems())
            .addItems(Blocks.getAllBlockItems()).setCancelled().onClick(event -> {
        if(ItemStackUtils.isAirOrNull(event.getCurrentItem())) return;
        event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
    }).build()),
    ;

    public final Inventory inv;

    Inventories(Inventory inv) {
        this.inv = inv;
    }
}
