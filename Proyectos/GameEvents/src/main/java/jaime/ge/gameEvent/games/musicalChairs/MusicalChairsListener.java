package jaime.ge.gameEvent.games.musicalChairs;

import jaime.ge.gameEvent.games.GameManager;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.utils.LocationUtils;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MusicalChairsListener implements Listener {
    private final GameManager gameManager;

    public MusicalChairsListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean isPlayerInGame(Player player){
        return gameManager.isOnGame(player, MinigameName.MUSICAL_CHAIRS);
    }




    @EventHandler
    public void onPlayerLeaveSpawnWhileWaiting(PlayerMoveEvent event){
        if(!isPlayerInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        MusicalChairs game = (MusicalChairs) gameManager.getGamePlayer(player).getGame();
        if(!game.isWaiting()) return;
        if(LocationUtils.isInsideCorners(player.getLocation(), game.getSpawnZone())) {
            return;
        }
        LocationUtils.keepPlayerInArea(player, game.getSpawnZone());
    }

    @EventHandler
    public void preventMinecraftDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player player){
            if(!isPlayerInGame(player)) return;
            if(event.getEntity() instanceof Minecart){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player player && event.getDamager() instanceof Player damager)){

            return;
        }
        if(!(isPlayerInGame(player) && isPlayerInGame(damager))){
            return;
        }
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        if(!gamePlayer.isAlive()) return;
        MusicalChairs game = (MusicalChairs) gamePlayer.getGame();
        if(!game.inGame()) return;
        event.setDamage(0);


    }




}
