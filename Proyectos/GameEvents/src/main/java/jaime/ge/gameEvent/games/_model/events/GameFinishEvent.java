package jaime.ge.gameEvent.games._model.events;

import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GameFinishEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Getter
    private final Game game;
    @Getter
    private final List<GamePlayer> finalists;
    @Getter
    private final boolean forceStopped;
    private final GamePlayer winner;
    @Getter
    private final List<GamePlayer> spectators;

    public GameFinishEvent(Game game, List<GamePlayer> finalists, List<GamePlayer> spectators, boolean forceStopped, GamePlayer winner) {
        this.game = game;
        this.finalists = finalists;
        this.forceStopped = forceStopped;
        this.winner = winner;
        this.spectators = spectators;
    }

    @Nullable
    public GamePlayer getWinner() {
        return winner;
    }

    public boolean hasWinner(){
        return winner != null;
    }
}
