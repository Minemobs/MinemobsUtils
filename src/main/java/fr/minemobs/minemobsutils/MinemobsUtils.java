package fr.minemobs.minemobsutils;

import fr.minemobs.minemobsutils.commands.CommandColor;
import kr.entree.spigradle.annotations.SpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
    }

    private void registerCommands() {
        getCommand("cc").setExecutor(new CommandColor());
        /*getCommand("afly").setExecutor(new CommandAllowFly());
        getCommand("heal").setExecutor(new CommandHeal());
        getCommand("tpsp").setExecutor(new CommandSpawnPoint());
        getCommand("nick").setExecutor(new CommandNick());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("frl").setExecutor(new CommandFakeReload());
        getCommand("tpsg").setExecutor(new StickTeleport());
        getCommand("enchants").setExecutor(new EnchantGive());
        getCommand("craft").setExecutor(new CommandCraft());
        getCommand("enderchest").setExecutor(new EnderChestCommand());*/
    }

    @Override
    public void onDisable() {

    }

    public static MinemobsUtils getInstance() {
        return instance;
    }
}
