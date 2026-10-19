package jaime.ge.gameEvent.files;

import jaime.ge.gameEvent.GameEvents;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class ConfigFile {
    private final YamlFile file;

    private String locationWandName;
    private int secondsBetweenGames;
    private int maxWaitTimeForGames;
    private boolean debug;


    public ConfigFile(GameEvents plugin){
        file = new YamlFile("config.yml", null, plugin, false);
        reloadConfig();
    }

    private void reloadConfig() {
        file.registerConfig();
        FileConfiguration config = file.getConfig();

        secondsBetweenGames = config.getInt("game_rotation_time");
        maxWaitTimeForGames = config.getInt("game_max_wait_time");

        locationWandName = config.getString("location_wand_name");
        debug = config.getBoolean("debug");
    }
}
