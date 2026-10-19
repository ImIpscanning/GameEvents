package jaime.ge.gameEvent.games.simonSays.model;

import jaime.ge.gameEvent.games.simonSays.SimonSays;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class NewOrderEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    private final Order order;
    private final SimonSays game;
    private final boolean fake;

    public NewOrderEvent(Order order, boolean fake, SimonSays game) {
        this.order = order;
        this.game = game;
        this.fake = fake;
    }
}
