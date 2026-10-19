package jaime.ge.gameEvent.games.musicalChairs;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.gameFiles.MusicalChairsGameFile;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.enums.Status;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import jaime.ge.gameEvent.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MusicalChairs extends Game {

    private final List<Minecart> seats;

    private final String playMusicSound;
    private final String eliminationSound;
    private final int fastestTime;
    private final int slowestTime;

    private final Location[] chairsSpawnZone;

    private boolean playing;
    private BukkitRunnable musicTask;

    private GameEvents plugin;

    public MusicalChairs(MusicalChairsGameFile gameFile) {
        super(gameFile, MinigameName.MUSICAL_CHAIRS);
        playMusicSound = gameFile.getMusic();
        eliminationSound = gameFile.getEliminationSound();
        chairsSpawnZone = gameFile.getChairSpawnZone();
        fastestTime = gameFile.getFastestTime();
        slowestTime = gameFile.getSlowestTime();

        seats = new ArrayList<>();
    }

    @Override
    protected void start(GameEvents plugin) {
        this.plugin = plugin;
        startMusic();
    }


    public void startMusic(){
        playing = true;
        playSoundToAll(playMusicSound, 1f, 1f);

        if(musicTask != null){
            musicTask.cancel();
        }

        musicTask = new BukkitRunnable() {
            @Override
            public void run() {
                stopMusic();
            }
        };
        musicTask.runTaskLater(plugin, getRandomTime(fastestTime, slowestTime));
    }

    public void stopMusic(){
        playing = true;
        stopSoundToAll(playMusicSound);
        spawnSeats();

        new BukkitRunnable(){
            int time = 0;
            @Override
            public void run() {
                playSoundToAll("BLOCK_LEVER_CLICK", 1f, 1.75f);
                time++;
                if(time == 10){
                    cancel();
                    eliminationTask();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void eliminationTask() {
        List<Player> playersInSeats = new ArrayList<>();
        for(Minecart seat : seats){
            Entity passenger = seat.getPassenger();
            assert passenger instanceof Player;
            playersInSeats.add((Player) passenger);
        }


        for(GamePlayer gamePlayer : new ArrayList<>(getPlayers())){
            if(!playersInSeats.contains(gamePlayer.getPlayer())){
                eliminatePlayer(gamePlayer);
            }
        }

        if(!checkGameFinishConditions()){
            return;
        }

        despawnSeats();
        startMusic();
    }

    private boolean checkGameFinishConditions() {
        if(playerCount() == 1){
            stop(getPlayers().getFirst());
            return false;
        }
        if(playerCount() == 0){
            stop(null);
            return false;
        }

        return true;
    }

    private void spawnSeats() {
        if(playerCount() < 2) return;
        int numberOfSeats = playerCount() - 1;
        if(playerCount() > 10) numberOfSeats = playerCount() - 2;
        if(playerCount() > 20) numberOfSeats = playerCount() - 4;

        for(int i = 0; i<numberOfSeats; i++){
            Location randomLoc = LocationUtils.getRandomLocationBetween(chairsSpawnZone[0], chairsSpawnZone[1]);
            Location offsetLocation = LocationUtils.addOffsetToLocation(randomLoc, 0, 3, 0);
            Minecart seat = (Minecart) offsetLocation.getWorld().spawnEntity(offsetLocation, EntityType.MINECART);
            seats.add(seat);
        }
    }

    private void despawnSeats(){
        for(Minecart minecart : seats){
            minecart.remove();
        }
    }

    @Override
    public void stop() {
        despawnSeats();
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), true, null);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void stop(GamePlayer winner) {
        despawnSeats();
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
    }

    @Override
    protected void checkConditionsToEndByLeave() {
        if(playerCount() == 1 && inGame()){
            stop(getPlayers().getFirst());
        }
    }
}
