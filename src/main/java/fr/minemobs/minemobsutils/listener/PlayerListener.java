package fr.minemobs.minemobsutils.listener;

import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.commands.StaffChatCommand;
import fr.minemobs.minemobsutils.event.ArmorEvent;
import fr.minemobs.minemobsutils.objects.Items;
import fr.minemobs.minemobsutils.objects.Recipes;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerListener implements Listener {

    /**
     * Staff Chat event
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String perm = MinemobsUtils.pluginID + ".mod.chat";
        Player player = event.getPlayer();
        if(!event.getMessage().startsWith("*")) return;
        if(!player.hasPermission(perm)) return;
        StaffChatCommand.sendMessageToModerators(player.getUniqueId(), event.getMessage());
        event.setCancelled(true);
    }

    /**
     * Give all recipes registered in {@link fr.minemobs.minemobsutils.objects.Recipes}
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (Recipes recipe : Arrays.stream(Recipes.values()).filter(recipes -> recipes.getRecipe() != null).collect(Collectors.toList())) {
            event.getPlayer().discoverRecipe(recipe.getRecipe().getKey());
        }
        for (Recipes recipe : Arrays.stream(Recipes.values()).filter(recipes -> recipes.getShapelessRecipe() != null).collect(Collectors.toList())) {
            event.getPlayer().discoverRecipe(recipe.getShapelessRecipe().getKey());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction().equals(Action.PHYSICAL)) {
            if(event.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                player.setVelocity(player.getLocation().getDirection().multiply(4));
                player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
            }
        } else {
            if(ItemStackUtils.isSimilar(event.getItem(), Items.BATTERY.stack)) {
                Optional<ItemStack> opIs = findFirstDraconicArmorPiece(player.getInventory().getArmorContents());
                if(!opIs.isPresent()) {
                    player.sendMessage(MinemobsUtils.ebheader + "All your armors are already charged or you don't have a Draconic armor on you!");
                    return;
                }
                chargeArmor(player, opIs.get());
            }
        }
    }

    @EventHandler
    public void onDamagedBySomething(EntityDamageEvent event) {
        if(event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if(!isDraconicArmor(player.getInventory().getArmorContents())) return;
        List<ItemStack> armorPieces = getAllDraconicArmorPieces(player.getInventory().getArmorContents());
        double armorDmg = event.getDamage() / (armorPieces.size() / 2.0D);
        boolean cancelled = true;
        for (ItemStack armorPiece : armorPieces) {
            EquipmentSlot slot = getEquipmentSlot(armorPiece.getType());
            ItemMeta meta = armorPiece.getItemMeta();
            List<String> lore = meta.getLore();
            int armorEnergy = Integer.parseInt(lore.get(1).replace("Energy: ", "").replace(" / 100", ""));
            if(armorEnergy == 0) {
                cancelled = false;
                return;
            }
            int newArmorEnergy = armorEnergy - (int) armorDmg;
            if(newArmorEnergy <= 0) newArmorEnergy = 0;
            lore.set(1, "Energy: " + newArmorEnergy + " / 100");
            meta.setLore(lore);
            armorPiece.setItemMeta(meta);
            player.getInventory().setItem(slot, armorPiece);
        }
        event.setCancelled(cancelled);
        Bukkit.getPluginManager().callEvent(new ArmorEvent(player));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getView().getPlayer();
        if(event.getInventory().getHolder() != player) return;
        Bukkit.getPluginManager().callEvent(new ArmorEvent(player));
    }

    @EventHandler
    public void onDraconicArmorEvent(ArmorEvent event) {
        Player player = event.getPlayer();
        if(isDraconicArmor(event.getPlayerArmor()) && isAllPiecesCharged(event.getPlayerArmor())) {
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

    public static boolean isDraconicArmor(ItemStack[] armor) {
        return Arrays.stream(armor).allMatch(stack -> !ItemStackUtils.isAirOrNull(stack) &&
                stack.getType().name().startsWith("LEATHER_") &&
                stack.getType() == Material.valueOf("LEATHER_" + stack.getType().name().split("_")[1]) &&
                stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() &&
                stack.getItemMeta().getDisplayName().equals(Items.valueOf("DRACONIC_" + stack.getType().name().split("_")[1]).stack.getItemMeta().getDisplayName()));
    }

    private List<ItemStack> getAllDraconicArmorPieces(ItemStack[] armor) {
        return Arrays.stream(armor).filter(stack -> !ItemStackUtils.isAirOrNull(stack) && stack.getType() == Material.valueOf("LEATHER_" + stack.getType().name().split("_")[1]) &&
                stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() &&
                stack.getItemMeta().getDisplayName().equals(Items.valueOf("DRACONIC_" + stack.getType().name().split("_")[1]).stack.getItemMeta().getDisplayName()) &&
                stack.getItemMeta().hasLore() && stack.getItemMeta().getLore().get(1) != null).collect(Collectors.toList());
    }

    private boolean isAllPiecesCharged(ItemStack[] armor) {
        return Arrays.stream(armor).allMatch(stack ->
                Integer.parseInt(stack.getItemMeta().getLore().get(1).replace("Energy: ", "").replace(" / 100", "")) != 0);
    }

    public static void chargeArmor(Player player, ItemStack is) {
        chargeArmor(player, is, 100);
    }

    public static void chargeArmor(Player player, ItemStack is, int amount) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.getLore();
        lore.set(1, "Energy: " + amount + " / 100");
        im.setLore(lore);
        is.setItemMeta(im);
        player.getInventory().setItem(getEquipmentSlot(is.getType()), is);
        player.getInventory().removeItem(Items.BATTERY.stack);
        player.sendMessage(MinemobsUtils.ebheader + "Your " + im.getDisplayName() + ChatColor.RESET + " is charged!");
        Bukkit.getPluginManager().callEvent(new ArmorEvent(player));
    }

    public static Optional<ItemStack> findFirstDraconicArmorPiece(ItemStack[] armor) {
        return Arrays.stream(armor).filter(stack -> !ItemStackUtils.isAirOrNull(stack) && stack.getType() == Material.valueOf("LEATHER_" + stack.getType().name().split("_")[1]) &&
                stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() &&
                stack.getItemMeta().getDisplayName().equals(Items.valueOf("DRACONIC_" + stack.getType().name().split("_")[1]).stack.getItemMeta().getDisplayName()) &&
                stack.getItemMeta().hasLore() && stack.getItemMeta().getLore().get(1) != null &&
                Integer.parseInt(stack.getItemMeta().getLore().get(1).replace("Energy: ", "").replace(" / 100", "")) != 100).findFirst();
    }

    public static EquipmentSlot getEquipmentSlot(Material mat) {
        String matName = mat.name().replace("LEATHER_", "");
        switch (matName) {
            case "HELMET": return EquipmentSlot.HEAD;
            case "CHESTPLATE": return EquipmentSlot.CHEST;
            case "LEGGINGS": return EquipmentSlot.LEGS;
            case "BOOTS": return EquipmentSlot.FEET;
            default: return EquipmentSlot.OFF_HAND;
        }
    }
}
