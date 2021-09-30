package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.support.SkinRestorer;
import fr.minemobs.minemobsutils.utils.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandInfo(name = "nick")
public class NickCommand extends PluginCommand {

    @Override
    public void execute(Player player, String[] args) {
        if(args.length < 1) {
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        String nick;
        if(target != null && target != player) {
            if(args[1].equalsIgnoreCase("reset")) {
                target.setDisplayName(target.getName());
                target.setPlayerListName(target.getName());
                player.sendMessage(MinemobsUtils.ebheader + target.getName() + "'s nickname has been reset.");
                setSkin(target);
                return;
            }
            nick = ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args).replace(target.getName() + " ", ""));
            target.setDisplayName(nick);
            target.setPlayerListName(nick);
            player.sendMessage(MinemobsUtils.ebheader + target.getName() + "'s nickname has been set to " + nick);
            setSkin(target);
        } else {
            if(args[0].equalsIgnoreCase("reset")) {
                player.setDisplayName(player.getName());
                player.setPlayerListName(player.getName());
                player.sendMessage(MinemobsUtils.ebheader + "Your nickname has been reset.");
                setSkin(player);
                return;
            }
            nick = ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args));
            setName(player, nick);
            setSkin(player);
        }
    }

    private void setName(Player player, String nick) {
        player.setDisplayName(nick);
        player.setPlayerListName(nick);
        player.sendMessage(MinemobsUtils.ebheader + "Your nickname has been set to " + nick);
    }

    private void setSkin(Player player) {
        if(Bukkit.getPluginManager().getPlugin("SkinsRestorer") == null) return;
        SkinRestorer.setSkin(player);
    }
}
