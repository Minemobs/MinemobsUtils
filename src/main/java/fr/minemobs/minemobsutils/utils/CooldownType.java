package fr.minemobs.minemobsutils.utils;

public enum CooldownType {
    WITHER_STAFF("wither_staff", 15),
    HEAL_FEED_COMMAND("status", 15),
    GRAPPLING_HOOK("grappling_hook", 2),
    ;

    private final String name;
    private final int cooldownInSeconds;

    CooldownType(String name, int cooldownInSeconds) {
        this.name = name;
        this.cooldownInSeconds = cooldownInSeconds;
    }

    public String getName() {
        return name;
    }

    public int getCooldownInSeconds() {
        return cooldownInSeconds;
    }
}
