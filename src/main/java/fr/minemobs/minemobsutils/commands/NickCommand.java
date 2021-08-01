package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.support.SkinRestorer;
import fr.minemobs.minemobsutils.utils.ArrayUtils;
import fr.minemobs.minemobsutils.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NickCommand implements CommandExecutor {

    public static final Map<Player, String> realNames = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            CommandUtils.senderError(sender);
            return false;
        }
        Player player = (Player) sender;
        if(args.length < 1) {
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        String nick;
        if(target != null && target != player) {
            if(args[1].equalsIgnoreCase("reset")) {
                changeName(realNames.get(target), target);
                setSkin(target);
                player.sendMessage(MinemobsUtils.ebheader + target.getName() + "'s nickname has been reset.");
                return true;
            }
            nick = ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args).replace(realNames.get(target) + " ", ""))
                    .replaceAll("\\s+", "");
            if(nick.length() >= 16) {
                nick = nick.substring(0, 15);
            }
            player.sendMessage(MinemobsUtils.ebheader + target.getName() + "'s nickname has been set to " + nick);
            changeName(nick, target);
            setSkin(target);
        } else {
            if(args[0].equalsIgnoreCase("reset")) {
                changeName(realNames.get(player), player);
                setSkin(player);
                player.sendMessage(MinemobsUtils.ebheader + "Your nickname has been reset.");
                return true;
            }
            nick = ChatColor.translateAlternateColorCodes('&', ArrayUtils.toString(args)).replaceAll("\\s+", "");
            player.sendMessage(MinemobsUtils.ebheader + "Your nickname has been set to " + nick);
            changeName(nick, player);
            setSkin(player);
        }
        return true;
    }

    /**
     * @author Weefle
     * @see <a href="https://github.com/Weefle/SkWaze/blob/1941eb3b719fff4b2548ff7ed638264d5bf74e36/src/fr/weefle/waze/effects/WazeEffectNametag.java">Link to github</a>
     */
    public static void changeName(String name, Player player) {
        try {
            Method getHandle = player.getClass().getMethod("getHandle",
                    (Class<?>[]) null);
            try {
                Class.forName("com.mojang.authlib.GameProfile");
            } catch (ClassNotFoundException e) {
                Bukkit.broadcastMessage("CHANGE NAME METHOD DOES NOT WORK IN 1.7 OR LOWER!");
                return;
            }
            Object profile = getHandle.invoke(player).getClass()
                    .getMethod("getProfile")
                    .invoke(getHandle.invoke(player));
            Field ff = profile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(profile, name);
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.hidePlayer(MinemobsUtils.getInstance(), player);
                players.showPlayer(MinemobsUtils.getInstance(), player);
            }
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void setSkin(Player player) {
        if(Bukkit.getPluginManager().getPlugin("SkinsRestorer") == null) return;
        SkinRestorer.setSkin(player);
    }
}
