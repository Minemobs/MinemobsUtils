package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.CommandUtils;
import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandInfo(name = "enderchest", aliases = "ec", permission = "minemobsutils.enderchest")
public class EnderChestCommand extends PluginCommand {

    public EnderChestCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        switch (args.length) {
            case 0 -> player.openInventory(player.getEnderChest());
            case 1 -> {
                if (!player.hasPermission(getInfo().permission() + ".other")) {
                    CommandUtils.permissionError(player);
                    return;
                }
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(MinemobsUtils.header + ChatColor.RED + args[0] + " is offline!");
                    return;
                }
                player.openInventory(target.getEnderChest());
            }
            case 2 -> {
                if (!player.hasPermission(getInfo().permission() + ".other")) {
                    CommandUtils.permissionError(player);
                    return;
                }
                Player source = Bukkit.getPlayer(args[0]);
                Player target = Bukkit.getPlayer(args[1]);

                if (source == null) {
                    player.sendMessage(MinemobsUtils.header + ChatColor.RED + args[0] + " is offline!");
                    return;
                }

                if (target == null) {
                    player.sendMessage(MinemobsUtils.header + ChatColor.RED + args[1] + " is offline!");
                    return;
                }

                target.openInventory(source.getEnderChest());
            }
            default -> player.sendMessage(ChatColor.GRAY + "[ " + ChatColor.YELLOW + "Warning " + ChatColor.GRAY + "] " + ChatColor.RESET + "You have added too much arguments.");
        }
    }
}
