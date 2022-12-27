package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.CommandUtils;
import fr.minemobs.minemobsutils.utils.CooldownType;
import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import fr.sunderia.sunderiautils.utils.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandInfo(name = "heal", permission = "minemobsutils.heal")
public class HealCommand extends PluginCommand {

    public HealCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (Cooldown.isInCooldown(player.getUniqueId(), CooldownType.HEAL_FEED_COMMAND.getName()) && !player.hasPermission(MinemobsUtils.pluginID + ".ignorecooldown")) {
            player.sendMessage(Cooldown.cooldownMessage(player.getUniqueId(), CooldownType.HEAL_FEED_COMMAND.getName()));
            return;
        }
        if (args.length == 0) {
            player.setHealth(20);
            player.setSaturation(20);
            player.setFoodLevel(20);
            player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
            player.sendMessage(MinemobsUtils.header + ChatColor.GREEN + "You have been healed");
        } else {
            if (!player.hasPermission("minemobsutils.heal.other")) {
                CommandUtils.permissionError(player);
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(MinemobsUtils.header + ChatColor.GREEN + args[0] + ChatColor.RED + " do not exist!");
                return;
            }
            target.setHealth(20);
            target.setSaturation(20);
            target.setFoodLevel(20);
            target.getActivePotionEffects().forEach(effect -> target.removePotionEffect(effect.getType()));
            player.sendMessage(MinemobsUtils.header + ChatColor.GREEN + target.getName() + "has been healed");
            target.sendMessage(MinemobsUtils.header + ChatColor.GREEN + "You have been healed");
        }
        if(!player.hasPermission(MinemobsUtils.pluginID + ".ignorecooldown")) {
            Cooldown cooldown = new Cooldown(player.getUniqueId(), CooldownType.HEAL_FEED_COMMAND.getName(), CooldownType.HEAL_FEED_COMMAND.getCooldownInSeconds());
            cooldown.start();
        }
    }
}
