package jaime.ge.gameEvent.games.knockback;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.gameFiles.GameFile;
import jaime.ge.gameEvent.files.gameFiles.KnockbackGameFile;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.enums.Status;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Knockback extends Game {

    private final ItemStack knockItem;
    @Getter
    private final Location[] deathZone;
    private final String deathSound;
    private final String killSound;


    public Knockback(KnockbackGameFile gameFile) {
        super(gameFile, MinigameName.KNOCKBACK);
        knockItem = gameFile.getKnockItem();
        deathZone = gameFile.getDeathZone();
        deathSound = gameFile.getDeathSound();
        killSound = gameFile.getKillSound();
    }

    @Override
    protected void start(GameEvents plugin) {
        giveKnockItems();
    }

    private void giveKnockItems() {
        for(GamePlayer gamePlayer : getPlayers()){
            gamePlayer.getPlayer().getInventory().addItem(knockItem);
        }
    }

    @Override
    public void stop() {
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), true, null);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void stop(GamePlayer winner) {
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), false, winner);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void eliminatePlayer(GamePlayer gamePlayer) {
        if(gamePlayer.getGame() != this) return;
        playSoundToPlayer(gamePlayer, deathSound, 1f, 1f);
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.setAlive(false);
        addSpectator(gamePlayer);
        checkConditionsToEndByLeave();
    }


    public void playKillSound(GamePlayer player){
        playSoundToPlayer(player, killSound, 1f, 1f);
    }
}
