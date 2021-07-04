package fr.minemobs.minemobsutils.utils;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandUtils {

    public static void senderError(@NotNull CommandSender sender) {
        sender.sendMessage(MinemobsUtils.ebheader + "Seuls les joueurs peuvent effectuer cette commande.");
    }

    public static void permissionError(@NotNull Player player) {
        player.sendMessage(MinemobsUtils.ebheader + ChatColor.RED + "You do not have the permission to execute this command !");
    }

}
