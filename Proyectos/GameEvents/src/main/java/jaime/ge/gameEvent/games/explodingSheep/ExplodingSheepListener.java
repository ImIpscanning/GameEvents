package jaime.ge.gameEvent.games.explodingSheep;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.games.GameManager;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplodingSheepListener implements Listener {

    private final GameEvents plugin;
    private final GameManager gameManager;

    public ExplodingSheepListener(GameEvents plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void preventTNTGrief(EntityExplodeEvent event){
        if (!(event.getEntity() instanceof TNTPrimed tntPrimed)) return;

        // Esto probablemente cambie en el futuro
        if(!gameManager.isGameActive(MinigameName.EXPLODING_SHEEP)) return;
        ExplodingSheep game = (ExplodingSheep) gameManager.getGameInstance(MinigameName.EXPLODING_SHEEP);

        Location[] gameZone = game.getGameZone();
        if(LocationUtils.isInsideArea(tntPrimed.getLocation(), gameZone)){
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onPlayerGetHitByExplosion(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player player)) return;
        if(!isPlayerInGame(player)) return;
        if(!(event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION))) return;

        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        ExplodingSheep game = (ExplodingSheep) gamePlayer.getGame();
        event.setDamage(0);
        game.eliminatePlayer(gamePlayer);
    }

    private boolean isPlayerInGame(Player player) {
        return gameManager.isOnGame(player, MinigameName.EXPLODING_SHEEP);
    }

    @EventHandler
    public void onPlayerPVP(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player player && event.getDamager() instanceof Player damager)){
            return;
        }
        if(!(isPlayerInGame(player) && isPlayerInGame(damager))){
            return;
        }

        if(!isPlayerInGame(player)) return;
        event.setDamage(0);
    }
}
