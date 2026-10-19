package jaime.ge.gameEvent.files.gameFiles;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.model.ConfigurableAttribute;
import jaime.ge.gameEvent.configurator.model.LocationArrayAttribute;
import jaime.ge.gameEvent.configurator.model.StringListAttribute;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games.simonSays.model.Order;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SimonSaysGameFile extends GameFile{

    private List<String> itemsToCraft;
    private List<String> blocksToInteract;

    private int midGame;
    private int lateGame;
    private int baseTimer;
    private int lateGameTimer;
    private int totalRounds;

    private List<String> fakeOrderPrefixes;
    private String orderPrefix;
    private List<String> availableOrders;

    private Map<Order, String> orderMessages;


    public SimonSaysGameFile(GameEvents plugin) {
        super(plugin, "simonSays.yml", MinigameName.SIMON_SAYS);
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        reloadBasics();
        FileConfiguration config = getFile().getConfig();

        itemsToCraft = config.getStringList("craft_items");
        blocksToInteract = config.getStringList("right_click_blocks");
        itemsToCraft.removeIf("NONE"::equals);
        blocksToInteract.removeIf("NONE"::equals);

        midGame = config.getInt("rounds_to_mid_game");
        lateGame = config.getInt("rounds_to_late_game");
        baseTimer = config.getInt("base_timer");
        lateGameTimer = config.getInt("late_game_timer");
        totalRounds = config.getInt("max_rounds");

        fakeOrderPrefixes = config.getStringList("fake_order_prefixes");
        orderPrefix = config.getString("order_prefix");

        availableOrders = config.getStringList("valid_orders");

        orderMessages = getOrderMessagesFromSection(config.getConfigurationSection("messages"));
    }

    private Map<Order, String> getOrderMessagesFromSection(ConfigurationSection messages) {
        Map<Order, String> orderStringMap = new HashMap<>();
        for(Order order : Order.values()){
            orderStringMap.put(order, messages.getString(order.toString()));
        }
        return orderStringMap;
    }

    @Override
    public Map<String, ConfigurableAttribute<?>> getConfigurableAttributes() {
        Map<String, ConfigurableAttribute<?>> attributesMap = new HashMap<>(this.getBaseConfigurableAttributes());
        attributesMap.put("craft_items", new StringListAttribute("craft_items", itemsToCraft));
        attributesMap.put("right_click_blocks", new StringListAttribute("right_click_blocks", blocksToInteract));

        attributesMap.put("rounds_to_mid_game", new ConfigurableAttribute<>("rounds_to_mid_game", Integer.class, midGame));
        attributesMap.put("rounds_to_late_game", new ConfigurableAttribute<>("rounds_to_late_game", Integer.class, lateGame));
        attributesMap.put("max_rounds", new ConfigurableAttribute<>("max_rounds", Integer.class, totalRounds));
        attributesMap.put("base_timer", new ConfigurableAttribute<>("base_timer", Integer.class, baseTimer));
        attributesMap.put("late_game_timer", new ConfigurableAttribute<>("late_game_timer", Integer.class, lateGameTimer));
        return attributesMap;
    }
}
