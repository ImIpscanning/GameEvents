package jaime.ge.gameEvent.games.tntTag.events;

import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games.tntTag.TNTTag;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TNTTagExplodeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    private final TNTTag game;
    private final List<GamePlayer> taggers;

    public TNTTagExplodeEvent(TNTTag game, List<GamePlayer> taggers) {
        this.game = game;
        this.taggers = taggers;
    }

    public List<GamePlayer> getTaggers() {
        return taggers;
    }

    public TNTTag getGame() {
        return game;
    }
}
