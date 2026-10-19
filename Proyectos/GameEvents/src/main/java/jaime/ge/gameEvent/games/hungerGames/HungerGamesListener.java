package jaime.ge.gameEvent.games.hungerGames;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games.GameManager;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.events.PlayerJoinGameEvent;
import jaime.ge.gameEvent.games._model.events.PlayerLeaveGameEvent;
import jaime.ge.gameEvent.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class HungerGamesListener implements Listener {

    private final GameEvents plugin;
    private final GameManager gameManager;

    public HungerGamesListener(GameEvents plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    public boolean isNotInGame(Player player){
        return !gameManager.isOnGame(player, MinigameName.HUNGER_GAMES);
    }

    @EventHandler
    public void onJoin(PlayerJoinGameEvent event){
        Player player = event.getPlayer();

        Game game = event.getGameJoined();
        GamePlayer gamePlayer = event.getGamePlayer();

        if(!(game instanceof HungerGames hungerGames)) return;

        Location spawnLocation = hungerGames.getNextFreeSpawnLocation(gamePlayer);
        player.teleport(LocationUtils.addOffsetToLocation(spawnLocation, 0, 1f, 0));
    }

    @EventHandler
    public void onLeave(PlayerLeaveGameEvent event){
        Game game = event.getGameLeft();
        GamePlayer gamePlayer = event.getGamePlayer();
        if(!(game instanceof HungerGames hungerGames)) return;
        hungerGames.freeSpawnLocation(gamePlayer);
    }

    @EventHandler
    public void onPlayerMoveWhileWaiting(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(isNotInGame(player)) return;

        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        HungerGames game = (HungerGames) gamePlayer.getGame();
        if(game.inGame()) return;

        Location from = event.getFrom();
        Location to = event.getTo();
        if(from.getX() == to.getX() && from.getZ() == to.getZ()){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeathByPlayer(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player player && event.getDamager() instanceof Player damager)){

            return;
        }
        if(isNotInGame(player) || isNotInGame(damager)){
            return;
        }

        if(player.getHealth() - event.getFinalDamage() > 0) return;

        event.setDamage(0);
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        HungerGames game = (HungerGames) gamePlayer.getGame();
        game.eliminatePlayer(gamePlayer);

        player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_killed_by_player)
                .replace("%player%", damager.getName())));

        damager.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_killed_player)
                .replace("%player%", player.getName())));
    }


    @EventHandler
    public void onPlayerKillPlayerWithProjectile(EntityDamageByEntityEvent event){
        Entity damagedEntity = event.getEntity();
        Entity damagerEntity = event.getDamager();
        if(!(damagedEntity instanceof Player player && damagerEntity instanceof Projectile projectile)) return;
        if(!(projectile.getShooter() instanceof Player shooter)) return;
        if(isNotInGame(player) || isNotInGame(shooter)) return;
        if(player.getHealth() - event.getFinalDamage() > 0) return;
        event.setDamage(0);
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        HungerGames game = (HungerGames) gamePlayer.getGame();
        game.eliminatePlayer(gamePlayer);
        player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_killed_by_player)
                .replace("%player%", shooter.getName())));

        shooter.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_killed_player)
                .replace("%player%", player.getName())));

    }
}
