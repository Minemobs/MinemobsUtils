package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.support.SkinRestorer;
import fr.minemobs.minemobsutils.utils.ArrayUtils;
import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandInfo(name = "nick")
public class NickCommand extends PluginCommand {

    public NickCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if(args.length < 1) return;

        Player target = Bukkit.getPlayer(args[0]);
        String nick;
        if(target != null && target != player) {
            if(args[1].equalsIgnoreCase("reset")) {
                reset(target);
                player.sendMessage(MinemobsUtils.header + target.getName() + "'s nickname has been reset.");
                return;
            }
            nick = ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args).replace(target.getName() + " ", ""));
            setNameAndSkin(target, nick);
            player.sendMessage(MinemobsUtils.header + target.getName() + "'s nickname has been set to " + nick);
        } else {
            if(args[0].equalsIgnoreCase("reset")) {
                reset(player);
                player.sendMessage(MinemobsUtils.header + "Your nickname has been reset.");
                return;
            }
            nick = ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args));
            setNameAndSkin(player, nick);
            player.sendMessage(MinemobsUtils.header + "Your nickname has been set to " + nick);
        }
    }

    private void setNameAndSkin(Player player, String nick) {
        setName(player, nick);
        setSkin(player);
    }

    private void reset(Player player) {
        setName(player, player.getName());
        setSkin(player);
    }

    private void setName(Player player, String nick) {
        player.setDisplayName(nick);
        player.setPlayerListName(nick);
    }

    private void setSkin(Player player) {
        if(Bukkit.getPluginManager().getPlugin("SkinsRestorer") == null) return;
        SkinRestorer.setSkin(player);
    }
}
