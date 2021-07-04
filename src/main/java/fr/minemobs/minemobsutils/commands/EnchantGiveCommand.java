package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.utils.CommandUtils;
import fr.minemobs.minemobsutils.utils.Items;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EnchantGiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            CommandUtils.senderError(sender);
            return false;
        }

        Player player = (Player) sender;
        for (Items value : Items.values()) {
            player.getInventory().addItem(value.stack);
        }
        return true;
    }
}
