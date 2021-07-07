package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.utils.ArrayUtils;
import fr.minemobs.minemobsutils.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            CommandUtils.senderError(sender);
            return false;
        }
        Player player = (Player) sender;
        if(args.length < 1) {
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        String nick;
        if(target != null) {
            nick = ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args).replace(target.getName() + " ", ""));
            target.setDisplayName(nick);
            target.setPlayerListName(nick);
            player.sendMessage(target.getName() + "'s nickname has been set to " + nick);
        } else {
            nick = ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args));
            player.setDisplayName(nick);
            player.setPlayerListName(nick);
            player.sendMessage("Your nickname has been set to " + nick);
        }
        return true;
    }
}
