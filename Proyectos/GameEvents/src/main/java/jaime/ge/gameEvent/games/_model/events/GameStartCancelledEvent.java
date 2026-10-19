package jaime.ge.gameEvent.games._model.events;

import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameStartCancelledEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    private final Game game;
    private final List<GamePlayer> waiters;

    public GameStartCancelledEvent(Game game, List<GamePlayer> waiters) {
        this.game = game;
        this.waiters = new ArrayList<>(waiters);
    }

}
