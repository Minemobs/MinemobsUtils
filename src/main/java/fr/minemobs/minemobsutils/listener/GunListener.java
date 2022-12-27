package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.objects.Items;
import fr.minemobs.minemobsutils.objects.item.ProjectileInfo;
import fr.sunderia.sunderiautils.utils.ItemStackUtils;
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
        if(ItemStackUtils.isAirOrNull(event.getItem()) || !ItemStackUtils.isSameItem(event.getItem(), Items.GUN.stack)) return;
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        Optional<ItemStack> ammo = Arrays.stream(inventory.getStorageContents()).filter(ItemStackUtils::isNotAirNorNull).filter(ItemStackUtils::hasLore)
                .filter(is -> {
                    Optional<Items> itemFromStack = Items.getItemFromStack(is);
                    return itemFromStack.isPresent() &&
                            Arrays.stream(Items.getAllItemsWithItemInfo(ProjectileInfo.class)).anyMatch(item -> item == itemFromStack.get() &&
                                    Arrays.stream(((ProjectileInfo) item.info).validWeapons()).anyMatch(items -> items == Items.GUN));
                })
                .findFirst();
        if(ammo.isEmpty()) {
            event.getPlayer().sendMessage(MinemobsUtils.header + ChatColor.RED + "You don't have ammo!");
            return;
        }
        Optional<Items> itemFromStack = Items.getItemFromStack(ammo.get());
        if(itemFromStack.isEmpty()) return;
        ProjectileInfo info = ProjectileInfo.getInfo(itemFromStack.get());
        World world = event.getPlayer().getWorld();
        drawLine(event.getPlayer().getEyeLocation(), event.getPlayer().getEyeLocation().clone().add(event.getPlayer().getEyeLocation().getDirection().clone().multiply(100)), 0.1D);
        RayTraceResult result = world.rayTrace(event.getPlayer().getEyeLocation(), event.getPlayer().getEyeLocation().getDirection(), 100.0D, FluidCollisionMode.NEVER,
                true, 0.2D, entity -> entity instanceof LivingEntity && !entity.equals(event.getPlayer()));
        ammo.get().setAmount(ammo.get().getAmount() - 1);
        if(result == null) return;
        if(result.getHitBlock() != null) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_STONE_HIT, 2.5F, 1.0F);
            return;
        }
        if(result.getHitEntity() == null) return;
        LivingEntity entity = (LivingEntity) result.getHitEntity();
        entity.damage(info.damage(), event.getPlayer());
        info.onHit(event.getPlayer(), ammo.get(), entity);
        if(info.isFlammable()) entity.setFireTicks(100);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
    }

    public void drawLine(Location start, Location end, double space) {
        World world = start.getWorld();
        if(world == null || end.getWorld() == null || !world.getUID().equals(end.getWorld().getUID())) return;
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
