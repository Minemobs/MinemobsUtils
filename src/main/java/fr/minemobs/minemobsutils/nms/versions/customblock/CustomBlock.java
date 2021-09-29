package fr.minemobs.minemobsutils.nms.versions.customblock;

import fr.minemobs.minemobsutils.utils.ReflectionUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.MobSpawnerAbstract;
import net.minecraft.world.level.MobSpawnerData;
import net.minecraft.world.level.block.entity.TileEntityMobSpawner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("customblock")
public class CustomBlock implements Cloneable, ConfigurationSerializable {

    private final int customModelData;
    private final Material mat;
    private final ItemStack[] drop;
    private final int xp;
    private Location loc;

    public CustomBlock(Map<String, Object> serializedCustomBlock) {
        this.customModelData = (int) serializedCustomBlock.get("cmd");
        this.mat = Material.valueOf((String) serializedCustomBlock.get("mat"));
        this.drop = ((List<ItemStack>) serializedCustomBlock.get("drops")).toArray(new ItemStack[0]);
        this.xp = (int) serializedCustomBlock.get("xp");
        this.loc = (Location) serializedCustomBlock.get("loc");
    }

    public CustomBlock(Material mat, int customModelData, int xp, Location loc, ItemStack... drop) {
        this.mat = mat;
        this.customModelData = customModelData;
        this.drop = drop;
        this.xp = xp;
        this.loc = loc;
    }

    public CustomBlock(Material mat, int customModelData, List<ItemStack> drop, int xp) {
        this(mat, customModelData, xp, null, drop.toArray(new ItemStack[0]));
    }

    public CustomBlock(Material mat, int customModelData, int xp, ItemStack... drop) {
        this(mat, customModelData, Arrays.asList(drop), xp);
    }

    public CustomBlock(int customModelData, int xp, List<ItemStack> drop) {
        this(Material.LIME_GLAZED_TERRACOTTA, customModelData, drop, xp);
    }

    public CustomBlock(int customModelData, int xp, ItemStack... drop) {
        this(Material.LIME_GLAZED_TERRACOTTA, customModelData, Arrays.asList(drop), xp);
    }

    public CustomBlock(int customModelData, List<ItemStack> drop) {
        this(customModelData, 0, drop);
    }

    public CustomBlock(int customModelData, ItemStack... drop) {
        this(customModelData, 0, drop);
    }

    public void setBlock(Location loc) {
        if(loc.getWorld() == null) return;
        loc.getBlock().setBlockData(Material.SPAWNER.createBlockData());
        if(!(loc.getBlock().getState() instanceof CreatureSpawner)) return;
        var craftWorld = ReflectionUtils.getCraftBukkitClass("CraftWorld").cast(loc.getWorld());
        WorldServer server;
        try {
            server = (WorldServer) ReflectionUtils.getCraftBukkitClass("CraftWorld").getMethod("getHandle").invoke(craftWorld);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        BlockPosition pos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        TileEntityMobSpawner spawnerTE = (TileEntityMobSpawner) server.getTileEntity(pos);
        MobSpawnerAbstract spawner = spawnerTE.getSpawner();
        NBTTagCompound spawnData = new NBTTagCompound();
        NBTTagList armorList = new NBTTagList();
        NBTTagCompound helmet = new NBTTagCompound();
        helmet.setString("id", "minecraft:" + this.getMat().getKey().getKey());
        helmet.setByte("Count", (byte) 1);
        NBTTagCompound customModelData = new NBTTagCompound();
        customModelData.setInt("CustomModelData", this.getCustomModelData());
        helmet.set("tag", customModelData);
        armorList.addAll(Arrays.asList(new NBTTagCompound(), new NBTTagCompound(), new NBTTagCompound(), helmet));
        spawnData.setString("id", "minecraft:armor_stand");
        spawnData.set("ArmorItems", armorList);
        spawnData.setByte("Marker", (byte) 1);
        spawnData.setByte("Invisible", (byte) 1);
        spawner.setSpawnData(server, pos, new MobSpawnerData(1, spawnData));
        spawner.m = 0;
        spawner.n = 0;
        spawner.i = 200;
        spawner.j = 800;
        spawner.k = 4;
        spawner.o = 4;
        spawnerTE.update();
    }

    @Override
    public CustomBlock clone() {
        try {
            return (CustomBlock) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public Material getMat() {
        return mat;
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
