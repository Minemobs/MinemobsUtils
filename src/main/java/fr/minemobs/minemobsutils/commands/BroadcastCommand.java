package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.utils.ArrayUtils;
import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

@CommandInfo(name = "broadcast", aliases = {"bc"}, requiresPlayer = false)
public class BroadcastCommand extends PluginCommand {

    public BroadcastCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(args.length == 0) return;
        Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.RED + " Broadcast " + ChatColor.GRAY + "] " + ChatColor.RESET +
                ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args)));
    }
}
