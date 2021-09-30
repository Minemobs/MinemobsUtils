package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandInfo(name = "fly")
public class FlyCommand extends PluginCommand {

    @Override
    public void execute(Player player, String[] args) {
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if(player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage(MinemobsUtils.ebheader + "Your flight ability has been disabled");
            return;
        }
        player.setAllowFlight(true);
        player.sendMessage(MinemobsUtils.ebheader + "Your flight ability has been enabled");
    }
}
