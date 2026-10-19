package jaime.ge.gameEvent.games;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.Reward;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.enums.Status;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import jaime.ge.gameEvent.games._model.events.GameStartCancelledEvent;
import jaime.ge.gameEvent.games._model.events.PlayerJoinGameEvent;
import jaime.ge.gameEvent.games._model.events.PlayerLeaveGameEvent;
import jaime.ge.gameEvent.games._model.interfaces.ControllableGame;
import jaime.ge.gameEvent.games._model.interfaces.RegenerativeBlocksMap;
import jaime.ge.gameEvent.utils.ItemBuilder;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class GameDefaultListener implements Listener {

    private final GameEvents plugin;
    private final GameManager gameManager;

    public GameDefaultListener(GameEvents plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinGameEvent event){
        Player player = event.getPlayer();
        Game game = event.getGameJoined();

        if(!gameHasCustomSpawn(game)){
            game.teleportToSpawn(player);
        }
        if(game instanceof ControllableGame controllableGame){
            if(controllableGame.playerCanBeController(player)){
                ItemStack controlModeItem = ItemBuilder.of(Material.COMMAND_BLOCK)
                        .name("&dAlternate controller mode").build();
                player.getInventory().setItem(0, controlModeItem);
            }
        }
    }

    private boolean gameHasCustomSpawn(Game game) {
        return Objects.requireNonNull(game.getName()) == MinigameName.HUNGER_GAMES;
    }

    @EventHandler
    public void onGameForceStopped(GameFinishEvent event){
        if(!event.isForceStopped()) return;
        event.getGame().setStatus(Status.FINISHING);
        removePlayers(event.getGame().getAllPlayers());
        gameManager.deactivateGame(event.getGame().getName());
    }

    @EventHandler
    public void onRegenerativeBlockGameStop(GameFinishEvent event){
        if(event.getGame() instanceof RegenerativeBlocksMap regenerativeBlocksMap){
            regenerativeBlocksMap.restoreBlocks();
        }
    }

    @EventHandler
    public void onGameFinishWithNoWinner(GameFinishEvent event){
        if(event.isForceStopped()) return;
        if(event.hasWinner()) return;
        event.getGame().setStatus(Status.FINISHING);
        startEndingTimer(event.getGame(), null, event.getGame().getAllPlayers());
    }

    @EventHandler
    public void onGameFinishWithWinner(GameFinishEvent event){
        if(event.isForceStopped()) return;
        if(!event.hasWinner()) return;

        Game game = event.getGame();
        game.isolatePlayerWinningEffect(event.getWinner());
        event.getGame().setStatus(Status.FINISHING);

        assert event.getWinner() != null;
        startFireworks(event.getWinner().getPlayer());
        startEndingTimer(game, event.getWinner(), event.getGame().getAllPlayers());
    }

    @EventHandler
    public void onGameStartCancelledEvent(GameStartCancelledEvent event){
        Game game = event.getGame();
        game.sendMessageToAll(colorize(plugin.getMessagesFile().getMessage(MessageName.game_countdown_cancelled)), null);
        removePlayers(event.getWaiters());
        gameManager.stopGame(game.getName());
    }


    @EventHandler
    public void onControllerItemInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.COMMAND_BLOCK) return;
        if (!gameManager.isOnGame(player)) return;

        event.setCancelled(true);
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        Game game = gamePlayer.getGame();

        if (!(game instanceof ControllableGame controllableGame)) return;
        if (controllableGame.hasController() && controllableGame.getController().equals(gamePlayer)) {
            controllableGame.removeController(plugin);
            game.sendMessageToAll(
                    plugin.getMessagesFile().getMessage(MessageName.game_controller_left),
                    player
            );
            updateControllerEnterItem(controllableGame, gamePlayer, item);
            return;
        }

        if(!controllableGame.playerCanBeController(player)){
            player.sendMessage(colorize(
                    plugin.getMessagesFile().getMessage(MessageName.game_controller_error)
            ));
        }
        else {
            controllableGame.addController(gamePlayer);
            game.sendMessageToAll(
                    plugin.getMessagesFile().getMessage(MessageName.game_controller_joined),
                    player
            );
        }
        updateControllerEnterItem(controllableGame, gamePlayer, item);

    }

    private void updateControllerEnterItem(ControllableGame game, GamePlayer gamePlayer, ItemStack item) {
        boolean shouldEnchantItem = game.hasController() && game.getController().equals(gamePlayer);
        ItemStack newItem = new ItemStack(item);
        ItemMeta meta = newItem.getItemMeta();
        meta.setEnchantmentGlintOverride(shouldEnchantItem);
        newItem.setItemMeta(meta);
        gamePlayer.getPlayer().getInventory().remove(item);
        int newSlot = shouldEnchantItem ? 8 : 0;
        gamePlayer.getPlayer().getInventory().setItem(newSlot, newItem);

    }


    private void startEndingTimer(Game game, GamePlayer winner, List<GamePlayer> allPlayers) {
        new BukkitRunnable(){

            @Override
            public void run() {
                removePlayers(game.getAllPlayers());
                plugin.getGameManager().deactivateGame(game.getName());
                distributeRewards(game, winner, allPlayers);

            }
        }.runTaskLater(plugin, 100L);
    }

    private void startFireworks(Player player) {
        new BukkitRunnable(){
            int i = 0;
            @Override
            public void run() {
                if(i == 3){
                    cancel();
                    return;
                }
                Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK_ROCKET);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                org.bukkit.Color red = org.bukkit.Color.RED;
                org.bukkit.Color blue = org.bukkit.Color.BLUE;
                org.bukkit.Color green = org.bukkit.Color.GREEN;
                org.bukkit.Color yellow = org.bukkit.Color.YELLOW;
                fireworkMeta.addEffect(FireworkEffect.builder()
                        .withColor(red, blue, green)
                        .withFade(yellow)
                        .trail(true)
                        .build());
                firework.setFireworkMeta(fireworkMeta);
                firework.setTicksToDetonate(20);
                i++;
            }
        }.runTaskTimer(plugin, 0L, 20L);

    }

    @EventHandler
    public void onPlayerPVPWhileWaiting(PrePlayerAttackEntityEvent event){
        if(!gameManager.isOnGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        if(!gamePlayer.getGame().inGame()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerLeaveWithZeroPlayers(PlayerLeaveGameEvent event){
        // To prevent weird bugs
        if(event.getGameLeft().playerCount() == 0 && event.getGameLeft().inGame()){
            event.getGameLeft().stop();
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event){
        if(!gameManager.isOnGame(event.getPlayer())) return;
        gameManager.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void cancelFireworkDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Firework){
            if(event.getEntity() instanceof Player player){
                if(!gameManager.isOnGame(player)) return;
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerGetDamageWhileSpectating(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player player)) return;
        if(!gameManager.isOnGame(player)) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        if(gamePlayer.isAlive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamageOthersWhileSpectating(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player player)) return;
        if(!gameManager.isOnGame(player)) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        if(gamePlayer.isAlive()) return;
        event.setCancelled(true);
    }


    private void removePlayers(List<GamePlayer> gamePlayerList){
        for(GamePlayer gamePlayer : gamePlayerList){
            gameManager.removePlayer(gamePlayer.getPlayer());
        }
    }

    private void distributeRewards(Game game, GamePlayer winner, List<GamePlayer> allPlayers){
        List<Reward> rewards = game.getRewards();

        for (Reward reward : rewards) {
            distributeReward(reward, winner, allPlayers);
        }
    }

    private void distributeReward(Reward reward, GamePlayer winner, List<GamePlayer> allPlayers) {
        for(GamePlayer gamePlayer : allPlayers){
            if(reward.shouldApplyTo(gamePlayer, winner)){
                reward.executeActions(gamePlayer, plugin);
            }
        }
    }




}
