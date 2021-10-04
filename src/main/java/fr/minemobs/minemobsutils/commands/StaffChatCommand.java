package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandInfo(name = "staffchat", alias = {"sc"})
public class StaffChatCommand extends PluginCommand {

    @Override
    public void execute(Player player, String[] args) {
        if(args.length < 1) {
            player.sendMessage("The message is missing");
            return;
        }
        String arg = ArrayUtils.toString(args);
        System.out.println(arg);
        sendMessageToModerators(player.getUniqueId(), arg);
    }

    public static void sendMessageToModerators(UUID uuid, String message) {
        if(message.startsWith("*")){
            message = message.replace("*", "");
        }
        final String msg = ChatColor.GRAY + "[" + ChatColor.RED + "Staff Chat" + ChatColor.GRAY + "] " + ChatColor.RESET + String.format("<%s> ",
                Bukkit.getPlayer(uuid).getDisplayName()) + message;
        Bukkit.getOnlinePlayers().stream().filter(players -> players.hasPermission(MinemobsUtils.pluginID + ".mod.chat")).forEach(players ->
                players.sendMessage(uuid, msg));
    }
}
