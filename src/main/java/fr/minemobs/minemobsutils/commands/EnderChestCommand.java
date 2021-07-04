package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderChestCommand implements CommandExecutor {

    private final String basePerm = "minemobsutils.enderchest";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            CommandUtils.senderError(sender);
            return false;
        }
        Player player = (Player) sender;
        switch (args.length) {
            case 0:
                player.openInventory(player.getEnderChest());
                break;
            case 1:
                if(!player.hasPermission(basePerm + ".other")) {
                    CommandUtils.permissionError(player);
                    return false;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null) {
                    player.sendMessage(MinemobsUtils.ebheader + ChatColor.RED + args[0] + " is offline!");
                    return false;
                }
                player.openInventory(target.getEnderChest());
                break;
            case 2:
                if(!player.hasPermission(basePerm + ".other")) {
                    CommandUtils.permissionError(player);
                    return false;
                }
                Player source = Bukkit.getPlayer(args[0]);
                target = Bukkit.getPlayer(args[1]);

                if(source == null) {
                    player.sendMessage(MinemobsUtils.ebheader + ChatColor.RED + args[0] + " is offline!");
                    return false;
                }

                if(target == null) {
                    player.sendMessage(MinemobsUtils.ebheader + ChatColor.RED + args[1] + " is offline!");
                    return false;
                }


                target.openInventory(source.getEnderChest());
                break;
            default:
                break;
        }
        return true;
    }
}
