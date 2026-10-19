package jaime.ge.gameEvent.files.gameFiles;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.model.ConfigurableAttribute;
import jaime.ge.gameEvent.configurator.model.LocationArrayAttribute;
import jaime.ge.gameEvent.configurator.model.MultiInventory;
import jaime.ge.gameEvent.configurator.model.StringListAttribute;
import jaime.ge.gameEvent.files.YamlFile;
import jaime.ge.gameEvent.games._model.Reward;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.enums.RewardType;
import jaime.ge.gameEvent.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

@Getter
public abstract class GameFile {

    private final YamlFile file;

    private int playtime;
    private int warmup;
    private String fancyName;
    private boolean enabled;
    private int minPlayers;
    private int maxPlayers;
    private final MinigameName minigameName;
    private final Location[] spawnZone = new Location[2];
    private final List<Reward> rewards;


    public GameFile(GameEvents plugin, String fileName, MinigameName minigameName){
        file = new YamlFile(fileName, "games", plugin, false);
        this.minigameName = minigameName;
        rewards = new ArrayList<>();
    }

    protected final void reloadBasics() {
        file.registerConfig();
        FileConfiguration config = file.getConfig();

        playtime = config.getInt("playtime");
        warmup = config.getInt("warmup");
        fancyName = config.getString("fancy_name");
        enabled = config.getBoolean("enabled");
        minPlayers = config.getInt("min_players");
        maxPlayers = config.getInt("max_players");
        ConfigurationSection spawnZoneSection = config.getConfigurationSection("spawn_zone");
        if(spawnZoneSection != null){
            getCorners(spawnZone, spawnZoneSection);
        }

        ConfigurationSection rewardsSection = config.getConfigurationSection("rewards");
        if(rewardsSection != null){
            loadRewards(rewardsSection);
        }
    }

    protected void loadRewards(ConfigurationSection rewardsSection){
        rewards.clear();
        for(String key : rewardsSection.getKeys(false)){
            ConfigurationSection section = rewardsSection.getConfigurationSection(key);
            if(section == null){
                Bukkit.getConsoleSender().sendMessage(colorize("&cGameEvents: Problem with reward &l" +
                        key + " &cin game " + minigameName));
                continue;
            }

            RewardType type = RewardType.valueOf(section.getString("type"));
            List<String> actions = section.getStringList("actions");
            String permission = section.getString("permission");
            rewards.add(new Reward(type, actions, permission));

        }
    }


    public void getCorners(Location[] array, ConfigurationSection section){
        array[0] = LocationUtils.deserialize(section.getString("corner1"));
        array[1] = LocationUtils.deserialize(section.getString("corner2"));

    }

    public Location[] getAllLocations(ConfigurationSection section) {
        return section.getKeys(false)
                .stream()
                .map(key -> LocationUtils.deserialize(section.getString(key)))
                .toArray(Location[]::new);
    }


    public abstract void reloadConfig();


    public void enable() {
        file.getConfig().set("enabled", true);
        file.saveConfig();
        reloadConfig();
    }

    public void disable() {
        file.getConfig().set("enabled", false);
        file.saveConfig();
        reloadConfig();
    }

    public void setFancyName(String fancyName) {
        file.getConfig().set("fancy_name", fancyName);
        file.saveConfig();
    }

    public void setWarmup(int warmup) {
        file.getConfig().set("warmup", warmup);
        file.saveConfig();

    }


    public void setMinPlayers(int minPlayers) {
        file.getConfig().set("min_players", minPlayers);
        file.saveConfig();
    }

    public void setMaxPlayers(int maxPlayers) {
        file.getConfig().set("max_players", maxPlayers);
        file.saveConfig();
    }




    protected Map<String, ConfigurableAttribute<?>> getBaseConfigurableAttributes(){
        Map<String, ConfigurableAttribute<?>> configurableAttributes = new HashMap<>();
        // always present values
        configurableAttributes.put("min_players", new ConfigurableAttribute<>("min_players", Integer.class, minPlayers));
        configurableAttributes.put("max_players", new ConfigurableAttribute<>("max_players", Integer.class, maxPlayers));
        configurableAttributes.put("spawn_zone", new LocationArrayAttribute("spawn_zone", spawnZone));
        configurableAttributes.put("warmup", new ConfigurableAttribute<>("warmup", Integer.class, warmup));
        configurableAttributes.put("fancy_name", new ConfigurableAttribute<>("fancy_name", String.class, fancyName));
        return configurableAttributes;
    }


    public abstract Map<String, ConfigurableAttribute<?>> getConfigurableAttributes();


    public void configureGameAttributes(Map<String, ConfigurableAttribute<?>> attributeMap){
        file.registerConfig();

        for(String key : attributeMap.keySet()){
            ConfigurableAttribute<?> attribute = attributeMap.get(key);
            Class<?> type = attribute.getType();

            if(type == Location[].class){
                @SuppressWarnings("unchecked")
                ConfigurableAttribute<Location[]> locationAttribute = (ConfigurableAttribute<Location[]>) attribute;
                configureLocationAttribute(locationAttribute);
                continue;
            }

            if(attribute.isItem()){
                @SuppressWarnings("unchecked")
                ConfigurableAttribute<ItemStack> itemAttribute = (ConfigurableAttribute<ItemStack>) attribute;
                configureItemAttribute(itemAttribute);
                continue;
            }

            if(attribute.getType().equals(MultiInventory.class)){
                continue;
            }

            if(attribute instanceof StringListAttribute stringListAttribute){
                configureStringList(stringListAttribute);
                continue;
            }

            FileConfiguration config = file.getConfig();
            config.set(attribute.getName(), attribute.getValue());
        }


        file.saveConfig();
    }

    private void configureStringList(StringListAttribute stringListAttribute) {
        FileConfiguration config = file.getConfig();
        config.set(stringListAttribute.getName(), stringListAttribute.getList());
    }

    private void configureItemAttribute(ConfigurableAttribute<ItemStack> itemAttribute) {
            FileConfiguration config = file.getConfig();
            config.set(itemAttribute.getName(), itemAttribute.getValue());
    }

    private void configureLocationAttribute(ConfigurableAttribute<Location[]> attribute) {
        int amountOfLocations = attribute.getValue().length;

        if(amountOfLocations == 2){
            configureLocationCorners(attribute);
            return;
        }

        configureMultiLocationAttribute(attribute);
    }

    private void configureLocationCorners(ConfigurableAttribute<Location[]> att){
        FileConfiguration configuration = getFile().getConfig();
        configuration.set(att.getName() + ".corner1", LocationUtils.serialize(att.getValue()[0]));
        configuration.set(att.getName() + ".corner2", LocationUtils.serialize(att.getValue()[1]));
    }


    private void configureMultiLocationAttribute(ConfigurableAttribute<Location[]> attribute){
        FileConfiguration config = file.getConfig();
        String mainPath = "location";

        for(int i = 0; i < attribute.getValue().length; i++){
            String currentPath = mainPath + (i+1);
            Location location = attribute.getValue()[i];
            config.set(attribute.getName() + "." + currentPath, LocationUtils.serialize(location));
        }
    }

    public boolean isProperlyConfigured(){
            List<ConfigurableAttribute<?>> values = new ArrayList<>(this.getConfigurableAttributes().values());
            for(ConfigurableAttribute<?> attribute : values){
                if(!attribute.isConfigured()){
                    Bukkit.getConsoleSender().sendMessage(colorize("&cThis game is not configured: &4" + attribute.getName()));
                    return false;
                }
            }
            return true;
    }

}
