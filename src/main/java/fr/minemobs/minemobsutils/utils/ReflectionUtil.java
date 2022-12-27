package fr.minemobs.minemobsutils.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class ReflectionUtil {

    private ReflectionUtil() {}

    public static Object getHandle(Player player) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return player.getClass().getMethod("getHandle").invoke(player);
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

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

}