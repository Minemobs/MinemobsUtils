package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.utils.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "broadcast", alias = {"bc"}, requiresPlayer = false)
public class BroadcastCommand extends PluginCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(args.length == 0) return;

        String msg = ChatColor.GRAY + "[" + ChatColor.RED + " Broadcast " + ChatColor.GRAY + "] " + ChatColor.RESET + 
                ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args));
        Bukkit.broadcastMessage(msg);
    }
}
