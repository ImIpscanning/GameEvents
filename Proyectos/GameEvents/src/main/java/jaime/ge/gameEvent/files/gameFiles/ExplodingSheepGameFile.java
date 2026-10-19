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
public class ExplodingSheepGameFile extends GameFile{

    private int explodeTimer;
    private final Location[] gameZone = new Location[2];
//    private String explodeSound;


    public ExplodingSheepGameFile(GameEvents plugin) {
        super(plugin, "explodingSheep.yml", MinigameName.EXPLODING_SHEEP);
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        reloadBasics();
        FileConfiguration config = getFile().getConfig();

        explodeTimer = config.getInt("explodeTimer");

        ConfigurationSection gameZoneSection = config.getConfigurationSection("gameZone");
        assert gameZoneSection != null;
        getCorners(gameZone, gameZoneSection);
    }

    @Override
    public Map<String, ConfigurableAttribute<?>> getConfigurableAttributes() {
        Map<String, ConfigurableAttribute<?>> map = new HashMap<>(this.getBaseConfigurableAttributes());

        map.put("gameZone", new LocationArrayAttribute("gameZone", gameZone));
        map.put("explodeTimer", new ConfigurableAttribute<>("explodeTimer", Integer.class, explodeTimer));

        return map;
    }
}
