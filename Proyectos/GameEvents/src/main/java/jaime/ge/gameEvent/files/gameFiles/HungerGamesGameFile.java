package jaime.ge.gameEvent.files.gameFiles;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.model.ConfigurableAttribute;
import jaime.ge.gameEvent.configurator.model.LocationArrayAttribute;
import jaime.ge.gameEvent.configurator.model.MultiInventory;
import jaime.ge.gameEvent.configurator.model.MultiInventoryAttribute;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
public class HungerGamesGameFile extends GameFile{

    private final Location[] gameZone = new Location[2];
    private MultiInventory loadouts;
    private String eliminationSound;
    private Location[] spawnLocations;

    public HungerGamesGameFile(GameEvents plugin) {
        super(plugin, "hungerGames.yml", MinigameName.HUNGER_GAMES);
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        reloadBasics();
        getFile().registerConfig();
        FileConfiguration config = this.getFile().getConfig();

        eliminationSound = config.getString("eliminationSound");


        ConfigurationSection spawnLocationsSection = config.getConfigurationSection("spawnLocations");
        if(spawnLocationsSection == null) spawnLocationsSection = config.createSection("spawnLocations");
        spawnLocations = getAllLocations(spawnLocationsSection);

        ConfigurationSection gameZoneSection = config.getConfigurationSection("gameZone");
        assert gameZoneSection != null;
        getCorners(gameZone, gameZoneSection);

        ConfigurationSection loadoutsSection = config.getConfigurationSection("loadouts");
        if(loadoutsSection == null) loadoutsSection = config.createSection("loadouts");
        loadouts = MultiInventory.fromConfigurationSection(loadoutsSection);

        setMaxPlayers(spawnLocations.length);

        getFile().saveConfig();
    }

    @Override
    public Map<String, ConfigurableAttribute<?>> getConfigurableAttributes() {
        Map<String, ConfigurableAttribute<?>> map = new HashMap<>(this.getBaseConfigurableAttributes());

        map.remove("spawn_zone");
        map.remove("max_players");

        map.put("eliminationSound", new ConfigurableAttribute<>("eliminationSound", String.class, eliminationSound));
        map.put("spawnLocations", new LocationArrayAttribute("spawnLocations", spawnLocations, -1));
        map.put("gameZone", new LocationArrayAttribute("gameZone", gameZone));
        map.put("loadouts", new MultiInventoryAttribute("loadouts", loadouts));

        return map;
    }

    @Override
    public void configureGameAttributes(Map<String, ConfigurableAttribute<?>> attributeMap){
        super.configureGameAttributes(attributeMap);
        getFile().registerConfig();
        FileConfiguration config = getFile().getConfig();
        ConfigurationSection loadoutsSection = config.getConfigurationSection("loadouts");
        assert loadoutsSection != null;

        MultiInventoryAttribute multiInventoryAttribute = (MultiInventoryAttribute) attributeMap.get("loadouts");
        setLoadouts(loadoutsSection, multiInventoryAttribute);

        getFile().saveConfig();
    }

    private void setLoadouts(ConfigurationSection loadoutsSection, MultiInventoryAttribute attribute) {
        int iterator = 0;
        for(ItemStack[] content : attribute.getValue().getInventories()){
            ConfigurationSection inventorySection = loadoutsSection.createSection(iterator + "");
            ItemUtils.serializeInventoryInSection(inventorySection, content);
            iterator++;
        }
    }
}
