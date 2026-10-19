package jaime.ge.gameEvent.games.redGreenLight;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games.GameManager;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import jaime.ge.gameEvent.utils.LocationUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class RedGreenLightListener implements Listener {
    private final GameManager gameManager;
    private final Map<Player, Integer> ticksMovedAfterRedLightMap;
    private final GameEvents plugin;

    public RedGreenLightListener(GameEvents plugin, GameManager gameManager) {
        this.gameManager = gameManager;
        ticksMovedAfterRedLightMap = new HashMap<>();
        this.plugin = plugin;
    }

    public boolean isNotInGame(Player player){
        return !gameManager.isOnGame(player, MinigameName.RED_GREEN_LIGHT);
    }




    @EventHandler
    public void onPlayerLeaveSpawnWhileWaiting(PlayerMoveEvent event){
        if(isNotInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        RedGreenLight game = (RedGreenLight) gameManager.getGamePlayer(player).getGame();

        if(game.hasController() && game.getController().equals(gameManager.getGamePlayer(player))) return;

        if(!game.isWaiting()) return;
        if(LocationUtils.isInsideCorners(player.getLocation(), game.getSpawnZone())) {
            return;
        }
        LocationUtils.keepPlayerInArea(player, game.getSpawnZone());
    }

    @EventHandler
    public void onPlayerReachFinishLine(PlayerMoveEvent event){
        if(isNotInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        RedGreenLight game = (RedGreenLight) gamePlayer.getGame();
        if(!game.inGame()) return;
        if(!gamePlayer.isAlive()) return;
        if(LocationUtils.isInsideCorners(player.getLocation(), game.getFinishLine())){
            game.stop(gamePlayer);
        }

    }

    @EventHandler
    public void onPlayerMoveDuringRedLight(PlayerMoveEvent event){
        if(isNotInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        if(!gamePlayer.isAlive()) return;
        RedGreenLight game = (RedGreenLight) gamePlayer.getGame();
        if(!game.inGame()) return;
        if(!game.isRedLight()) {
            ticksMovedAfterRedLightMap.remove(player);
            return;
        }
        if(!ticksMovedAfterRedLightMap.containsKey(player)){
            ticksMovedAfterRedLightMap.put(player, 1);
            return;
        }
        ticksMovedAfterRedLightMap.compute(player, (k, actualTicks) -> actualTicks + 1);
        int moved = ticksMovedAfterRedLightMap.get(player);
//        player.sendMessage(ticksMovedAfterRedLightMap.get(player) + "");
        if(moved > game.getForgivingTimeInTicks()){
            killPlayer(gamePlayer, game);
        }
    }

    @EventHandler
    public void onPlayerToggleSneakDuringRedLight(PlayerToggleSneakEvent event){
        if(isNotInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        if(!gamePlayer.isAlive()) return;
        RedGreenLight game = (RedGreenLight) gamePlayer.getGame();
        if(!game.inGame()) return;
        if(game.isRedLight()){
            killPlayer(gamePlayer, game);
        }
    }

    @EventHandler
    public void resetTicksOnGameFinish(GameFinishEvent event){
        Game game = event.getGame();
        if(!(game instanceof RedGreenLight redGreenLight)) return;
        for(GamePlayer gamePlayer : redGreenLight.getAllPlayers()){
            ticksMovedAfterRedLightMap.remove(gamePlayer.getPlayer());
        }

    }

    private void killPlayer(GamePlayer gamePlayer, RedGreenLight game) {
        Player player = gamePlayer.getPlayer();
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_HURT, 1, 1);
        player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.rg_message_death)));
        game.sendMessageToAll(plugin.getMessagesFile().getMessage(MessageName.rg_message_death_announce), player);
        game.eliminatePlayer(gamePlayer);
    }

    @EventHandler
    public void onPvp(PrePlayerAttackEntityEvent event){
        if(isNotInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        if(!gamePlayer.isAlive()) return;
        RedGreenLight game = (RedGreenLight) gamePlayer.getGame();
        if(!game.inGame()) return;
        if(!game.isPvp()) event.setCancelled(true);
    }

    @EventHandler
    public void onManualItemInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL || !Objects.equals(event.getHand(), EquipmentSlot.HAND)) return;
        Player player = event.getPlayer();
        if (isNotInGame(player)) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        Game game = gamePlayer.getGame();
        if (!game.inGame()) return;
        if (!(game instanceof RedGreenLight redGreenLight)) return;
        if (!redGreenLight.hasController() || !redGreenLight.getController().equals(gamePlayer)) return;

        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.isSimilar(redGreenLight.getManualGreenLightItem()) || item.isSimilar(redGreenLight.getManualRedLightItem())) {
            redGreenLight.switchLight(plugin);
        }

        updateManualItem(player, redGreenLight.isRedLight(), redGreenLight.getManualGreenLightItem(), redGreenLight.getManualRedLightItem());
    }

    private void updateManualItem(Player player, boolean redLight, ItemStack manualGreenLightItem, ItemStack manualRedLightItem) {
        player.getInventory().remove(manualGreenLightItem.getType());
        player.getInventory().remove(manualRedLightItem.getType());

        ItemStack item = redLight ? manualRedLightItem : manualGreenLightItem;
        player.getInventory().setItem(0, item);


    }


}
