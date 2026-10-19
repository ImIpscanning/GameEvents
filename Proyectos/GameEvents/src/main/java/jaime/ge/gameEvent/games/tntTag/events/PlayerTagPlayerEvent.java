package jaime.ge.gameEvent.games.tntTag.events;

import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerTagPlayerEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    private final GamePlayer tagger;
    private final GamePlayer tagged;

    public PlayerTagPlayerEvent(GamePlayer tagger, GamePlayer tagged) {
        this.tagger = tagger;
        this.tagged = tagged;
    }

    public GamePlayer getTagger() {
        return tagger;
    }

    public GamePlayer getTagged() {
        return tagged;
    }

    public Game getGame(){
        return tagged.getGame();
    }
}
