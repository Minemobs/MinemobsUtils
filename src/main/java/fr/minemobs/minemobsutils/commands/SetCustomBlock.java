package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.objects.Blocks;
import fr.minemobs.minemobsutils.objects.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class SetCustomBlock implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        if(args.length == 0) return false;
        Optional<CustomBlock> block = Arrays.stream(Blocks.values()).filter(blocks -> blocks.name().equalsIgnoreCase(args[0])).map(Blocks::getBlock).findFirst();
        if(!block.isPresent()) return false;
        Player player = (Player) sender;
        setBlock(player.getLocation(), block.get());
        return true;
    }

    /**
     * Temporary function
     */
    public static void setBlock(Location loc, CustomBlock block) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("setblock %d %d %d minecraft:spawner{MaxNearbyEntities:0,RequiredPlayerRange:0," +
                "SpawnData:{id:\"minecraft:armor_stand\"," +
                "Marker:1b,Invisible:1b," +
                "ArmorItems:[{},{},{},{id:\"minecraft:command_block\",Count:1b,tag:{CustomModelData:%d}}]}}", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
                block.getCustomModelData()));
    }

    public static void setBlock(Location loc, Blocks block) {
        setBlock(loc, block.getBlock());
    }
}