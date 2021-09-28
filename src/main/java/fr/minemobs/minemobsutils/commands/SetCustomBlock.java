package fr.minemobs.minemobsutils.commands;

import fr.minemobs.minemobsutils.nms.versions.customblock.CustomBlock;
import fr.minemobs.minemobsutils.objects.Blocks;
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
        block.get().setBlock(player.getLocation());
        return true;
    }
}