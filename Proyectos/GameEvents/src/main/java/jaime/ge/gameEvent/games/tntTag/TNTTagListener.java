package jaime.ge.gameEvent.games.tntTag;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.GameManager;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games.tntTag.events.TNTTagExplodeEvent;
import jaime.ge.gameEvent.games.tntTag.events.TaggersAssignedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class TNTTagListener implements Listener {
    private final GameEvents plugin;
    private final GameManager gameManager;

    public TNTTagListener(GameEvents plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }


    @EventHandler
    public void onTaggersAssigned(TaggersAssignedEvent event){
        TNTTag game = event.getGame();
        for(GamePlayer gamePlayer : event.getTaggers()){
            game.sendMessageToAll(plugin.getMessagesFile().getMessage(MessageName.tg_message_tagger_announce)
                    , gamePlayer.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerTagPlayer(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player tagger)) return;
        if(!(event.getEntity() instanceof Player tagged)) return;
        boolean taggerOnGame = gameManager.isOnGame(tagger, MinigameName.TNT_TAG);
        boolean taggedOnGame = gameManager.isOnGame(tagged, MinigameName.TNT_TAG);
        if(!taggedOnGame || !taggerOnGame) return;
        GamePlayer gameTagger = gameManager.getGamePlayer(tagger);
        GamePlayer gameTagged = gameManager.getGamePlayer(tagged);
        TNTTag game = (TNTTag) gameTagger.getGame();
        if(!game.inGame()) return;
        game.tagPlayer(gameTagger, gameTagged);
    }

    @EventHandler
    public void onExplode(TNTTagExplodeEvent event){
        TNTTag game = event.getGame();
        for(GamePlayer gamePlayer : event.getTaggers()){
            game.sendMessageToAll(plugin.getMessagesFile().getMessage(MessageName.tg_message_explosion), gamePlayer.getPlayer());
        }
    }


    @EventHandler
    public void onPlayerPVPWhileWaiting(PrePlayerAttackEntityEvent event){
        if(!gameManager.isOnGame(event.getPlayer(), MinigameName.TNT_TAG)) return;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        if(!gamePlayer.getGame().inGame()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTaggersExplosionCancelDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player player)) return;
        if(!gameManager.isOnGame(player, MinigameName.TNT_TAG)) return;
        if(!(event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION))) return;
        event.setCancelled(true);
    }

}
