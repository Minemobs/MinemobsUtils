package fr.minemobs.minemobsutils.utils;

import org.bukkit.Bukkit;

public class ReflectionUtils {

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

}