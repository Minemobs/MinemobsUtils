package fr.minemobs.minemobsutils.utils;

import org.bukkit.Bukkit;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getServerVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static <T> Set<Class<? extends T>> getClass(String _package, Class<? extends T> subtype) {
        Reflections reflections = new Reflections(_package);
        return reflections.getSubTypesOf(subtype).stream().map(clazz -> ((Class<? extends T>) clazz)).collect(Collectors.toSet());
    }
}