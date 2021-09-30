package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandInfo(name = "feed", permission = "minemobsutils.feed")
public class FeedCommand extends PluginCommand {

    @Override
    public void execute(Player player, String[] args) {
        if(args.length == 0) {
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.sendMessage(MinemobsUtils.ebheader + ChatColor.GREEN + "You have been fed");
        } else {
            if(!player.hasPermission("minemobsutils.feed.other")) {
                CommandUtils.permissionError(player);
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                player.sendMessage(MinemobsUtils.ebheader + ChatColor.GREEN + args[0] + ChatColor.RED + " do not exist!");
                return;
            }
            target.setSaturation(20);
            target.setFoodLevel(20);
            player.sendMessage(MinemobsUtils.ebheader + ChatColor.GREEN + target.getName() + "has been fed");
            target.sendMessage(MinemobsUtils.ebheader + ChatColor.GREEN + "You have been fed");
        }
    }
}
