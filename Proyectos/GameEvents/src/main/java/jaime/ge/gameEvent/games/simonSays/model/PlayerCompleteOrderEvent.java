package jaime.ge.gameEvent.games.simonSays.model;

import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games.simonSays.SimonSays;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerCompleteOrderEvent extends Event {
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
    private final GamePlayer gamePlayer;

    public PlayerCompleteOrderEvent(Order order, boolean fake, GamePlayer gamePlayer) {
        this.order = order;
        this.gamePlayer = gamePlayer;
        this.game = (SimonSays) gamePlayer.getGame();
    }
}
