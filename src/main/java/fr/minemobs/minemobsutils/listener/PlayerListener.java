package fr.minemobs.minemobsutils.listener;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTTileEntity;
import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.commands.StaffChatCommand;
import fr.minemobs.minemobsutils.customblock.CustomBlock;
import fr.minemobs.minemobsutils.event.ArmorEvent;
import fr.minemobs.minemobsutils.event.CustomBlockBreakEvent;
import fr.minemobs.minemobsutils.event.CustomBlockInteractEvent;
import fr.minemobs.minemobsutils.event.CustomBlockPlaceEvent;
import fr.minemobs.minemobsutils.objects.Blocks;
import fr.minemobs.minemobsutils.objects.Items;
import fr.minemobs.minemobsutils.objects.Recipes;
import fr.minemobs.minemobsutils.utils.Cooldown;
import fr.minemobs.minemobsutils.utils.ItemStackUtils;
import org.bukkit.*;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
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
        if (event.getMessage().startsWith("*") && player.hasPermission(perm)) {
            StaffChatCommand.sendMessageToModerators(player.getUniqueId(), event.getMessage());
            event.setCancelled(true);
        }
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
    public void onQuit(PlayerQuitEvent event) {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction().equals(Action.PHYSICAL)) {
            if(event.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                player.setVelocity(player.getLocation().getDirection().multiply(4));
                player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
            }
        } else if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(ItemStackUtils.isAirOrNull(event.getItem())) return;
            if(event.getItem().isSimilar(Items.BATTERY.stack)) {
                event.setCancelled(true);
                Optional<ItemStack> opIs = findFirstDraconicArmorPiece(player.getInventory().getArmorContents());
                if(!opIs.isPresent()) {
                    player.sendMessage(MinemobsUtils.ebheader + "All your armors are already charged or you don't have a Draconic armor on you!");
                    return;
                }
                chargeArmor(player, opIs.get());
            } else if(event.getItem().isSimilar(Items.CRAFTING_TABLE_PORTABLE.stack)) {
                player.openWorkbench(null, true);
            } else if(event.getItem().isSimilar(Items.FIREBALL_STAFF.stack)) {
                if(!player.getInventory().contains(Material.FIRE_CHARGE)) {
                    player.sendMessage(MinemobsUtils.ebheader + ChatColor.RED + "You need at least 1 fireball to use this item !");
                    return;
                }
                Fireball fireball = player.getWorld().spawn(player.getEyeLocation(), Fireball.class);
                fireball.setYield(0);
                fireball.setCustomName("Fireball");
                fireball.setShooter(player);
                fireball.setVelocity(player.getLocation().getDirection());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (fireball.isDead()) {
                            cancel();
                            return;
                        }
                        fireball.getWorld().spawnParticle(Particle.FLAME, fireball.getLocation(), 1);
                    }
                }.runTaskTimer(MinemobsUtils.getInstance(), 0, 2);
                int slot = player.getInventory().first(Material.FIRE_CHARGE);
                player.getInventory().getItem(slot).setAmount(player.getInventory().getItem(slot).getAmount() - 1);
                event.setCancelled(true);
            }

            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(event.getClickedBlock().getType() != Material.SPAWNER) return;
                CreatureSpawner spawner = (CreatureSpawner) event.getClickedBlock().getState();
                NBTTileEntity te = new NBTTileEntity(spawner);
                NBTCompound spawnData = te.getCompound("SpawnData");
                NBTCompoundList armorItems = spawnData.getCompoundList("ArmorItems");
                int cmd = armorItems.get(3).getCompound("tag").getInteger("CustomModelData");
                Optional<CustomBlock> cb = CustomBlockListener.blocks.stream()
                        .filter(customBlock -> customBlock.getCustomModelData() == cmd && customBlock.getLoc().getX() == event.getClickedBlock().getLocation().getX() &&
                                customBlock.getLoc().getY() == event.getClickedBlock().getY() && event.getClickedBlock().getZ() == customBlock.getLoc().getZ()).findFirst();
                if (!cb.isPresent()) return;
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        Bukkit.getPluginManager().callEvent(new CustomBlockInteractEvent(player, event.getItem(), event.getClickedBlock(), event.getBlockFace(), cb.get()));
                    }
                }.runTaskLater(MinemobsUtils.getInstance(), 2);
            }
        }
    }

    @EventHandler
    public void onFireballLand(ProjectileHitEvent event) {
        if(event.getEntityType() != EntityType.FIREBALL || event.getEntity().getCustomName() == null || !event.getEntity().getCustomName().equals("Fireball")) return;
        event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 5, true, true, event.getEntity());
        for (Entity e : event.getEntity().getNearbyEntities(5, 5, 5)) {
            if(!(e instanceof LivingEntity)) return;
            LivingEntity entity = (LivingEntity) e;
            double distance = event.getEntity().getLocation().distanceSquared(entity.getLocation());
            if(distance <= .5) {
                entity.setVelocity(new Location(entity.getWorld(), 0, 1, 0).toVector());
            } else {
                entity.setVelocity(entity.getLocation().subtract(event.getEntity().getLocation()).toVector().multiply(1 / distance));
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if(event.getState() != PlayerFishEvent.State.REEL_IN ||
                !ItemStackUtils.isSimilar(event.getPlayer().getInventory().getItemInMainHand(), Items.GRAPPLING_HOOK.stack)) return;
        Player player = event.getPlayer();
        if(Cooldown.isInCooldown(player.getUniqueId(), Cooldown.CooldownType.GRAPPLING_HOOK.name) && !player.hasPermission(MinemobsUtils.pluginID + ".ignorecooldown")) {
            player.sendMessage(Cooldown.cooldownMessage(player.getUniqueId(), Cooldown.CooldownType.GRAPPLING_HOOK));
            return;
        }
        Location playerLoc = player.getLocation();
        Location hookLoc = event.getHook().getLocation();
        Location change = hookLoc.subtract(playerLoc);
        player.setVelocity(change.toVector().multiply(.3));
        if(player.hasPermission(MinemobsUtils.pluginID + ".ignorecooldown")) return;
        Cooldown c = new Cooldown(player.getUniqueId(), Cooldown.CooldownType.GRAPPLING_HOOK);
        c.start();
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getType() != Material.SPAWNER || event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
        NBTTileEntity te = new NBTTileEntity(spawner);
        NBTCompound spawnData = te.getCompound("SpawnData");
        NBTCompoundList armorItems = spawnData.getCompoundList("ArmorItems");
        int cmd = armorItems.get(3).getCompound("tag").getInteger("CustomModelData");
        Optional<CustomBlock> cb = CustomBlockListener.blocks.stream()
                .filter(customBlock -> customBlock.getCustomModelData() == cmd && customBlock.getLoc().getX() == event.getBlock().getLocation().getX() &&
                        customBlock.getLoc().getY() == event.getBlock().getY() && event.getBlock().getZ() == customBlock.getLoc().getZ()).findFirst();
        if (!cb.isPresent()) return;
        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new CustomBlockBreakEvent(event.getBlock(), event.getPlayer(), cb.get()));
            }
        }.runTaskLater(MinemobsUtils.getInstance(), 2);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomBlockPlaced(BlockPlaceEvent event) {
        if(event.getBlock().getType() != Material.LIME_GLAZED_TERRACOTTA || !event.getItemInHand().hasItemMeta() || !event.getItemInHand().getItemMeta().hasDisplayName() ||
                !event.getItemInHand().getItemMeta().hasCustomModelData()) return;
        String itemName = event.getItemInHand().getItemMeta().getDisplayName();
        if(Blocks.getAllBlockItems().stream().noneMatch(itemStack -> itemStack.getItemMeta().getDisplayName().equals(itemName))) return;
        if(Blocks.getAllBlockItems().stream()
                .noneMatch(itemStack -> itemStack.getItemMeta().getCustomModelData() == event.getItemInHand().getItemMeta().getCustomModelData())) return;
        Optional<CustomBlock> opBlock = Arrays.stream(Blocks.values())
                .filter(blocks -> blocks.block.getCustomModelData() == event.getItemInHand().getItemMeta().getCustomModelData()).map(Blocks::getBlock).findFirst();
        if(!opBlock.isPresent()) return;
        CustomBlock block = opBlock.get();
        block.setLoc(event.getBlock().getLocation());
        event.setCancelled(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                CustomBlockPlaceEvent e = new CustomBlockPlaceEvent(event.getBlockPlaced(), event.getBlockReplacedState(),
                        event.getBlockAgainst(), event.getItemInHand(), event.getPlayer(), event.canBuild(),
                        event.getHand(), block);
                if(event.getPlayer().getGameMode() == GameMode.SURVIVAL) event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
                Bukkit.getPluginManager().callEvent(e);
            }
        }.runTaskLater(MinemobsUtils.getInstance(), 2);
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
