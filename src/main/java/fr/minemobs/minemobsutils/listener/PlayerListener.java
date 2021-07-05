package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.objects.Items;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements Listener {

    private final Map<UUID, BukkitRunnable> armorRunnables = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction().equals(Action.PHYSICAL)) {
    		if(event.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                player.setVelocity(player.getLocation().getDirection().multiply(4));
                player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
            }
        }
    }

    /*@EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();
        if (p.getInventory().getBoots() != null && p.getInventory().getBoots().getItemMeta().getDisplayName().equalsIgnoreCase("Draconic boots")) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            p.setAllowFlight(true);
            return;
        }
        if (!p.hasPotionEffect(PotionEffectType.SPEED)) return;
        p.removePotionEffect(PotionEffectType.SPEED);
        if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) return;
        p.setAllowFlight(false);
    }*/

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getView().getPlayer();
        if(event.getInventory().getHolder() != player) return;
        PlayerInventory inv = player.getInventory();
        ItemStack[] playerArmor = inv.getArmorContents();

        if(isDraconicArmor(playerArmor)) {
            player.addPotionEffect(PotionEffectType.SPEED.createEffect(Integer.MAX_VALUE, 5));
            player.addPotionEffect(PotionEffectType.JUMP.createEffect(Integer.MAX_VALUE, 3));
            player.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(Integer.MAX_VALUE, 1));
            player.setAllowFlight(true);
            return;
        }
        if(player.hasPotionEffect(PotionEffectType.SPEED) && player.getPotionEffect(PotionEffectType.SPEED).getAmplifier() == 5) player.removePotionEffect(PotionEffectType.SPEED);
        if(player.hasPotionEffect(PotionEffectType.JUMP) && player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() == 3) player.removePotionEffect(PotionEffectType.JUMP);
        if(player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE) && player.getPotionEffect(PotionEffectType.FIRE_RESISTANCE).getAmplifier() == 1) player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        player.setAllowFlight(false);
    }

    private boolean isDraconicArmor(ItemStack[] armor) {
        return Arrays.stream(armor).allMatch(stack -> !ItemStackUtils.isAirOrNull(stack) && stack.getType() == Material.valueOf("DIAMOND_" + stack.getType().name().split("_")[1]) &&
                stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() && stack.getItemMeta().getDisplayName().equals(Items.valueOf("DRACONIC_" + stack.getType().name().split("_")[1]).stack.getItemMeta().getDisplayName()));
    }
}
