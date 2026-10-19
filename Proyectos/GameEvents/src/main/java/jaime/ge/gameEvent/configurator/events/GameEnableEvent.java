package jaime.ge.gameEvent.configurator.events;

import jaime.ge.gameEvent.games._model.enums.MinigameName;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameEnableEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public MinigameName minigameName;

    public GameEnableEvent(MinigameName minigameName) {
        this.minigameName = minigameName;
    }
}
