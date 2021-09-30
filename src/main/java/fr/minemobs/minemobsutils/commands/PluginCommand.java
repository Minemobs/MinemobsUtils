package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.utils.CommandUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PluginCommand implements CommandExecutor {

    private final CommandInfo commandInfo;

    protected PluginCommand() {
        this.commandInfo = getClass().getDeclaredAnnotation(CommandInfo.class);
        Validate.notNull(commandInfo, "Commands must have CommandInfo annotations");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandInfo.requiresPlayer() && !(commandSender instanceof Player)) return false;
        if(!commandInfo.permission().isEmpty() && !commandSender.hasPermission(commandInfo.permission())) {
            CommandUtils.permissionError(commandSender);
            return true;
        }

        if (commandSender instanceof Player) {
            execute((Player) commandSender, strings);
        } else {
            execute(commandSender, strings);
        }
        return true;
    }

    public void execute(Player player, String[] args) {}
    public void execute(CommandSender sender, String[] args) {}

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }
}
