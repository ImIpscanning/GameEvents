package jaime.ge.gameEvent.games.knockback;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games.GameManager;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.utils.LocationUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class KnockbackListener implements Listener {
    private final GameEvents plugin;
    private final GameManager gameManager;

    private final Map<Player, Player> lastAttackedByMap;

    public KnockbackListener(GameEvents plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        lastAttackedByMap = new HashMap<>();

    }
    public boolean isPlayerInGame(Player player){
        return gameManager.isOnGame(player, MinigameName.KNOCKBACK);
    }


    @EventHandler
    public void onPlayerLeaveSpawnWhileWaiting(PlayerMoveEvent event){
        if(!isPlayerInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        Game game = gameManager.getGamePlayer(player).getGame();
        if(!game.isWaiting()) return;
        if(LocationUtils.isInsideCorners(player.getLocation(), game.getSpawnZone())) {
            return;
        }
        LocationUtils.keepPlayerInArea(player, game.getSpawnZone());
    }

    @EventHandler
    public void onPlayerAttackPlayer(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player attacker)) return;
        if(!(event.getEntity() instanceof Player attacked)) return;
        boolean taggerOnGame = gameManager.isOnGame(attacker, MinigameName.KNOCKBACK);
        boolean taggedOnGame = gameManager.isOnGame(attacked, MinigameName.KNOCKBACK);
        if(!taggedOnGame || !taggerOnGame) return;

        event.setDamage(0);
        lastAttackedByMap.put(attacked, attacker);
    }

    @EventHandler
    public void onPlayerEnterDeathZone(PlayerMoveEvent event){
        if(!isPlayerInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        Knockback game = (Knockback) gameManager.getGamePlayer(player).getGame();
        if(!game.inGame()) return;

        if(!LocationUtils.isInsideArea(player.getLocation(), game.getDeathZone())){
           return;
        }
        game.eliminatePlayer(gameManager.getGamePlayer(player));
        if(lastAttackedByMap.get(player) != null){
            GamePlayer killer = gameManager.getGamePlayer(lastAttackedByMap.get(player));
            game.playKillSound(killer);
            game.sendMessageToAll(plugin.getMessagesFile().getMessage(MessageName.kb_message_killed)
                    .replace("%killer%", killer.getPlayer().getName()), player);
            return;

        }
        game.sendMessageToAll(plugin.getMessagesFile().getMessage(MessageName.kb_message_death), player);
    }

}
