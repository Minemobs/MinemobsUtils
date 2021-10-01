package fr.minemobs.minemobsutils.utils;

import fr.minemobs.minemobsutils.MinemobsUtils;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(MinemobsUtils.getInstance(), () -> {
            try(InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=80802").openStream(); Scanner sc = new Scanner(is)) {
                if(sc.hasNext()) {
                    consumer.accept(sc.next());
                }
            } catch (IOException e) {
                MinemobsUtils.getInstance().getLogger().info("Cannot look for updates: " + e.getMessage());
            }
        });
    }
}
