package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandInfo(name = "enderchest", alias = "ec", permission = "minemobsutils.enderchest")
public class EnderChestCommand extends PluginCommand {

    @Override
    public void execute(Player player, String[] args) {
        String basePerm = "minemobsutils.enderchest";
        switch (args.length) {
            default:
                player.sendMessage(ChatColor.GRAY + "[ " + ChatColor.YELLOW + "Warning " + ChatColor.GRAY + "] " + ChatColor.RESET + "You have added too much arguments.");
            case 0:
                player.openInventory(player.getEnderChest());
                break;
            case 1:
                if(!player.hasPermission(basePerm + ".other")) {
                    CommandUtils.permissionError(player);
                    return;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null) {
                    player.sendMessage(MinemobsUtils.ebheader + ChatColor.RED + args[0] + " is offline!");
                    return;
                }
                player.openInventory(target.getEnderChest());
                break;
            case 2:
                if(!player.hasPermission(basePerm + ".other")) {
                    CommandUtils.permissionError(player);
                    return;
                }
                Player source = Bukkit.getPlayer(args[0]);
                target = Bukkit.getPlayer(args[1]);

                if(source == null) {
                    player.sendMessage(MinemobsUtils.ebheader + ChatColor.RED + args[0] + " is offline!");
                    return;
                }

                if(target == null) {
                    player.sendMessage(MinemobsUtils.ebheader + ChatColor.RED + args[1] + " is offline!");
                    return;
                }

                target.openInventory(source.getEnderChest());
                break;
        }
    }
}
