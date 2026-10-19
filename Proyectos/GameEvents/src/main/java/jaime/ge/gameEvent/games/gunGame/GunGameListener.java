package jaime.ge.gameEvent.games.gunGame;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.GameManager;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.utils.LocationUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class GunGameListener implements Listener {
    public final GameEvents plugin;
    public final GameManager gameManager;

    public GunGameListener(GameEvents plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    public boolean isPlayerInGame(Player player){
        return gameManager.isOnGame(player, MinigameName.GUN_GAME);
    }



    @EventHandler
    public void onPlayerLeaveSpawnWhileWaiting(PlayerMoveEvent event){
        if(!isPlayerInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        GunGame game = (GunGame) gameManager.getGamePlayer(player).getGame();
        if(!game.isWaiting()) return;
        if(LocationUtils.isInsideCorners(player.getLocation(), game.getSpawnZone())) {
            return;
        }
        LocationUtils.keepPlayerInArea(player, game.getSpawnZone());
    }

    @EventHandler
    public void onPlayerDeathByPlayer(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player player && event.getDamager() instanceof Player damager)){

            return;
        }
        if(!(isPlayerInGame(player) && isPlayerInGame(damager))){
            return;
        }

        if(player.getHealth() - event.getFinalDamage() > 0) return;

        event.setDamage(0);
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        GunGame game = (GunGame) gamePlayer.getGame();
        game.temporaryKillPlayer(gamePlayer, plugin);
        nextLevelForPlayer(damager);
        player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_killed_by_player)
                .replace("%player%", damager.getName())));
    }


    @EventHandler
    public void onPlayerKillPlayerWithProjectile(EntityDamageByEntityEvent event){
        Entity damagedEntity = event.getEntity();
        Entity damagerEntity = event.getDamager();
        if(!(damagedEntity instanceof Player player && damagerEntity instanceof Projectile projectile)) return;
        if(!(projectile.getShooter() instanceof Player shooter)) return;
        if(!(isPlayerInGame(player) && isPlayerInGame(shooter))) return;
        if(player.getHealth() - event.getFinalDamage() > 0) return;
        event.setDamage(0);
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        GunGame game = (GunGame) gamePlayer.getGame();
        game.temporaryKillPlayer(gamePlayer, plugin);
        nextLevelForPlayer(shooter);
        player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_killed_by_player)
                .replace("%player%", shooter.getName())));
    }

    private void nextLevelForPlayer(Player player) {
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        GunGame game = (GunGame) gamePlayer.getGame();
        game.nextLevel(gamePlayer);
    }


    @EventHandler
    public void onPlayerExternalDeath(PlayerDeathEvent event){
        Player player = event.getPlayer();
        if(!(isPlayerInGame(player))) return;
        player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_unknown_death)));
        event.setCancelled(true);

        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        GunGame game = (GunGame) gamePlayer.getGame();
        game.temporaryKillPlayer(gamePlayer, plugin);

    }





}
