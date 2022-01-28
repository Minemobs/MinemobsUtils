package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.objects.Items;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Optional;

public class GunListener implements Listener {

    @EventHandler
    public void onGunUsed(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if(ItemStackUtils.isAirOrNull(event.getItem()) || !event.getItem().isSimilar(Items.GUN.stack)) return;
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        //TODO: Add support for custom projectiles
        Optional<ItemStack> ammo = Arrays.stream(inventory.getStorageContents()).filter(ItemStackUtils::isNotAirNorNull).filter(ItemStackUtils::hasLore)
                .filter(is -> ItemStackUtils.isSameItem(is, Items.GUN_AMMO.stack) || ItemStackUtils.isSameItem(is, Items.FIRE_GUN_AMMO.stack)).findFirst();
        if(ammo.isEmpty()) {
            event.getPlayer().sendMessage(MinemobsUtils.ebheader + ChatColor.RED + "You don't have ammo!");
            return;
        }
        World world = event.getPlayer().getWorld();
        drawLine(event.getPlayer().getEyeLocation(), event.getPlayer().getEyeLocation().clone().add(event.getPlayer().getEyeLocation().getDirection().clone().multiply(100)), 0.1D);
        RayTraceResult result = world.rayTrace(event.getPlayer().getEyeLocation(), event.getPlayer().getEyeLocation().getDirection(), 100.0D, FluidCollisionMode.NEVER,
                true, 0.2D, entity -> entity instanceof LivingEntity && !entity.equals(event.getPlayer()));
        boolean useFireAmmo = ItemStackUtils.isSameItem(ammo.get(), Items.FIRE_GUN_AMMO.stack);
        ammo.get().setAmount(ammo.get().getAmount() - 1);
        if(result == null) return;
        if(result.getHitBlock() != null) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_STONE_HIT, 2.5F, 1.0F);
            return;
        }
        if(result.getHitEntity() == null) return;
        LivingEntity entity = (LivingEntity) result.getHitEntity();
        entity.damage(5.0D, event.getPlayer());
        if(useFireAmmo) entity.setFireTicks(100);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
    }

    public void drawLine(Location start, Location end, double space) {
        World world = start.getWorld();
        if(!world.equals(end.getWorld())) return;
        double distance = start.distance(end);
        Vector startVec = start.toVector();
        Vector endVec = end.toVector();
        Vector vec = endVec.clone().subtract(startVec).normalize().multiply(space);
        double covered = 0.0D;
        for(; covered < distance; startVec.add(vec)) {
            if(world.getBlockAt(startVec.toLocation(world)).getType().isSolid()) break;
            world.spawnParticle(Particle.SPELL_WITCH, startVec.getX(), startVec.getY(), startVec.getZ(), 1);
            covered += space;
        }
    }
}
