package fr.minemobs.minemobsutils.utils;

import fr.sunderia.sunderiautils.SunderiaUtils;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(SunderiaUtils.getPlugin(), () -> {
            try(InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=80802").openStream(); Scanner sc = new Scanner(is)) {
                if(sc.hasNext()) {
                    consumer.accept(sc.next());
                }
            } catch (IOException e) {
                SunderiaUtils.getPlugin().getLogger().info("Cannot look for updates: " + e.getMessage());
            }
        });
    }
}
