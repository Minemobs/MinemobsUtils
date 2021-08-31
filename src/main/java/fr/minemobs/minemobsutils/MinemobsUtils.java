package fr.minemobs.minemobsutils;

import fr.minemobs.minemobsutils.commands.*;
import fr.minemobs.minemobsutils.objects.CustomEnchants;
import fr.minemobs.minemobsutils.objects.Recipes;
import fr.minemobs.minemobsutils.utils.ReflectionUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MinemobsUtils extends JavaPlugin {

    private static MinemobsUtils instance;
    private final FileConfiguration config = getConfig();
    public static final String ebheader = String.format("%s[%sMinemobs Utils%s]%s ", ChatColor.DARK_GRAY, ChatColor.DARK_RED, ChatColor.DARK_GRAY, ChatColor.RESET);
    public static final String pluginID = "minemobsutils";
    private final String[] supportedNMSVersions = new String[]{"1_16_R3", "1_17_R1"};

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ebheader + ChatColor.GREEN + "Enabled.");
        if(Arrays.stream(supportedNMSVersions).noneMatch(s -> s.equalsIgnoreCase(ReflectionUtils.getServerVersion().substring(1)))) {
            getLogger().severe("This version is not supported !");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        registerListeners();
        registerCommands();
        registerCrafts();
        CustomEnchants.register();
        //checkUpdates();
        setupBStats();
    }

    private void setupBStats() {
        Metrics metrics = new Metrics(this, 8000);
    }

    private void checkUpdates() {
        /*final SpigetUpdate updater = new SpigetUpdate(this, 80802);
        updater.checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
                if (hasDirectDownload) {
                    if (updater.downloadUpdate()) {
                        // Update downloaded, will be loaded when the server restarts
                        getLogger().info("The new update has been downloaded !");
                    } else {
                        // Update failed
                        getLogger().warning("Update download failed, reason is " + updater.getFailReason());
                    }
                }
            }

            @Override
            public void upToDate() {}
        });*/
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
        ReflectionUtils.getClass("fr.minemobs.minemobsutils.listener", Listener.class).forEach(clazz -> {
            try {
                pm.registerEvents(clazz.newInstance(), this);
            } catch (InstantiationException | IllegalAccessException e) {
                getLogger().severe(e.getMessage());
            }
        });
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
        registerCommand("setcustomblock", new SetCustomBlock(), "scb");
    }

    private void registerCommand(@NotNull String commandName, @NotNull CommandExecutor commandExecutor, @Nullable String... commandAliases) {
        PluginCommand command = getCommand(commandName);
        if (commandAliases != null && commandAliases.length != 0) command.setAliases(Arrays.asList(commandAliases));
        command.setExecutor(commandExecutor);
    }

    @Override
    public void onDisable() {

    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(getInstance(), key);
    }

    public static MinemobsUtils getInstance() {
        return instance;
    }
}
