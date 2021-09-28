package fr.minemobs.minemobsutils.utils;

import java.util.concurrent.TimeUnit;

public class TickUtils {

    private TickUtils() {}

    public static long toUnit(long ticks, TimeUnit unit) {
        return unit.convert(ticks * (1000 / 20), TimeUnit.MILLISECONDS);
    }

    public static long toTicks(int time, TimeUnit unit) {
        return unit.toSeconds(time) * 20;
    }

}
