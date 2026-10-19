package jaime.ge.gameEvent.games._model;

import jaime.ge.gameEvent.games.Game;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
public class GamePlayer {
    private Player player;
    private Game game;

    @Setter
    private boolean alive = true;
    private final PlayerPreJoinInformation preJoinInformation;

    public GamePlayer(Player player, Game game, PlayerPreJoinInformation preJoinInformation) {
        this.player = player;
        this.game = game;
        this.preJoinInformation = preJoinInformation;
    }


}
