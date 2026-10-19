package jaime.ge.gameEvent.files.gameFiles;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.model.ConfigurableAttribute;
import jaime.ge.gameEvent.configurator.model.LocationArrayAttribute;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TNTRunGameFile extends GameFile {


    private String eliminationSound;
    private final Location[] gameZone = new Location[2];
    private final Location[] deathZone = new Location[2];

    private int blockDecayDelayInTicks;

    public TNTRunGameFile(GameEvents plugin) {
        super(plugin, "tntRun.yml", MinigameName.TNT_RUN);
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        reloadBasics();
        FileConfiguration config = this.getFile().getConfig();

        blockDecayDelayInTicks = config.getInt("blockDecayDelayInTicks");
        eliminationSound = config.getString("eliminationSound");
        ConfigurationSection gameZoneSection = config.getConfigurationSection("gameZone");
        assert gameZoneSection != null;
        getCorners(gameZone, gameZoneSection);

        ConfigurationSection deathZoneSection = config.getConfigurationSection("deathZone");
        if(deathZoneSection == null){
            deathZoneSection = config.createSection("deathZone");
            deathZoneSection.set("corner1", "empty");
            deathZoneSection.set("corner2", "empty");
        }
        getCorners(deathZone, deathZoneSection);



    }

    @Override
    public Map<String, ConfigurableAttribute<?>> getConfigurableAttributes() {
        Map<String, ConfigurableAttribute<?>> attributesMap = new HashMap<>(this.getBaseConfigurableAttributes());
        attributesMap.put("eliminationSound", new ConfigurableAttribute<>("eliminationSound", String.class, eliminationSound));
        attributesMap.put("gameZone", new LocationArrayAttribute("gameZone", gameZone));
        attributesMap.put("blockDecayDelayInTicks", new ConfigurableAttribute<>("blockDecayDelayInTicks", Integer.class, blockDecayDelayInTicks));
        attributesMap.put("deathZone", new LocationArrayAttribute("deathZone", deathZone));

        return attributesMap;
    }

}
