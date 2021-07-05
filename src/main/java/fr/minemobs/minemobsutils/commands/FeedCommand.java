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

public class FeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            CommandUtils.senderError(sender);
            return false;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("minemobsutils.feed")) {
            CommandUtils.permissionError(player);
            return false;
        }
        if(args.length == 0) {
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.sendMessage(MinemobsUtils.ebheader + ChatColor.GREEN + "You have been fed");
        } else {
            if(!player.hasPermission("minemobsutils.feed.other")) {
                CommandUtils.permissionError(player);
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                player.sendMessage(MinemobsUtils.ebheader + ChatColor.GREEN + args[0] + ChatColor.RED + " do not exist!");
                return false;
            }
            target.setSaturation(20);
            target.setFoodLevel(20);
            player.sendMessage(MinemobsUtils.ebheader + ChatColor.GREEN + target.getName() + "has been fed");
            target.sendMessage(MinemobsUtils.ebheader + ChatColor.GREEN + "You have been fed");
        }
        return true;
    }
}
