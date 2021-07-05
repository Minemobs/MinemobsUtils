package fr.minemobs.minemobsutils.objects;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.ItemBuilder;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum Inventories {

    CustomEnchantGiver(SmartInventory.builder().size(3, 9).title(ChatColor.RED + "Custom Enchant Giver").provider(new InventoryProvider(){

        @Override
        public void init(Player player, InventoryContents contents) {
            for (CustomEnchants value : CustomEnchants.values()) {
                contents.add(ClickableItem.of(new ItemBuilder(Material.ENCHANTED_BOOK).setGlow().setDisplayName(StringUtils.capitalize(value.enchantment.getKey().getKey())).build(),
                        e -> {
                            if (ItemStackUtils.isAirOrNull(player.getInventory().getItemInMainHand())) return;
                            e.setCancelled(true);
                            ItemStack is = player.getInventory().getItemInMainHand();
                            ItemMeta meta = is.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            if(meta.hasLore()) lore = meta.getLore();
                            String enchantmentLore = ChatColor.GRAY + StringUtils.capitalize(value.enchantment.getKey().getKey()) + " I";
                            if(meta.hasEnchants() && meta.hasEnchant(value.enchantment)) {
                                meta.removeEnchant(value.enchantment);
                                lore.remove(enchantmentLore);
                            } else {
                                meta.addEnchant(value.enchantment, 1, true);
                                lore.add(enchantmentLore);
                            }
                            meta.setLore(lore);
                            is.setItemMeta(meta);
                        }));
            }
        }

        @Override
        public void update(Player player, InventoryContents contents) {}
    }).manager(MinemobsUtils.getManager()).build()),
    ;

    public final SmartInventory inv;

    Inventories(SmartInventory inv) {
        this.inv = inv;
    }
}
