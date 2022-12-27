package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandInfo(name = "ping")
public class PingCommand extends PluginCommand {

    public PingCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if(args.length == 0) {
            player.sendMessage(MinemobsUtils.header + ChatColor.GREEN + "Pong. " + ChatColor.YELLOW + "You have: " +
                    ChatColor.WHITE + getPing(player) + ChatColor.YELLOW + " ms");
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                player.sendMessage(MinemobsUtils.header + ChatColor.DARK_RED + "This player is offline");
                return;
            }
            player.sendMessage(MinemobsUtils.header + ChatColor.GREEN + target.getName() + ChatColor.YELLOW + " has: " +
                    ChatColor.WHITE + getPing(target) + ChatColor.YELLOW + " ms");
        }
    }

    public int getPing(Player p) {
        return p.getPing();
    }
}
