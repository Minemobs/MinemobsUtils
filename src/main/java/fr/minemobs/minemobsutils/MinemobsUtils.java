package fr.minemobs.minemobsutils;

import fr.minemobs.minemobsutils.commands.BroadcastCommand;
import fr.minemobs.minemobsutils.listener.CraftListener;
import fr.minemobs.minemobsutils.objects.CustomEnchants;
import fr.minemobs.minemobsutils.objects.Recipes;
import fr.minemobs.minemobsutils.utils.UpdateChecker;
import fr.sunderia.sunderiautils.SunderiaUtils;
import fr.sunderia.sunderiautils.customblock.CustomBlock;
import fr.sunderia.sunderiautils.listeners.CustomBlockListener;
import io.netty.util.internal.ThrowableUtil;
import org.apache.commons.io.FileUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class MinemobsUtils extends JavaPlugin {

    public static final String header = String.format("%s[%sMinemobs Utils%s]%s ", ChatColor.DARK_GRAY, ChatColor.DARK_RED, ChatColor.DARK_GRAY, ChatColor.RESET);
    public static final String pluginID = "minemobsutils";

    @Override
    public void onEnable() {
        checkUpdates();
        ConfigurationSerialization.registerClass(CustomBlock.class, "customblock");
        SunderiaUtils.of(this);
        try {
            loadCustomBlocks();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            registerListeners();
            registerCommands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        registerCrafts();
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
        cfg.getKeys(false).forEach(key -> {
            CustomBlock block = (CustomBlock) cfg.get(key);
            if(block != null) CustomBlockListener.putCustomBlock(block);
        });
    }


    private void setupBStats() {
        new Metrics(this, 8000);
    }

    private void registerCrafts() {
        for (Recipes recipe : Arrays.stream(Recipes.values()).filter(recipes -> recipes.getRecipe() != null).toList()) {
            getServer().addRecipe(recipe.getRecipe());
        }
        for (Recipes recipe : Arrays.stream(Recipes.values()).filter(recipes -> recipes.getShapelessRecipe() != null).toList()) {
            getServer().addRecipe(recipe.getShapelessRecipe());
        }
    }

    private void registerListeners() throws IOException {
        SunderiaUtils.registerListeners(CraftListener.class.getPackageName());
    }

    private void registerCommands() throws IOException {
        SunderiaUtils.registerCommands(BroadcastCommand.class.getPackageName());
    }

    @Override
    public void onDisable() {
        try {
            saveBlocks();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void saveBlocks() throws IOException {
        Path path = getDataFolder().toPath().resolve("customblocks.yml");
        if(!Files.exists(path)) Files.createFile(path);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(path.toFile());
        int i = 0;
        //for (CustomBlock block : CustomBlockListener.blocks.values()) {
        for (CustomBlock block : getCustomBlocksVariable().values()) {
            cfg.set(String.valueOf(i), block);
            i++;
        }
        cfg.save(path.toFile());
    }

    private Map<NamespacedKey, CustomBlock> getCustomBlocksVariable() {
        Class<CustomBlockListener> clazz = CustomBlockListener.class;
        try {
            Field blocks = clazz.getDeclaredField("blocks");
            blocks.setAccessible(true);
            Map<NamespacedKey, CustomBlock> map = (Map<NamespacedKey, CustomBlock>) blocks.get(null);
            blocks.setAccessible(false);
            return map;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().severe(() -> "An error occurred while trying to get the custom blocks variable.\n" + ThrowableUtil.stackTraceToString(e));
            return Collections.emptyMap();
        }
    }

    public static NamespacedKey getKey(String key) {
        return SunderiaUtils.key(key);
    }
}
