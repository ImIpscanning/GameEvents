package jaime.ge.gameEvent.games.tntRun;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.gameFiles.TNTRunGameFile;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.enums.Status;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import jaime.ge.gameEvent.games._model.interfaces.RegenerativeBlocksMap;
import jaime.ge.gameEvent.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class TNTRun extends Game implements RegenerativeBlocksMap {

    private Map<Location, Material> savedBlocks;
    private final Material blockToSave = Material.TNT;
    private boolean saveTaskFinished = false;

    private final Location[] gameZone;
    @Getter
    private final Location[] deathZone;
    private final String eliminationSound;
    @Getter
    private final int blockDecayDelayInTicks;

    public TNTRun(TNTRunGameFile gameFile, GameEvents plugin){
        super(gameFile, MinigameName.TNT_RUN);
        saveBlocksToRestore(plugin);

        gameZone = gameFile.getGameZone();
        eliminationSound = gameFile.getEliminationSound();
        blockDecayDelayInTicks = gameFile.getBlockDecayDelayInTicks();
        deathZone = gameFile.getDeathZone();

    }

    @Override
    protected void start(GameEvents plugin) {
        new BukkitRunnable(){
            @Override
            public void run() {
                if(saveTaskFinished){
                    cancel();
                    setInGame();
                }
            }
        }.runTaskTimer(plugin, 0, 5L);
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
        playSoundToPlayer(gamePlayer, eliminationSound, 1f, 1f);
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.setAlive(false);
        addSpectator(gamePlayer);
        checkConditionsToEndByLeave();
    }



    private void saveBlocksToRestore(GameEvents plugin) {
        savedBlocks = new HashMap<>();
        new BukkitRunnable(){
            @Override
            public void run() {
                LocationUtils.iterateRegion(gameZone[0], gameZone[1], block -> {
                    if (block.getType() == blockToSave) {
                        savedBlocks.put(block.getLocation(), block.getType());
                    }
                });

                saveTaskFinished = true;
            }
        }.runTask(plugin);
    }

    @Override
    public void restoreBlocks() {
        if(!saveTaskFinished){
            Bukkit.getConsoleSender().sendMessage(
                    colorize("&cGameEvents: Tried to restore TNTRun before saving. Report this to developers!"));
            return;
        }

        for(Location location : savedBlocks.keySet()){
            location.getBlock().setType(savedBlocks.get(location));
        }
        savedBlocks.clear();
        // Bukkit.getConsoleSender().sendMessage(colorize("&aGameEvents: Restored TNTRun game"));

    }

}
