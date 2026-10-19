package jaime.ge.gameEvent.games._model;

import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerPreJoinInformation {
    @Getter
    private final Location joinLocation;
    @Getter
    private final GameMode preJoinGamemode;
    @Getter
    private final ItemStack[] preJoinInventory;
    private final boolean allowFlight;
    private final Player player;

    public PlayerPreJoinInformation(Player player) {
        this.joinLocation = player.getLocation();
        this.preJoinGamemode = player.getGameMode();
        this.preJoinInventory = player.getInventory().getContents();
        allowFlight = player.getAllowFlight();
        this.player = player;
    }

    public void restoreInformation(){
        player.setGameMode(preJoinGamemode);
        player.teleport(joinLocation);
        player.getInventory().setContents(preJoinInventory);
        player.setAllowFlight(allowFlight);

        // some games set invisible to true
        player.setInvisible(false);
    }
}
