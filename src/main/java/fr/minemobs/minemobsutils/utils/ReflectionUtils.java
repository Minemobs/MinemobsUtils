package fr.minemobs.minemobsutils.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtils {

    private ReflectionUtils() {}

    public static Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getServerVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getHandle(Player player) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return player.getClass().getMethod("getHandle").invoke(player);
    }

    public static Object getHandle(World world) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return world.getClass().getMethod("getHandle").invoke(world);
    }

    public static ServerLevel getWorldServer(World world) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return (ServerLevel) getHandle(world);
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

    public static <T> Set<Class<? extends T>> getClass(String classesPackage, Class<? extends T> subtype) {
        Reflections reflections = new Reflections(classesPackage);
        return reflections.getSubTypesOf(subtype).stream().map(clazz -> ((Class<? extends T>) clazz)).collect(Collectors.toSet());
    }

    public static <T extends Annotation> Set<Class<?>> getClassWithAnnotation(String classesPackage, Class<? extends T> annotation) {
        Reflections reflections = new Reflections(classesPackage);
        return reflections.getTypesAnnotatedWith(annotation);
    }

}