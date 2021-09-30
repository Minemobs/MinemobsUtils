package fr.minemobs.minemobsutils.commands;

import org.bukkit.entity.Player;

@CommandInfo(name = "craft", alias = {"crafting", "craftingtable", "workbench"}, permission = "minemobsutils.openWorkbench")
public class CraftCommand extends PluginCommand {

    @Override
    public void execute(Player player, String[] args) {
        player.openWorkbench(null, true);
    }
}
