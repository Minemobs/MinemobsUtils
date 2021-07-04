package fr.minemobs.minemobsutils.utils;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.command.CommandSender;

public class CommandUtils {

    public static void senderError(CommandSender sender) {
        sender.sendMessage(MinemobsUtils.ebheader + "Seuls les joueurs peuvent effectuer cette commande.");
    }

}
