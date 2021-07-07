package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.ArrayUtils;
import fr.minemobs.minemobsutils.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class StaffChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            CommandUtils.senderError(sender);
            return false;
        }
        Player player = (Player) sender;

        if(args.length < 1) {
            player.sendMessage("The message is missing");
            return false;
        }
        String arg = ArrayUtils.toString(args);
        System.out.println(arg);
        sendMessageToModerators(player.getUniqueId(), arg);
        return true;
    }

    public static void sendMessageToModerators(UUID uuid, String message) {
        if(message.startsWith("*")){
            message = message.replace("*", "");
        }
        final String msg = ChatColor.GRAY + "[" + ChatColor.RED + " Staff Chat " + ChatColor.GRAY + "] " + ChatColor.RESET + String.format("<%s> ", Bukkit.getPlayer(uuid).getDisplayName()) +
                ChatColor.translateAlternateColorCodes('&', message);
        /*Bukkit.getOnlinePlayers().stream().filter(players -> players.hasPermission(MinemobsUtils.pluginID + ".mod.chat")).forEach(players ->
                players.sendMessage(uuid, msg));*/
        Bukkit.getServer().broadcast(msg, MinemobsUtils.pluginID + ".mod.chat");
    }
}
