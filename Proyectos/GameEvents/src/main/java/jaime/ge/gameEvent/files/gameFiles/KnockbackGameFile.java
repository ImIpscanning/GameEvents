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
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KnockbackGameFile extends GameFile{

    private final Location[] deathZone = new Location[2];
    private ItemStack knockItem;
    private String deathSound;
    private String killSound;


    public KnockbackGameFile(GameEvents plugin) {
        super(plugin, "knockback.yml", MinigameName.KNOCKBACK);
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        reloadBasics();
        FileConfiguration config = getFile().getConfig();

        ConfigurationSection deathZoneSection = config.getConfigurationSection("deathZone");
        assert deathZoneSection != null;
        getCorners(deathZone, deathZoneSection);

        knockItem = config.getItemStack("knockItem");
        deathSound = config.getString("deathSound");
        killSound = config.getString("killSound");
    }

    @Override
    public Map<String, ConfigurableAttribute<?>> getConfigurableAttributes() {
        Map<String, ConfigurableAttribute<?>> attributesMap = new HashMap<>(this.getBaseConfigurableAttributes());
        attributesMap.put("deathSound", new ConfigurableAttribute<>("deathSound", String.class, deathSound));
        attributesMap.put("killSound", new ConfigurableAttribute<>("killSound", String.class, killSound));
        attributesMap.put("knockItem", new ConfigurableAttribute<>("knockItem", ItemStack.class, knockItem));
        attributesMap.put("deathZone", new LocationArrayAttribute("deathZone", deathZone));

        return attributesMap;
    }
}
