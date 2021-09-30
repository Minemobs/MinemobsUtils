package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.objects.Inventories;
import org.bukkit.entity.Player;

@CommandInfo(name = "customenchant", alias = "ce", permission = "minemobsutils.inventories.customenchant")
public class CustomEnchantCommand extends PluginCommand {

    @Override
    public void execute(Player player, String[] args) {
        player.openInventory(Inventories.CUSTOM_ENCHANT_GIVER.inv);
    }
}
