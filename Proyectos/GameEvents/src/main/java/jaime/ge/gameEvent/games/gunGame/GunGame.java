package jaime.ge.gameEvent.games.gunGame;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.model.MultiInventory;
import jaime.ge.gameEvent.files.gameFiles.GunGameGameFile;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.enums.Status;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import jaime.ge.gameEvent.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

@Getter
public class GunGame extends Game {

    private final Map<GamePlayer, Integer> levels;
    private final MultiInventory levelLoadouts;
    private final String killSound;
    private final String deathSound;
    private final Location[] respawnLocations;
    private final int maxLevel;

    private String messageNextLevel;

    private final Map<GamePlayer, BukkitRunnable> respawnTasks;

    public GunGame(GunGameGameFile gameFile) {
        super(gameFile, MinigameName.GUN_GAME);
        levels = new HashMap<>();
        respawnTasks = new HashMap<>();
        levelLoadouts = gameFile.getMultiInventory();

        killSound = gameFile.getKillSound();
        deathSound = gameFile.getDeathSound();
        respawnLocations = gameFile.getRespawnLocations();

        maxLevel = levelLoadouts.getInventories().size() - 1;
    }

    @Override
    protected void start(GameEvents plugin) {
        selectiveRandomSpawnLocationStart();
        for(GamePlayer gamePlayer : getPlayers()){
            levels.put(gamePlayer, 0);
            giveLevelItems(gamePlayer);
        }

        messageNextLevel = plugin.getMessagesFile().getMessage(MessageName.gg_message_next_level);
    }

    @Override
    public void stop() {
        clearLevelMap();

        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), true, null);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void stop(GamePlayer winner) {
        clearLevelMap();
        respawnAndEliminatePlayers();
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), false, winner);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    private void respawnAndEliminatePlayers() {
        for(GamePlayer gamePlayer : respawnTasks.keySet()){
            respawnTasks.get(gamePlayer).cancel();
            eliminatePlayer(gamePlayer);
        }
    }

    private void clearLevelMap() {
        levels.clear();
    }

    @Override
    public void eliminatePlayer(GamePlayer gamePlayer) {
        if(gamePlayer.getGame() != this) return;
        playSoundToPlayer(gamePlayer, deathSound, 1f, 1f);
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.setAlive(false);
        addSpectator(gamePlayer);
        checkConditionsToEndByLeave();
    }

    public void temporaryKillPlayer(GamePlayer gamePlayer, GameEvents plugin){
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.getPlayer().setHealth(20);
        addSpectatorEffects(gamePlayer);
        lowerLevel(gamePlayer);
        playSoundToPlayer(gamePlayer, deathSound, 1f, 1f);

        BukkitRunnable runnable = new BukkitRunnable(){
            @Override
            public void run() {
                removeSpectatorEffects(gamePlayer);
                teleportToRandomSpawnLocation(gamePlayer);
                giveLevelItems(gamePlayer);
            }
        };

        runnable.runTaskLater(plugin, 100L);
        respawnTasks.put(gamePlayer, runnable);

    }

    private void lowerLevel(GamePlayer gamePlayer) {
        int currentLevel = levels.get(gamePlayer);
        currentLevel = currentLevel == 0 ? 0 : currentLevel - 1;
        levels.put(gamePlayer, currentLevel);
    }

    private void giveLevelItems(GamePlayer gamePlayer){
        ItemStack[] contents = levelLoadouts.getInventory(levels.get(gamePlayer));
        gamePlayer.getPlayer().getInventory().setContents(contents);
    }

    public void nextLevel(GamePlayer gamePlayer){
        gamePlayer.getPlayer().getInventory().clear();
        levels.compute(gamePlayer, (k, level) -> level + 1);
        if(levels.get(gamePlayer) >= maxLevel){
            stop(gamePlayer);
            return;
        }
        giveLevelItems(gamePlayer);
        gamePlayer.getPlayer().sendMessage(
                colorize(messageNextLevel.replace("%level%", "" + getLevel(gamePlayer))));
    }

    public int getLevel(GamePlayer gamePlayer){
        return levels.get(gamePlayer);
    }

    public void teleportToRandomSpawnLocation(GamePlayer gamePlayer){
        Location locationWithOffset = LocationUtils.addOffsetToLocation(
                LocationUtils.getRandomLocationFromArray(respawnLocations), 0, 1f, 0);
        gamePlayer.getPlayer().teleport(locationWithOffset);
    }

    private void selectiveRandomSpawnLocationStart(){
        List<GamePlayer> modifiablePlayerList = new ArrayList<>(getPlayers());
        Collections.shuffle(modifiablePlayerList);
        for(Location location : respawnLocations){
            if (modifiablePlayerList.isEmpty()) break; // Evitar crash si hay más locations que jugadores
            Location locationWithOffset = LocationUtils.addOffsetToLocation(location, 0, 1f, 0);
            modifiablePlayerList.removeFirst().getPlayer().teleport(locationWithOffset);
        }

        for(GamePlayer remainingPlayer : modifiablePlayerList){
            teleportToRandomSpawnLocation(remainingPlayer);
        }

    }
}
