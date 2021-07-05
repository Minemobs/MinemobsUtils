package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.CommandUtils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            CommandUtils.senderError(sender);
            return false;
        }
        Player player = (Player) sender;
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return true;
        if(player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage(MinemobsUtils.ebheader + "You can't fly now");
            return true;
        }
        player.setAllowFlight(true);
        player.sendMessage(MinemobsUtils.ebheader + "You can now fly");
        return true;
    }
}
