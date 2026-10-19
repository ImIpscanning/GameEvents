package jaime.ge.gameEvent.files.gameFiles;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.model.ConfigurableAttribute;
import jaime.ge.gameEvent.configurator.model.LocationArrayAttribute;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MusicalChairsGameFile extends GameFile{

    public MusicalChairsGameFile(GameEvents plugin) {
        super(plugin, "musicalChairs.yml", MinigameName.MUSICAL_CHAIRS);
        reloadConfig();
    }

    private int fastestTime;
    private int slowestTime;

    private String eliminationSound;
    private String music;

    private final Location[] chairSpawnZone = new Location[2];


    @Override
    public void reloadConfig() {
        reloadBasics();
        FileConfiguration config = getFile().getConfig();

        fastestTime = config.getInt("fastestTime");
        slowestTime = config.getInt("slowestTime");

        eliminationSound = config.getString("eliminationSound");
        music = config.getString("music");

        ConfigurationSection chairSpawnZoneSection = config.getConfigurationSection("chairSpawnZone");
        assert chairSpawnZoneSection != null;
        getCorners(chairSpawnZone, chairSpawnZoneSection);

    }

    @Override
    public Map<String, ConfigurableAttribute<?>> getConfigurableAttributes() {
        Map<String, ConfigurableAttribute<?>> attributesMap = new HashMap<>(this.getBaseConfigurableAttributes());

        attributesMap.put("chairSpawnZone", new LocationArrayAttribute("chairSpawnZone", chairSpawnZone));
        attributesMap.put("fastestTime", new ConfigurableAttribute<>("fastestTime", Integer.class, fastestTime));
        attributesMap.put("slowestTime", new ConfigurableAttribute<>("slowestTime", Integer.class, slowestTime));
        attributesMap.put("eliminationSound", new ConfigurableAttribute<>("eliminationSound", String.class, eliminationSound));
        attributesMap.put("music", new ConfigurableAttribute<>("music", String.class, music));

        return attributesMap;
    }
}
