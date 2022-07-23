package fr.minemobs.minemobsutils.customblock;

import com.google.common.collect.ImmutableList;
import fr.minemobs.minemobsutils.MinemobsUtils;
import fr.minemobs.minemobsutils.utils.ItemBuilder;
import fr.minemobs.minemobsutils.utils.ReflectionUtils;
import fr.minemobs.minemobsutils.utils.WordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SerializableAs("customblock")
public class CustomBlock implements Cloneable, ConfigurationSerializable {

    private static final List<CustomBlock> REGISTERED_BLOCKS = new ArrayList<>();
    public static final NamespacedKey CMD_KEY = MinemobsUtils.getKey("custom_model_data");

    public static class Builder {

        private final NamespacedKey key;
        private final int cmd;

        private Material mat;
        private ItemStack[] drop;
        private int xp;
        private Location loc;

        public Builder(@NotNull NamespacedKey key, @Range(from = 1, to = Integer.MAX_VALUE) int cmd) {
            this.key = key;
            this.cmd = cmd;
        }

        public Builder setMaterial(Material mat) {
            this.mat = mat;
            return this;
        }

        public Builder setDrops(ItemStack... drop) {
            this.drop = drop;
            return this;
        }

        public Builder setXp(int xp) {
            this.xp = xp;
            return this;
        }

        public Builder setLocation(Location loc) {
            this.loc = loc;
            return this;
        }

        public CustomBlock build() {
            return new CustomBlock(key, mat, cmd, xp, loc, drop);
        }

    }

    private final NamespacedKey key;
    private final int customModelData;
    private final Material mat;
    private final ItemStack[] drop;
    private final int xp;
    private Location loc;
    private final ItemStack item;

    @SuppressWarnings("unchecked")
    public CustomBlock(Map<String, Object> serializedCustomBlock) {
        this(serializedCustomBlock.containsKey("name") ? (NamespacedKey) serializedCustomBlock.get("name") :
                (MinemobsUtils.getKey("old_block_" + (int) serializedCustomBlock.get("cmd"))),
                Material.valueOf((String) serializedCustomBlock.get("mat")),
                (int) serializedCustomBlock.get("cmd"), (int) serializedCustomBlock.get("xp"), (Location) serializedCustomBlock.get("loc"),
                ((List<ItemStack>) serializedCustomBlock.get("drops")).toArray(new ItemStack[0]));
    }

    public CustomBlock(@NotNull NamespacedKey key, @Nullable(value = "Default to Lime Glazed Terracotta") Material mat, @Range(from = 1, to = Integer.MAX_VALUE) int customModelData,
                       @Range(from = 1, to = Integer.MAX_VALUE) int xp, Location loc, ItemStack... drop) {
        this.key = Objects.requireNonNull(key, "The namespace key cannot be null");
        this.mat = mat == null ? Material.LIME_GLAZED_TERRACOTTA : mat;
        this.customModelData = customModelData;
        this.drop = drop == null ? new ItemStack[0] : drop;
        this.xp = xp;
        this.loc = loc;
        this.item = new ItemBuilder(this.mat)
                .setDisplayName(WordUtils.capitalize(key.getKey().replace('_', ' ').toLowerCase()))
                .setCustomModelData(customModelData).build();
        REGISTERED_BLOCKS.add(this);
    }

    public void setBlock(Location loc) {
        if(loc.getWorld() == null) return;
        loc.getBlock().setBlockData(Material.SPAWNER.createBlockData());
        if(!(loc.getBlock().getState() instanceof CreatureSpawner)) return;
        var craftWorld = ReflectionUtils.getCraftBukkitClass("CraftWorld").cast(loc.getWorld());
        ServerLevel server;
        try {
            server = (ServerLevel) ReflectionUtils.getCraftBukkitClass("CraftWorld").getMethod("getHandle").invoke(craftWorld);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        BlockPos pos = new BlockPos(loc.getX(), loc.getY(), loc.getZ());
        SpawnerBlockEntity spawnerTE = (SpawnerBlockEntity) server.getBlockEntity(pos);
        BaseSpawner spawner = spawnerTE.getSpawner();
        CompoundTag spawnData = new CompoundTag();
        ListTag armorList = new ListTag();
        CompoundTag helmet = new CompoundTag();
        helmet.putString("id", "minecraft:" + this.getMat().getKey().getKey());
        helmet.putByte("Count", (byte) 1);
        CompoundTag cmd = new CompoundTag();
        cmd.putInt("CustomModelData", this.getCustomModelData());
        helmet.put("tag", cmd);
        armorList.addAll(Arrays.asList(new CompoundTag(), new CompoundTag(), new CompoundTag(), helmet));
        spawnData.putString("id", "minecraft:armor_stand");
        spawnData.put("ArmorItems", armorList);
        spawnData.putByte("Marker", (byte) 1);
        spawnData.putByte("Invisible", (byte) 1);
        spawner.setNextSpawnData(server, pos, new SpawnData(spawnData, Optional.of(new SpawnData.CustomSpawnRules(new InclusiveRange<>(0, 0), new InclusiveRange<>(0, 0)))));
        spawner.maxNearbyEntities = 0;
        spawner.requiredPlayerRange = 0;
        spawnerTE.setChanged();
        this.loc = loc;
    }

    @Override
    public CustomBlock clone() {
        try {
            return (CustomBlock) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return new CustomBlock(this.key, this.mat, this.customModelData, this.xp, this.loc, this.drop);
        }
    }

    public static List<CustomBlock> getRegisteredBlocks() {
        return ImmutableList.copyOf(REGISTERED_BLOCKS);
    }

    @NotNull
    public NamespacedKey getKey() {
        return key;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public Material getMat() {
        return mat;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStack[] getDrop() {
        return drop;
    }

    public int getXp() {
        return xp;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("cmd", customModelData);
        result.put("mat", mat.toString());
        result.put("drops", drop);
        result.put("xp", xp);
        result.put("loc", loc);
        return result;
    }
}
