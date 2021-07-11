package fr.minemobs.minemobsutils;

import fr.minemobs.minemobsutils.commands.*;
import fr.minemobs.minemobsutils.listener.CraftListener;
import fr.minemobs.minemobsutils.listener.EnchantmentListener;
import fr.minemobs.minemobsutils.listener.GrapplingHookListener;
import fr.minemobs.minemobsutils.listener.PlayerListener;
import fr.minemobs.minemobsutils.objects.CustomEnchants;
import fr.minemobs.minemobsutils.objects.Recipes;
import kr.entree.spigradle.annotations.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

@SpigotPlugin
public class MinemobsUtils extends JavaPlugin {

    private static MinemobsUtils instance;
    private final FileConfiguration config = getConfig();
    public static final String ebheader = String.format("%s[%sMinemobs Utils%s]%s ", ChatColor.DARK_GRAY, ChatColor.DARK_RED, ChatColor.DARK_GRAY, ChatColor.RESET);
    public static final String pluginID = "minemobsutils";

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ebheader + ChatColor.GREEN + "Enabled.");
        registerListeners();
        registerCommands();
        registerCrafts();
        CustomEnchants.register();
    }

    private boolean isNullOrDefault(String configName) {
        return config.get(configName) == null || config.getDefaults().get(configName) == config.get(configName);
    }

    private void registerCrafts() {
        for (Recipes recipe : Arrays.stream(Recipes.values()).filter(recipes -> recipes.getRecipe() != null).collect(Collectors.toList())) {
            getServer().addRecipe(recipe.getRecipe());
        }
        for (Recipes recipe : Arrays.stream(Recipes.values()).filter(recipes -> recipes.getShapelessRecipe() != null).collect(Collectors.toList())) {
            getServer().addRecipe(recipe.getShapelessRecipe());
        }
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new GrapplingHookListener(), this);
        pm.registerEvents(new EnchantmentListener(), this);
        pm.registerEvents(new CraftListener(), this);
    }

    private void registerCommands() {
        registerCommand("cc", new ColorCommand(), "chatcolor", "colorcode");
        registerCommand("craft", new CraftCommand(), "crafting");
        registerCommand("ec", new EnderChestCommand(), "enderchest");
        registerCommand("ci", new CustomItemsCommand(), "customitems");
        registerCommand("ping", new PingCommand());
        registerCommand("customenchant", new CustomEnchantCommand(), "ce");
        registerCommand("heal", new HealCommand());
        registerCommand("feed", new FeedCommand());
        registerCommand("fly", new FlyCommand());
        registerCommand("staffchat", new StaffChatCommand(), "sc");
        registerCommand("nick", new NickCommand());
        registerCommand("broadcast", new BroadcastCommand(), "bc");
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
