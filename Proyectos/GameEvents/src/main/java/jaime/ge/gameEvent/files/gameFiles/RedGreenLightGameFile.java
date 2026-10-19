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

import java.util.*;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

@Getter
public class RedGreenLightGameFile extends GameFile {


    private int fastestTime;
    private int slowestTime;

    private final Location[] finishLine = new Location[2];

    private boolean pvp;
    private int forgivingTimeInTicks;
    private String redLightSound;
    private String greenLightSound;

    public RedGreenLightGameFile(GameEvents plugin) {
        super(plugin, "redGreenLight.yml", MinigameName.RED_GREEN_LIGHT);
        reloadConfig();

    }

    @Override
    public void reloadConfig() {
        reloadBasics();
        FileConfiguration config = this.getFile().getConfig();

        fastestTime = config.getInt("fastestTime");
        slowestTime = config.getInt("slowestTime");

        ConfigurationSection finishLineSection = config.getConfigurationSection("finish_line");
        assert finishLineSection != null;
        getCorners(finishLine, finishLineSection);


        forgivingTimeInTicks = config.getInt("forgiving_time_in_ticks");
        redLightSound = config.getString("red_light_sound");
        greenLightSound = config.getString("green_light_sound");

    }

    @Override
    public Map<String, ConfigurableAttribute<?>> getConfigurableAttributes() {
        Map<String, ConfigurableAttribute<?>> attributesMap = new HashMap<>(this.getBaseConfigurableAttributes());

        attributesMap.put("finish_line", new LocationArrayAttribute("finish_line", finishLine));
        attributesMap.put("pvp", new ConfigurableAttribute<>("pvp", Boolean.class, pvp));
        attributesMap.put("forgiving_time_in_ticks", new ConfigurableAttribute<>("forgiving_time_in_ticks", Integer.class, forgivingTimeInTicks));
        attributesMap.put("slowestTime", new ConfigurableAttribute<>("slowestTime", Integer.class, slowestTime));
        attributesMap.put("fastestTime", new ConfigurableAttribute<>("fastestTime", Integer.class, fastestTime));
        attributesMap.put("red_light_sound", new ConfigurableAttribute<>("red_light_sound", String.class, redLightSound));
        attributesMap.put("green_light_sound", new ConfigurableAttribute<>("green_light_sound", String.class, greenLightSound));

        return attributesMap;
    }



}
