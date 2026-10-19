package jaime.ge.gameEvent.games.tntRun;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.GameManager;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.utils.LocationUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class TNTRunListener implements Listener {
    private final GameManager gameManager;
    private final GameEvents plugin;

    public TNTRunListener(GameEvents plugin, GameManager gameManager) {
        this.gameManager = gameManager;
        this.plugin = plugin;
    }

    public boolean isPlayerInGame(Player player){
        return gameManager.isOnGame(player, MinigameName.TNT_RUN);
    }




    @EventHandler
    public void onPlayerLeaveSpawnWhileWaiting(PlayerMoveEvent event){
        if(!isPlayerInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        TNTRun game = (TNTRun) gameManager.getGamePlayer(player).getGame();
        if(!game.isWaiting()) return;
        if(LocationUtils.isInsideCorners(player.getLocation(), game.getSpawnZone())) {
            return;
        }
        LocationUtils.keepPlayerInArea(player, game.getSpawnZone());
    }

    @EventHandler
    public void onPlayerMoveAboveTNT(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(!isPlayerInGame(player)) return;
        TNTRun game = (TNTRun) gameManager.getGamePlayer(player).getGame();
        if(!game.inGame()) return;

        Block blockBelow = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (blockBelow.getType() == Material.AIR) {
            for (BlockFace face : Arrays.asList(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST)) {
                Block neighborBelow = blockBelow.getRelative(face);
                if (neighborBelow.getType() == Material.TNT) {
                    removeBlockWithCooldown(neighborBelow, game.getBlockDecayDelayInTicks());
                }
            }
        } else if (blockBelow.getType() == Material.TNT) {
            removeBlockWithCooldown(blockBelow, game.getBlockDecayDelayInTicks());
        }

    }

    private void removeBlockWithCooldown(Block blockBelow, long ticksDelay) {
        new BukkitRunnable(){
            @Override
            public void run() {
                blockBelow.setType(Material.AIR);
            }
        }.runTaskLater(plugin, ticksDelay);
    }

    @EventHandler
    public void onPlayerEnterDeathZone(PlayerMoveEvent event){
        if(!isPlayerInGame(event.getPlayer())) return;
        Player player = event.getPlayer();
        TNTRun game = (TNTRun) gameManager.getGamePlayer(player).getGame();
        if(!game.inGame()) return;
        if(!LocationUtils.isInsideArea(player.getLocation(), game.getDeathZone())){
            return;
        }
        game.eliminatePlayer(gameManager.getGamePlayer(player));
        game.sendMessageToAll(plugin.getMessagesFile().getMessage(MessageName.tr_message_death), player);
    }


    @EventHandler
    public void cancelAllDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player player)) return;
        if(!isPlayerInGame(player)) return;
        TNTRun game = (TNTRun) gameManager.getGamePlayer(player).getGame();
        if(!game.inGame()) return;
        event.setDamage(0);
    }




}
