package jaime.ge.gameEvent.games;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.MessagesFile;
import jaime.ge.gameEvent.files.gameFiles.GameFile;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.Reward;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.enums.Status;
import jaime.ge.gameEvent.games._model.events.GameStartCancelledEvent;
import jaime.ge.gameEvent.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public abstract class Game {

    @Getter
    private final List<GamePlayer> players;
    @Getter
    private final List<GamePlayer> spectators;
    private Status status;
    private BukkitRunnable startTimer;
    @Getter
    private final int minPlayers;
    @Getter
    private final int maxPlayers;
    @Getter
    private final MinigameName name;
    private final String fancyName;
    private final int warmup;

    @Getter
    private final Location[] spawnZone;

    @Getter
    private final List<Reward> rewards;



    public Game(GameFile gameFile, MinigameName name) {
        this.minPlayers = gameFile.getMinPlayers();
        this.maxPlayers = gameFile.getMaxPlayers();
        players = new ArrayList<>();
        spectators = new ArrayList<>();
        status = Status.WAITING;
        this.name = name;
        fancyName = gameFile.getFancyName();
        warmup = gameFile.getWarmup();
        spawnZone = gameFile.getSpawnZone();
        rewards = gameFile.getRewards();
    }

    public Game(GameFile gameFile) {
        this.minPlayers = gameFile.getMinPlayers();
        this.maxPlayers = gameFile.getMaxPlayers();
        players = new ArrayList<>();
        spectators = new ArrayList<>();
        status = Status.WAITING;
        this.name = gameFile.getMinigameName();
        fancyName = gameFile.getFancyName();
        warmup = gameFile.getWarmup();
        spawnZone = gameFile.getSpawnZone();
        rewards = gameFile.getRewards();

    }

    protected abstract void start(GameEvents plugin);

    public abstract void stop();
    public abstract void stop(GamePlayer winner);

    public void startWaitingTimer(GameEvents plugin){
        MessagesFile messagesFile = plugin.getMessagesFile();

        startTimer = new BukkitRunnable() {
            private int counter = warmup;
            private int waitingTimeElapsed = 0;
            @Override
            public void run() {
                boolean enoughPlayersToStart = players.size() >= minPlayers;
                if(enoughPlayersToStart){
                    counter--;
                    if(counter == 0){
                        start(plugin);
                        sendMessageToAll(messagesFile.getMessage(MessageName.game_started), null);
                        setInGame();
                        cancel();
                        return;
                    }
                    sendMessageToAll(messagesFile.getMessage(MessageName.game_starting_countdown)
                            .replace("%s%", counter + ""), null);
                }
                if (players.size() < minPlayers && counter != warmup){
                    counter = warmup;
                    sendMessageToAll(messagesFile.getMessage(MessageName.game_countdown_cancelled), null);
                }
                if(!enoughPlayersToStart) waitingTimeElapsed += 1;
                if(waitingTimeElapsed > plugin.getConfigFile().getMaxWaitTimeForGames()){
                    cancelStart();
                }
            }
        };

        startTimer.runTaskTimer(plugin, 0L, 20L);
    }

    private void cancelStart() {
        startTimer.cancel();
        GameStartCancelledEvent event = new GameStartCancelledEvent(this, getAllPlayers());
        event.callEvent();
        stop();
    }


    public void playSoundToAll(String sound, float volume, float pitch){
        try {
            for(GamePlayer gamePlayer : getAllPlayers()){
                Player player = gamePlayer.getPlayer();
                player.playSound(player, Sound.valueOf(sound), volume, pitch);
            }
        } catch (IllegalArgumentException e) {
            for(GamePlayer gamePlayer : getAllPlayers()){
                Player player = gamePlayer.getPlayer();
                player.playSound(player, sound.toLowerCase(), volume, pitch);
            }
        }
    }

    public void stopSoundToAll(String sound){
        try {
            for(GamePlayer gamePlayer : getAllPlayers()){
                Player player = gamePlayer.getPlayer();
                player.stopSound(Sound.valueOf(sound));
            }
        } catch (IllegalArgumentException e) {
            for(GamePlayer gamePlayer : getAllPlayers()){
                Player player = gamePlayer.getPlayer();
                player.stopSound(sound.toLowerCase());
            }
        }
    }

    public void playSoundToPlayer(GamePlayer gamePlayer, String sound, float volume, float pitch){
        Player player = gamePlayer.getPlayer();
        try{
            player.playSound(player, Sound.valueOf(sound), volume, pitch);
        } catch (IllegalArgumentException e){
            player.playSound(player, sound, volume, pitch);
        }
    }


    public void sendMessageToAll(String message, @Nullable Player playerForPlaceholders){
        String playerName = playerForPlaceholders == null ? "" : playerForPlaceholders.getName();

        for(GamePlayer gamePlayer : getAllPlayers()){
            gamePlayer.getPlayer().sendMessage(colorize(message
                    .replace("%max_players%", maxPlayers + "")
                    .replace("%min_players%", minPlayers + "")
                    .replace("%game%", fancyName)
                    .replace("%current_players%", playerCount() + "")
                    .replace("%player%", playerName)));
        }

    }

    protected List<GamePlayer> getExtraPlayers() {
        return Collections.emptyList();
    }



    public void addPlayer(GamePlayer gamePlayer){
        players.add(gamePlayer);

    }

    public abstract void eliminatePlayer(GamePlayer gamePlayer);

    public void removePlayer(Player player){
        players.removeIf(gamePlayer -> gamePlayer.getPlayer().equals(player));
        checkConditionsToEndByLeave();
    }

    protected void checkConditionsToEndByLeave() {
        if(!inGame()) return;
        if(getPlayers().isEmpty()){
            stop();
        }
        if(playerCount() == 1){
            stop(getPlayers().getFirst());
        }

    }

    public boolean canJoin(){
        return !(status.equals(Status.INGAME) || status.equals(Status.FULL));
    }

    public int playerCount(){
        return players.size();
    }

    public boolean inGame(){
        return status.equals(Status.INGAME);
    }

    public boolean isWaiting(){
        return status.equals(Status.WAITING);
    }

    protected void setStatus(Status status){
        this.status = status;
    }

    public String getFancyName() {
        return colorize(fancyName);
    }


    /*
        Isolates the player making him the only player
        remaining in the game, every other player is put
        at the spectators list
     */
    public void isolatePlayerWinningEffect(GamePlayer winner){
        for(GamePlayer gamePlayer : List.copyOf(players)){
            if(gamePlayer != winner){
                addSpectator(gamePlayer);
            }
        }
    }

    public void addSpectator(GamePlayer gamePlayer){
        gamePlayer.getPlayer().getInventory().clear();
        spectators.add(gamePlayer);
        gamePlayer.setAlive(false);
        players.remove(gamePlayer);
        addSpectatorEffects(gamePlayer);
    }

    protected void addSpectatorEffects(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
        gamePlayer.getPlayer().setAllowFlight(true);
        gamePlayer.getPlayer().setFlying(true);
        gamePlayer.getPlayer().setInvisible(true);
    }

    protected void removeSpectatorEffects(GamePlayer gamePlayer){
        gamePlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
        gamePlayer.getPlayer().setAllowFlight(false);
        gamePlayer.getPlayer().setFlying(false);
        gamePlayer.getPlayer().setInvisible(false);
    }


    /*
        Get players plus spectators.
     */
    public List<GamePlayer> getAllPlayers(){
        List<GamePlayer> allPlayers = new ArrayList<>(players);
        allPlayers.addAll(spectators);
        allPlayers.addAll(getExtraPlayers());
        return allPlayers;
    }


    public void setInGame(){
        status = Status.INGAME;
    }

    protected long getRandomTime(int fastest, int slowest){
        return ThreadLocalRandom.current().nextInt(fastest, slowest + 1) * 20L;
    }

    public void teleportToSpawn(Player player){
        Location randomLoc = LocationUtils.getRandomLocationBetween(getSpawnZone());
        if(randomLoc == null) return;
        player.teleport(randomLoc);
    }


}
