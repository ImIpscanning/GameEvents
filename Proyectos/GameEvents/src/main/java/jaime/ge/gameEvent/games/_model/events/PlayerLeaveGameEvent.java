package jaime.ge.gameEvent.games._model.events;

import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerLeaveGameEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    private final GamePlayer gamePlayer;
    private final Game gameLeft;

    public PlayerLeaveGameEvent(GamePlayer player){
        gamePlayer = player;
        gameLeft = player.getGame();
    }

    public Player getPlayer(){
        return gamePlayer.getPlayer();
    }
}
