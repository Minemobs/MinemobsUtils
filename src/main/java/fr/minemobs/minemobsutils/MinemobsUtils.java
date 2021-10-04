package fr.minemobs.minemobsutils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.minemobs.minemobsutils.commands.ColorCommand;
import fr.minemobs.minemobsutils.commands.CommandInfo;
import fr.minemobs.minemobsutils.customblock.CustomBlock;
import fr.minemobs.minemobsutils.listener.CustomBlockListener;
import fr.minemobs.minemobsutils.objects.CustomEnchants;
import fr.minemobs.minemobsutils.objects.Recipes;
import fr.minemobs.minemobsutils.utils.ReflectionUtils;
import fr.minemobs.minemobsutils.utils.UpdateChecker;
import kr.entree.spigradle.annotations.SpigotPlugin;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

@SpigotPlugin
public class MinemobsUtils extends JavaPlugin {

    private static MinemobsUtils instance;
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
    public static final String ebheader = String.format("%s[%sMinemobs Utils%s]%s ", ChatColor.DARK_GRAY, ChatColor.DARK_RED, ChatColor.DARK_GRAY, ChatColor.RESET);
    public static final String pluginID = "minemobsutils";
    private final String[] supportedNMSVersions = new String[]{"1_17_R1"};
    //configs
    private final FileConfiguration config = getConfig();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        checkUpdates();
        ConfigurationSerialization.registerClass(CustomBlock.class, "customblock");
        try {
            loadCustomBlocks();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
        setupBStats();
    }

    private void checkUpdates() {
        new UpdateChecker().getVersion(version -> {
            if(this.getDescription().getVersion().equalsIgnoreCase(version) ||
                    Integer.parseInt(this.getDescription().getVersion().split("\\.")[1]) > Integer.parseInt(version.split("\\.")[1])) {
                getLogger().info("There is no new update available.");
                return;
            }
            getLogger().info("There is a new update available.");
            String downloadURL = "https://api.spiget.org/v2/resources/80802/download";
            try {
                File pluginFolder = new File(getServer().getWorldContainer().getAbsolutePath() + File.separatorChar + "plugins" + File.separatorChar);
                FileUtils.copyURLToFile(new URL(downloadURL), new File(pluginFolder, this.getDescription().getName() + ".jar"),
                        10000, 10000);
                getLogger().info("The new version has been downloaded !");
            } catch (IOException exception) {
                getLogger().severe("Could not download the latest version.\n" + exception.getMessage());
            }
        });
    }

    private void loadCustomBlocks() {
        final File file = new File(getDataFolder(), "customblocks.yml");
        if(!file.exists()) return;
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.getKeys(false).forEach(key -> CustomBlockListener.blocks.add((CustomBlock) cfg.get(key)));
    }


    private void setupBStats() {
        Metrics metrics = new Metrics(this, 8000);
    }

    private boolean isNullOrDefault(String configName) {
        return config.get(configName) == null || config.getDefaults().get(configName) == config.get(configName);
    }

    private void registerCrafts()
    {
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
                pm.registerEvents(clazz.getDeclaredConstructor().newInstance(), this);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                getLogger().severe(e.getMessage());
            }
        });
    }

    private void registerCommands() {
        registerCommand("cc", new ColorCommand(), "chatcolor", "colorcode");
        ReflectionUtils.getClassWithAnnotation("fr.minemobs.minemobsutils.commands", CommandInfo.class).forEach(clazz -> {
            try {
                registerCommand((fr.minemobs.minemobsutils.commands.PluginCommand) clazz.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                getLogger().severe(e.getMessage());
            }
        });
    }

    private void registerCommand(fr.minemobs.minemobsutils.commands.PluginCommand cmd) {
        PluginCommand command = getCommand(cmd.getCommandInfo().name());
        if(cmd.getCommandInfo().alias().length != 0) command.setAliases(Arrays.asList(cmd.getCommandInfo().alias()));
        command.setExecutor(cmd);
    }

    private void registerCommand(@NotNull String commandName, @NotNull CommandExecutor commandExecutor, @Nullable String... commandAliases) {
        PluginCommand command = getCommand(commandName);
        if (commandAliases != null && commandAliases.length != 0) command.setAliases(Arrays.asList(commandAliases));
        command.setExecutor(commandExecutor);
    }

    @Override
    public void onDisable() {
        try {
            saveBlocks();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void saveBlocks() throws Exception {
        final File file = new File(getDataFolder(), "customblocks.yml");
        if(file.exists()) {
            file.delete();
        } else {
            file.mkdirs();
        }
        file.createNewFile();
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        for (int i = 0; i < CustomBlockListener.blocks.size(); i++) {
            cfg.set(String.valueOf(i), CustomBlockListener.blocks.get(i));
        }
        cfg.save(file);
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(getInstance(), key);
    }

    public static MinemobsUtils getInstance() {
        return instance;
    }
}
