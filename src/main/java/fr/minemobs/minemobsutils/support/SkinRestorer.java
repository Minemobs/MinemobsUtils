package fr.minemobs.minemobsutils.support;

import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import org.bukkit.entity.Player;

public class SkinRestorer {

    private SkinRestorer() {}

    public static void setSkin(Player player) {
        SkinsRestorerAPI api = SkinsRestorerAPI.getApi();
        try {
            api.setSkin(player.getName(), player.getDisplayName());
            api.applySkin(new PlayerWrapper(player));
        } catch (SkinRequestException ignored) {
            //Skin do not exist
        }
    }

}
