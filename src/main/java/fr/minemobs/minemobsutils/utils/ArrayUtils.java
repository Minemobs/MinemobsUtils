package fr.minemobs.minemobsutils.utils;

import com.google.common.base.Joiner;
import org.jetbrains.annotations.NotNull;

public class ArrayUtils {

    public static String toString(@NotNull String[] array) {
        return Joiner.on(", ").skipNulls().join(array).replaceAll(", ", " ");
    }

}
