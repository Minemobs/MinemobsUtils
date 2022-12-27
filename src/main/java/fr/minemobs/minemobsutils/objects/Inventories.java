package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.listener.EnchantmentListener;
import fr.sunderia.sunderiautils.utils.Gui;
import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import fr.sunderia.sunderiautils.utils.ItemStackUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public enum Inventories {

    CUSTOM_ENCHANT_GIVER(new InventoryBuilder(ChatColor.RED + "Custom Enchant Giver").setRows(3).addItems(CustomEnchants.toEnchantedBook()).onClick((event, gui) -> {
        event.setCancelled(true);
        if(ItemStackUtils.isAirOrNull(event.getCurrentItem())) return;
        ItemStack is = event.getWhoClicked().getInventory().getItemInMainHand();
        CustomEnchants value = CustomEnchants.valueOf(event.getCurrentItem().getItemMeta().getDisplayName().toUpperCase().replaceAll("\\s+", "_"));
        if(EnchantmentListener.hasEnchant(is, value)) {
            EnchantmentListener.removeEnchant(is, value);
        } else {
            //TODO: Readd enchantment check
            /*if(!value.enchantment.canEnchantItem(is)) {
                event.getWhoClicked().sendMessage(MinemobsUtils.header + "You cannot enchant this item with " + value.enchantment.getKey().getKey());
                return;
            }*/
            value.getEnchantment(is);
        }
    }).build()),
    CUSTOM_ITEMS_GIVER(new InventoryBuilder(ChatColor.RED + "Custom Items Giver", 6).addItems(Items.getAllItems())
            .addItems(Blocks.getAllBlockItems()).onClick((event, gui) -> {
                event.setCancelled(true);
                if(ItemStackUtils.isAirOrNull(event.getCurrentItem())) return;
                if(event.getClickedInventory() instanceof PlayerInventory) return;
                event.getWhoClicked().getInventory().addItem((event.isShiftClick() ? new ItemBuilder(event.getCurrentItem().clone()).setAmount(64).build() :
                        event.getCurrentItem().clone()));
        }).build()),
    ;

    public final Gui inv;

    Inventories(Gui inv) {
        this.inv = inv;
    }
}
