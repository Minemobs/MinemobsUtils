package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.objects.Inventories;
import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandInfo(name = "customitems", aliases = "ci", permission = "minemobsutils.inventories.customitems")
public class CustomItemsCommand extends PluginCommand {

    public CustomItemsCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        Inventories.CUSTOM_ITEMS_GIVER.inv.openInventory(player);
    }
}
