package jaime.ge.gameEvent.files.gameFiles;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.model.ConfigurableAttribute;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TNTTagGameFile extends GameFile {


    private boolean explodeRandomTime;
    private int fastestExplodeTime;
    private int slowestExplodeTime;
    private String taggedSound;
    private String explosionSound;
    private ItemStack tntItem;

    public TNTTagGameFile(GameEvents plugin) {
        super(plugin, "tntTag.yml", MinigameName.TNT_TAG);
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        reloadBasics();
        FileConfiguration config = this.getFile().getConfig();

        explodeRandomTime = config.getBoolean("explode_random_time");
        fastestExplodeTime = config.getInt("fastest_explode_time");
        slowestExplodeTime = config.getInt("slowest_explode_time");
        taggedSound = config.getString("tagged_sound");
        explosionSound = config.getString("explosion_sound");
        tntItem = config.getItemStack("tnt_head_item");
    }

    @Override
    public Map<String, ConfigurableAttribute<?>> getConfigurableAttributes() {
        Map<String, ConfigurableAttribute<?>> attributesMap = new HashMap<>(this.getBaseConfigurableAttributes());
        attributesMap.put("explode_random_time", new ConfigurableAttribute<>("explode_random_time", Boolean.class, explodeRandomTime));
        attributesMap.put("fastest_explode_time", new ConfigurableAttribute<>("fastest_explode_time", Integer.class, fastestExplodeTime));
        attributesMap.put("slowest_explode_time", new ConfigurableAttribute<>("slowest_explode_time", Integer.class, slowestExplodeTime));
        attributesMap.put("tagged_sound", new ConfigurableAttribute<>("tagged_sound", String.class, taggedSound));
        attributesMap.put("explosion_sound", new ConfigurableAttribute<>("explosion_sound", String.class, explosionSound));
        attributesMap.put("tnt_head_item", new ConfigurableAttribute<>("tnt_head_item", ItemStack.class, tntItem));
        return attributesMap;
    }

}
