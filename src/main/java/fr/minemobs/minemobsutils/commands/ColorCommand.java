package fr.minemobs.minemobsutils.commands;

import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@CommandInfo(name = "colorcode", aliases = {"cc", "chatcolor"})
public class ColorCommand extends PluginCommand {

    public ColorCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        Inventory colorCode = Bukkit.getServer().createInventory(player, 27, "Color Code");
        ChatColor[] othersColorCode = new ChatColor[]{ChatColor.RESET, ChatColor.ITALIC, ChatColor.UNDERLINE, ChatColor.STRIKETHROUGH, ChatColor.BOLD, ChatColor.MAGIC};
        for (ChatColor value : ChatColor.values()) {
            String name = value.name();
            if(name.endsWith("AQUA")) {
                name = name.replace("AQUA", "CYAN");
            }
            if(name.startsWith("DARK")) {
                colorCode.addItem(new ItemBuilder(Material.valueOf(name.replace("_", "").replace("DARK", "") + "_CONCRETE"))
                        .setGlow().setLore("&" + value.getChar()).setDisplayName(StringUtils.capitalize(name.toLowerCase().replace('_', ' '))).build());
            } else if(name.equalsIgnoreCase(ChatColor.LIGHT_PURPLE.name())) {
                colorCode.addItem(new ItemBuilder(Material.PURPLE_WOOL).setGlow().setLore("&" + value.getChar()).setDisplayName("Light purple").build());
            } else {
                String finalName = name;
                if(Arrays.stream(othersColorCode).anyMatch(chatColor -> chatColor.name().equalsIgnoreCase(finalName))) {
                    colorCode.addItem(new ItemBuilder(Material.COMPASS).setGlow().setLore("&" + value.getChar()).setDisplayName(StringUtils.capitalize(name.toLowerCase())).build());
                } else {
                    if(name.equalsIgnoreCase("gold")) {
                        colorCode.addItem(new ItemBuilder(Material.GOLD_BLOCK).setGlow().setLore("&" + value.getChar()).setDisplayName(StringUtils.capitalize(name.toLowerCase())).build());
                    } else {
                        colorCode.addItem(new ItemBuilder(Material.valueOf(name + "_WOOL")).setGlow().setLore("&" + value.getChar())
                                .setDisplayName(StringUtils.capitalize(name.toLowerCase())).build());
                    }
                }
            }
        }
        player.openInventory(colorCode);
    }
}
