package jaime.ge.gameEvent.games._model.interfaces;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.games._model.GamePlayer;
import org.bukkit.entity.Player;

public interface ControllableGame {

    boolean hasController();
    boolean isManualMode();
    boolean playerCanBeController(Player player);
    GamePlayer getController();

    void addController(GamePlayer gamePlayer);

    void removeController(GameEvents plugin);
    boolean isController(GamePlayer gamePlayer);

    void giveControllerItems();
}
