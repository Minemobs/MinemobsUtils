package fr.minemobs.minemobsutils.utils;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtils {

    public static void senderError(CommandSender sender) {
        sender.sendMessage(MinemobsUtils.ebheader + "Seuls les joueurs peuvent effectuer cette commande.");
    }

    public static void permissionError(Player player) {
        player.sendMessage(MinemobsUtils.ebheader + ChatColor.RED + "You do not have the permission to execute this command !");
    }

}
