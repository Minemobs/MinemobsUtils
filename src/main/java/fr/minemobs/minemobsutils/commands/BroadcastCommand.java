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

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            CommandUtils.senderError(sender);
            return false;
        }

        if(args.length == 0) return false;

        String msg = ChatColor.GRAY + "[" + ChatColor.RED + " Broadcast " + ChatColor.GRAY + "] " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args));
        Bukkit.broadcastMessage(msg);
        return true;
    }
}
