package fr.minemobs.minemobsutils.nms.versions.customblock;

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
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CustomBlock_1_17_R1 extends ICustomBlock {

    public CustomBlock_1_17_R1(int customModelData, List<ItemStack> drop, int xp) {
        super(customModelData, drop, xp);
    }

    public CustomBlock_1_17_R1(int customModelData, int xp, ItemStack... drop) {
        super(customModelData, xp, drop);
    }

    public CustomBlock_1_17_R1(int customModelData, List<ItemStack> drop) {
        super(customModelData, drop);
    }

    public CustomBlock_1_17_R1(int customModelData, ItemStack... drop) {
        super(customModelData, drop);
    }

    @Override
    public void setBlock(Location loc) {
        if(loc.getWorld() == null) return;
        loc.getBlock().setBlockData(Material.SPAWNER.createBlockData());
        if(!(loc.getBlock().getState() instanceof CreatureSpawner)) return;
        WorldServer server = ((CraftWorld) loc.getWorld()).getHandle();
        BlockPosition pos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        TileEntityMobSpawner spawnerTE = (TileEntityMobSpawner) server.getTileEntity(pos);
        MobSpawnerAbstract spawner = spawnerTE.getSpawner();
        NBTTagCompound spawnData = new NBTTagCompound();
        NBTTagList armorList = new NBTTagList();
        NBTTagCompound helmet = new NBTTagCompound();
        helmet.setString("id", "minecraft:command_block");
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
}
