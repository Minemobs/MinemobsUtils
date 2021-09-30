package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.objects.Inventories;
import org.bukkit.entity.Player;

@CommandInfo(name = "customitems", alias = "ci", permission = "minemobsutils.inventories.customitems")
public class CustomItemsCommand extends PluginCommand {

    @Override
    public void execute(Player player, String[] args) {
        player.openInventory(Inventories.CUSTOM_ITEMS_GIVER.inv);
    }
}
