package fr.minemobs.minemobsutils.utils.helper;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.WorldServer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class NMSHelper {

    public static Object getHandle(Player player) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return player.getClass().getMethod("getHandle").invoke(player);
    }

    public static Object getHandle(World world) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return world.getClass().getMethod("getHandle").invoke(world);
    }

    public static WorldServer getWorldServer(World world) {
        return (WorldServer) world;
    }

    public static GameProfile getGameProfile(Player player) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object handle = getHandle(player);
        return (GameProfile) handle.getClass().getSuperclass().getDeclaredMethod("getProfile").invoke(handle);
    }

    @Nullable
    public static Property getTexturesProperty(GameProfile profile) {
        Optional<Property> textureProperty = profile.getProperties().get("textures").stream().findFirst();
        return textureProperty.orElse(null);
    }

}
