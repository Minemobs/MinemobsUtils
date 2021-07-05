package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import fr.minemobs.minemobsutils.objects.Items;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class GrapplingHookListener implements Listener {

    @EventHandler
    public void grappleEvent(EntityShootBowEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Arrow arrow = (Arrow) event.getProjectile();

            ItemStack grapplingHook = Items.GRAPPLING_HOOK.stack;

            new BukkitRunnable() {

                @Override
                public void run() {
                    if(arrow.getLocation().distance(player.getLocation()) > 50) {
                        arrow.remove();
                        this.cancel();
                    }

                    if(hasItemInHand(player, Material.BOW) && ItemStackUtils.isSimilar(player.getInventory().getItemInMainHand(), grapplingHook) ||
                            ItemStackUtils.isSimilar(player.getInventory().getItemInOffHand(), grapplingHook)) {

                        if(player.isSneaking()) {
                            player.setVelocity(player.getLocation().getDirection().multiply(2D));
                            arrow.remove();
                            this.cancel();
                        }

                        if(arrow.isOnGround() && !arrow.isDead()) {
                            Vector direction = arrow.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                            player.setVelocity(direction.multiply(1.2D));
                            DrawLine(player.getLocation(), arrow.getLocation(),2);
                            if(player.getLocation().distance(arrow.getLocation()) <= 3) {
                                this.cancel();
                                arrow.remove();
                            }
                        }
                    }else if (!hasItemInHand(player, Material.BOW) || !ItemStackUtils.isSimilar(player.getInventory().getItemInMainHand(), grapplingHook) ||
                            !ItemStackUtils.isSimilar(player.getInventory().getItemInOffHand(), grapplingHook)) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(MinemobsUtils.getInstance(), 0, 0);
        }
    }

    private boolean hasItemInHand(Player player, Material mat) {
        return player.getInventory().getItemInMainHand().getType().equals(mat) || player.getInventory().getItemInOffHand().getType().equals(mat);
    }

    private void DrawLine(Location player, Location arrow, double space) {
        World world = player.getWorld();
        Validate.isTrue(arrow.getWorld().equals(world), "Les Particles ne peuvent pas être dessinées dans différents mondes");

        double distance = player.distance(arrow);

        Vector p1 = player.toVector();
        Vector p2 = arrow.toVector();

        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);

        double covered = 0;

        for(;covered < distance; p1.add(vector)) {
            covered += space;
        }
    }
}
