package fr.minemobs.minemobsutils.nms.versions.customblock;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CustomBlock_1_16_R3 extends ICustomBlock {

    public CustomBlock_1_16_R3(int customModelData, List<ItemStack> drop, int xp) {
        super(customModelData, drop, xp);
    }

    public CustomBlock_1_16_R3(int customModelData, int xp, ItemStack... drop) {
        super(customModelData, xp, drop);
    }

    public CustomBlock_1_16_R3(int customModelData, List<ItemStack> drop) {
        super(customModelData, drop);
    }

    public CustomBlock_1_16_R3(int customModelData, ItemStack... drop) {
        super(customModelData, drop);
    }

    @Override
    public void setBlock(Location loc) {
        if(loc.getWorld() == null) return;
        WorldServer server = ((CraftWorld) loc.getWorld()).getHandle();
        BlockPosition pos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        server.setTypeUpdate(pos, CraftMagicNumbers.getBlock(Material.SPAWNER).getBlockData());
        loc.getBlock().setBlockData(Material.SPAWNER.createBlockData());
        if(!(loc.getBlock().getState() instanceof CreatureSpawner)) return;
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
        spawner.setSpawnData(new MobSpawnerData(1, spawnData));
        spawner.maxNearbyEntities = 0;
        spawner.requiredPlayerRange = 0;
        spawner.spawnCount = 4;
        spawner.maxSpawnDelay = 800;
        spawner.spawnDelay = 0;
        spawner.spawnRange = 4;
        spawner.minSpawnDelay = 200;
        spawnerTE.update();
    }
}
