package fr.minemobs.minemobsutils;

import fr.minemobs.minemobsutils.commands.*;
import fr.minemobs.minemobsutils.listener.EnchantmentListener;
import fr.minemobs.minemobsutils.listener.GrapplingHookListener;
import fr.minemobs.minemobsutils.listener.PlayerListener;
import fr.minemobs.minemobsutils.objects.CustomEnchants;
import kr.entree.spigradle.annotations.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        CustomEnchants.register();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new GrapplingHookListener(), this);
        pm.registerEvents(new EnchantmentListener(), this);
    }

    private void registerCommands() {
        registerCommand("cc", new ColorCommand(), "chatcolor", "colorcode");
        registerCommand("craft", new CraftCommand(), "crafting");
        registerCommand("ec", new EnderChestCommand(), "enderchest");
        registerCommand("gh", new EnchantGiveCommand(), "grapplinghook");
        registerCommand("ping", new PingCommand());
        registerCommand("customenchant", new GrapplingHookCommand(), "ce");
        registerCommand("heal", new HealCommand());
        registerCommand("feed", new FeedCommand());
        registerCommand("fly", new FlyCommand());
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

    private void registerCommand(@NotNull String commandName, @NotNull CommandExecutor commandExecutor, @Nullable String... commandAliases) {
        PluginCommand command = getCommand(commandName);
        if (commandAliases != null && commandAliases.length != 0) command.setAliases(Arrays.asList(commandAliases));
        command.setExecutor(commandExecutor);
    }

    @Override
    public void onDisable() {

    }

    public static MinemobsUtils getInstance() {
        return instance;
    }
}
