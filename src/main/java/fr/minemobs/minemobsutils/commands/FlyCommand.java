package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@CommandInfo(name = "fly")
public class FlyCommand extends PluginCommand {

    public FlyCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if(player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage(MinemobsUtils.header + "Your flight ability has been disabled");
            return;
        }
        player.setAllowFlight(true);
        player.sendMessage(MinemobsUtils.header + "Your flight ability has been enabled");
    }
}
