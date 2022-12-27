package fr.minemobs.minemobsutils.commands;

import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandInfo(name = "top", permission = "minemobsutils.top")
public class TopCommand extends PluginCommand {

    public TopCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        Location loc = player.getWorld().getHighestBlockAt(player.getLocation()).getLocation().clone().add(0, 1, 0);
        loc.setDirection(player.getLocation().getDirection());
        player.teleport(loc);
        player.sendMessage("You have been teleported at X:" + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ());
    }
}
