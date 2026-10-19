package jaime.ge.gameEvent.configurator.events;

import jaime.ge.gameEvent.configurator.model.ConfigurableAttribute;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PlayerStartConfiguringEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    private Player player;
    private Map<String, ConfigurableAttribute<?>> attributeMap;
    private MinigameName minigameName;

    public PlayerStartConfiguringEvent(Player player, Map<String, ConfigurableAttribute<?>> attributeMap, MinigameName minigameName) {
        this.player = player;
        this.attributeMap = attributeMap;
        this.minigameName = minigameName;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<String, ConfigurableAttribute<?>> getAttributeMap() {
        return attributeMap;
    }

    public MinigameName getMinigameName() {
        return minigameName;
    }
}
