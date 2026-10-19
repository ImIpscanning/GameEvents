package jaime.ge.gameEvent.files.gameFiles;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.model.ConfigurableAttribute;
import jaime.ge.gameEvent.configurator.model.LocationArrayAttribute;
import jaime.ge.gameEvent.configurator.model.MultiInventory;
import jaime.ge.gameEvent.configurator.model.MultiInventoryAttribute;
import jaime.ge.gameEvent.files.YamlFile;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public class GunGameGameFile extends GameFile {


    private String deathSound;
    private String killSound;
    private Location[] respawnLocations;
    private MultiInventory multiInventory;


    public GunGameGameFile(GameEvents plugin) {
        super(plugin, "gunGame.yml", MinigameName.GUN_GAME);
        reloadConfig();

    }

    @Override
    public void reloadConfig() {
        reloadBasics();

        FileConfiguration config = this.getFile().getConfig();

        deathSound = config.getString("deathSound");
        killSound = config.getString("killSound");

        ConfigurationSection respawnLocationsSection = config.getConfigurationSection("respawnLocations");
        assert respawnLocationsSection != null;
        respawnLocations = getAllLocations(respawnLocationsSection);

        ConfigurationSection levelsSection = config.getConfigurationSection("levels");
        if(levelsSection == null) levelsSection = config.createSection("levels");
        multiInventory = MultiInventory.fromConfigurationSection(levelsSection);



    }

    @Override
    public Map<String, ConfigurableAttribute<?>> getConfigurableAttributes() {
        Map<String, ConfigurableAttribute<?>> attributesMap = new HashMap<>(this.getBaseConfigurableAttributes());
        attributesMap.put("deathSound", new ConfigurableAttribute<>("deathSound", String.class, deathSound));
        attributesMap.put("killSound", new ConfigurableAttribute<>("killSound", String.class, deathSound));
        attributesMap.put("respawnLocations", new LocationArrayAttribute("respawnLocations", respawnLocations, -1));
        attributesMap.put("levels", new MultiInventoryAttribute("levels", multiInventory));
        return attributesMap;
    }

    @Override
    public void configureGameAttributes(Map<String, ConfigurableAttribute<?>> attributeMap){
        super.configureGameAttributes(attributeMap);
        getFile().registerConfig();
        FileConfiguration config = getFile().getConfig();
        ConfigurationSection levelsSection = config.getConfigurationSection("levels");
        assert levelsSection != null;

        MultiInventoryAttribute multiInventoryAttribute = (MultiInventoryAttribute) attributeMap.get("levels");
        setLevels(levelsSection, multiInventoryAttribute);

        getFile().saveConfig();
    }

    private void setLevels(ConfigurationSection levelsSection, MultiInventoryAttribute attribute) {
        int iterator = 0;
        for(ItemStack[] content : attribute.getValue().getInventories()){
            ConfigurationSection inventorySection = levelsSection.createSection(iterator + "");
            ItemUtils.serializeInventoryInSection(inventorySection, content);
            iterator++;
        }
    }

}
