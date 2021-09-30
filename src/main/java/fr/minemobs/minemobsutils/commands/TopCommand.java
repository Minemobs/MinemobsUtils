package fr.minemobs.minemobsutils.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandInfo(name = "top", permission = "minemobsutils.top")
public class TopCommand extends PluginCommand {

    @Override
    public void execute(Player player, String[] args) {
        Location loc = player.getWorld().getHighestBlockAt(player.getLocation()).getLocation().clone().add(0, 1, 0);
        loc.setDirection(player.getLocation().getDirection());
        player.teleport(loc);
        player.sendMessage("You have been teleported at X:" + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ());
    }
}
