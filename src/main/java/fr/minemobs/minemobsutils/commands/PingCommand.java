package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            CommandUtils.senderError(sender);
            return false;
        }
        Player player = (Player) sender;
        if(args.length == 0) {
            player.sendMessage(MinemobsUtils.ebheader + ChatColor.GREEN + "Pong. " + ChatColor.YELLOW + "You have: " + ChatColor.WHITE + getPing(player) + ChatColor.YELLOW + " ms");
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                player.sendMessage(MinemobsUtils.ebheader + ChatColor.DARK_RED + "This player is offline");
                return false;
            }
            player.sendMessage(MinemobsUtils.ebheader + ChatColor.GREEN + target.getName() + ChatColor.YELLOW + " has: " + ChatColor.WHITE + getPing(target) + ChatColor.YELLOW + " ms");
        }
        return true;
    }

    public int getPing(Player p) {
        return p.getPing();
    }
}
