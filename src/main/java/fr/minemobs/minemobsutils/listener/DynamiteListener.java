package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.objects.Items;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class DynamiteListener implements Listener {

    private final List<FallingBlock> thrownedTnt = new ArrayList<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(ItemStackUtils.isAirOrNull(event.getItem())) return;
            if(!event.getItem().isSimilar(Items.DYNAMITE.stack)) return;
            FallingBlock tnt = event.getPlayer().getWorld().spawnFallingBlock(event.getPlayer().getEyeLocation(), Bukkit.createBlockData(Material.TNT));
            tnt.setDropItem(false);
            thrownedTnt.add(tnt);
            tnt.setVelocity(event.getPlayer().getLocation().getDirection().multiply(1.5));
            event.setCancelled(true);
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
    }

    @EventHandler
    public void onFall(EntityChangeBlockEvent event) {
        if(event.getEntityType() != EntityType.FALLING_BLOCK) return;
        if(!thrownedTnt.contains(event.getEntity())) return;
        event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 15, false, true, event.getEntity());
        event.setCancelled(true);
        event.getEntity().remove();
        thrownedTnt.remove(event.getEntity());
    }
}
