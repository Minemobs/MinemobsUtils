package fr.minemobs.minemobsutils.utils;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandUtils {

    public static void senderError(@NotNull CommandSender sender) {
        sender.sendMessage(MinemobsUtils.ebheader + "Only players can use this command.");
    }

    public static void permissionError(@NotNull CommandSender sender) {
        sender.sendMessage(MinemobsUtils.ebheader + ChatColor.RED + "You do not have the permission to execute this command !");
    }

}
