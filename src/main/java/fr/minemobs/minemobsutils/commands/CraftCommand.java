package fr.minemobs.minemobsutils.commands;

import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandInfo(name = "craft", aliases = {"crafting", "craftingtable", "workbench"}, permission = "minemobsutils.openWorkbench")
public class CraftCommand extends PluginCommand {

    public CraftCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        player.openWorkbench(null, true);
    }
}
