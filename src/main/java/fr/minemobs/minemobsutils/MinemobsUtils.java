package fr.minemobs.minemobsutils;

import fr.minemobs.minemobsutils.commands.ColorCommand;
import fr.minemobs.minemobsutils.commands.CraftCommand;
import fr.minemobs.minemobsutils.commands.EnchantGiveCommand;
import fr.minemobs.minemobsutils.commands.EnderChestCommand;
import fr.minemobs.minemobsutils.listener.GrapplingHookListener;
import fr.minemobs.minemobsutils.listener.PlayerListener;
import kr.entree.spigradle.annotations.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SpigotPlugin
public class MinemobsUtils extends JavaPlugin {

    private static MinemobsUtils instance;
    public static final String ebheader = String.format("%s[%sMinemobs Utils%s] ", ChatColor.DARK_GRAY, ChatColor.DARK_RED, ChatColor.DARK_GRAY);

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ebheader + ChatColor.GREEN + "Enabled.");
        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new GrapplingHookListener(), this);
    }

    private void registerCommands() {
        registerCommand("cc", new ColorCommand(), "chatcolor", "colorcode");
        registerCommand("craft", new CraftCommand(), "crafting");
        registerCommand("ec", new EnderChestCommand(), "enderchest");
        registerCommand("egive", new EnchantGiveCommand(), "enchantgive");
        /*getCommand("afly").setExecutor(new CommandAllowFly());
        getCommand("heal").setExecutor(new CommandHeal());
        getCommand("tpsp").setExecutor(new CommandSpawnPoint());
        getCommand("nick").setExecutor(new CommandNick());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("frl").setExecutor(new CommandFakeReload());
        getCommand("tpsg").setExecutor(new StickTeleport());
        getCommand("enchants").setExecutor(new EnchantGive());
        getCommand("enderchest").setExecutor(new EnderChestCommand());
        */
    }

    private void registerCommand(@NotNull String commandName, @NotNull CommandExecutor commandExecutor, String... commandAliases) {
        PluginCommand command = getCommand(commandName);
        if(commandAliases != null && commandAliases.length != 0) command.setAliases(Arrays.asList(commandAliases));
        command.setExecutor(commandExecutor);
    }

    @Override
    public void onDisable() {

    }

    public static MinemobsUtils getInstance() {
        return instance;
    }
}
