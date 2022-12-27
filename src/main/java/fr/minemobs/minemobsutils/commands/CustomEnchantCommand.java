package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.objects.Inventories;
import fr.sunderia.sunderiautils.SunderiaUtils;
import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandInfo(name = "customenchant", aliases = {"ce"}, permission = "minemobsutils.inventories.customenchant")
public class CustomEnchantCommand extends PluginCommand {

    public CustomEnchantCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        Inventories.CUSTOM_ENCHANT_GIVER.inv.openInventory(player);
    }
}
