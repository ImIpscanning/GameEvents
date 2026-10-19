package jaime.ge.gameEvent.games;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.MessagesFile;
import jaime.ge.gameEvent.files.gameFiles.*;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games._model.PlayerPreJoinInformation;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.events.PlayerJoinGameEvent;
import jaime.ge.gameEvent.games._model.events.PlayerLeaveGameEvent;
import jaime.ge.gameEvent.games._model.interfaces.ControllableGame;
import jaime.ge.gameEvent.games.explodingSheep.ExplodingSheep;
import jaime.ge.gameEvent.games.explodingSheep.ExplodingSheepListener;
import jaime.ge.gameEvent.games.gunGame.GunGame;
import jaime.ge.gameEvent.games.gunGame.GunGameListener;
import jaime.ge.gameEvent.games.hungerGames.HungerGames;
import jaime.ge.gameEvent.games.hungerGames.HungerGamesListener;
import jaime.ge.gameEvent.games.knockback.Knockback;
import jaime.ge.gameEvent.games.knockback.KnockbackListener;
import jaime.ge.gameEvent.games.musicalChairs.MusicalChairs;
import jaime.ge.gameEvent.games.musicalChairs.MusicalChairsListener;
import jaime.ge.gameEvent.games.redGreenLight.RedGreenLight;
import jaime.ge.gameEvent.games.redGreenLight.RedGreenLightListener;
import jaime.ge.gameEvent.games.simonSays.SimonSays;
import jaime.ge.gameEvent.games.simonSays.SimonSaysListener;
import jaime.ge.gameEvent.games.tntRun.TNTRun;
import jaime.ge.gameEvent.games.tntRun.TNTRunListener;
import jaime.ge.gameEvent.games.tntTag.TNTTag;
import jaime.ge.gameEvent.games.tntTag.TNTTagListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class GameManager {
    private final Map<Player, GamePlayer> gamePlayerMap;
    private final GameEvents plugin;

    private final Map<MinigameName, Game> nameGameMap;
    private final MessagesFile messages;

    private BukkitRunnable gameRotationTask;

    public GameManager(GameEvents plugin) {
        this.plugin = plugin;
        gamePlayerMap = new HashMap<>();
        nameGameMap = new HashMap<>();
        messages = plugin.getMessagesFile();
        registerListeners();
        startGameRotation(false);
    }

    private void registerListeners() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new GameDefaultListener(plugin, this), plugin);

        pluginManager.registerEvents(new RedGreenLightListener(plugin,this), plugin);
        pluginManager.registerEvents(new TNTTagListener(plugin, this), plugin);
        pluginManager.registerEvents(new MusicalChairsListener(this), plugin);
        pluginManager.registerEvents(new TNTRunListener(plugin, this), plugin);
        pluginManager.registerEvents(new GunGameListener(plugin, this), plugin);
        pluginManager.registerEvents(new KnockbackListener(plugin, this), plugin);
        pluginManager.registerEvents(new HungerGamesListener(plugin, this), plugin);
        pluginManager.registerEvents(new SimonSaysListener(plugin, this), plugin);
        pluginManager.registerEvents(new ExplodingSheepListener(plugin, this), plugin);
    }

    public void reloadManager(){
        stopGameRotation();
        startGameRotation(false);
    }

    public void stopGameRotation(){
        gameRotationTask.cancel();

    }

    public void hurryGameRotation(){
        stopGameRotation();
        startGameRotation(true);
    }

    public void startGameRotation(boolean startImmediately){

        long startDelay = startImmediately? 0 : plugin.getConfigFile().getSecondsBetweenGames();

        gameRotationTask = new BukkitRunnable() {
            @Override
            public void run() {
                MinigameName nextGame = selectRandomAvailableMinigame();
                if(nextGame == null) return;
                startGame(nextGame);
            }
        };

        gameRotationTask.runTaskTimer(plugin, startDelay, plugin.getConfigFile().getSecondsBetweenGames());
    }



    public void startGame(MinigameName name){
        Game game;

        switch (name){
            case RED_GREEN_LIGHT -> {
                RedGreenLightGameFile gameFile = plugin.getGameFilesManager().getRedGreenLight();
                game = new RedGreenLight(gameFile);
            }
            case TNT_TAG -> {
                TNTTagGameFile gameFile = plugin.getGameFilesManager().getTntTagGameFile();
                game = new TNTTag(gameFile);
            }
            case MUSICAL_CHAIRS -> {
                MusicalChairsGameFile gameFile = plugin.getGameFilesManager().getMusicalChairsGameFile();
                game = new MusicalChairs(gameFile);
            }
            case TNT_RUN -> {
                TNTRunGameFile gameFile = plugin.getGameFilesManager().getTntRunGameFile();
                game = new TNTRun(gameFile, plugin);
            }
            case GUN_GAME -> {
                GunGameGameFile gameFile = plugin.getGameFilesManager().getGunGameGameFile();
                game = new GunGame(gameFile);
            }
            case KNOCKBACK -> {
                KnockbackGameFile gameFile = plugin.getGameFilesManager().getKnockbackGameFile();
                game = new Knockback(gameFile);
            }
            case HUNGER_GAMES -> {
                HungerGamesGameFile gameFile = plugin.getGameFilesManager().getHungerGamesGameFile();
                game = new HungerGames(gameFile, plugin);
            }
            case SIMON_SAYS -> {
                SimonSaysGameFile gameFile = plugin.getGameFilesManager().getSimonSaysGameFile();
                game = new SimonSays(gameFile, plugin);
            }
            case EXPLODING_SHEEP -> {
                ExplodingSheepGameFile gameFile = plugin.getGameFilesManager().getExplodingSheepGameFile();
                game = new ExplodingSheep(gameFile);
            }
            case null, default -> {
                Bukkit.getConsoleSender().sendMessage(colorize("&cGameEvents: TRIED TO ACTIVATE A UNREGISTERED GAME. PLEASE REPORT THIS!"));
                return;
            }

        }

        game.startWaitingTimer(plugin);
        nameGameMap.put(name, game);
        sendStartMessageToOnlinePlayers(game, name);

    }

    public Game getGameInstance(MinigameName name){
        if(!nameGameMap.containsKey(name)) return null;
        return nameGameMap.get(name);
    }

    private void sendStartMessageToOnlinePlayers(Game game, MinigameName name) {
        Component finalMessage = createFinalMessage(game, name);
        for(Player player : Bukkit.getOnlinePlayers()){
            if(isOnGame(player)) continue;
            player.sendMessage(finalMessage);
        }

    }

    private @NotNull Component createFinalMessage(Game game, MinigameName name) {
        List<String> startMessage = plugin.getMessagesFile().getListMessage(MessageName.game_start_broadcast);
        String clickToJoinMessage = plugin.getMessagesFile().getMessage(MessageName.click_to_join);

        Component finalMessage = Component.empty();
        for(String s : startMessage){
            finalMessage = finalMessage.append(LegacyComponentSerializer.legacySection().deserialize(colorize(s
                    .replace("%game%", game.getFancyName())
                    .replace("%max_players%", game.getMaxPlayers() + "")
                    ))).append(Component.newline());
        }

        Component clickComponent = LegacyComponentSerializer.legacySection().deserialize(colorize(clickToJoinMessage));
        finalMessage = finalMessage.append(clickComponent.clickEvent(ClickEvent.runCommand("/ge join " + name)));
        return finalMessage;
    }

    public boolean isGameActive(MinigameName name){
        return nameGameMap.containsKey(name);
    }

    public void stopGame(MinigameName name){
        if(!nameGameMap.containsKey(name)) return;
        nameGameMap.get(name).stop();
        deactivateGame(name);
    }

    public void deactivateGame(MinigameName name){
        nameGameMap.remove(name);
    }

    public void joinPlayer(Player player, MinigameName minigameName){
        if(gamePlayerMap.containsKey(player)){
            player.sendMessage(messages.getMessage(MessageName.player_in_game));
            return;
        }

        MessagesFile messages = plugin.getMessagesFile();
        if(!nameGameMap.containsKey(minigameName)){
            player.sendMessage(messages.getMessage(MessageName.game_unknown));
            return;
        }

        Game game = nameGameMap.get(minigameName);
        if(!game.canJoin()){
            player.sendMessage(messages.getMessage(MessageName.game_in_game));
            return;
        }


        PlayerPreJoinInformation preJoinInformation = new PlayerPreJoinInformation(player);
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);

        GamePlayer gamePlayer = new GamePlayer(player, game, preJoinInformation);
        game.addPlayer(gamePlayer);
        gamePlayerMap.put(player, gamePlayer);
        messages.getMessage(MessageName.game_join);

        player.sendMessage(colorize(messages.getMessage(MessageName.game_join)
                .replace("%game%", game.getFancyName())));
        game.sendMessageToAll(messages.getMessage(MessageName.game_join_others), player);


        PlayerJoinGameEvent event = new PlayerJoinGameEvent(gamePlayer);
        event.callEvent();
    }

    public void removePlayer(Player player){

        if(!gamePlayerMap.containsKey(player)){
            player.sendMessage(messages.getMessage(MessageName.game_not_in_game));
            return;
        }

        GamePlayer gamePlayer = gamePlayerMap.get(player);
        Game game = gamePlayer.getGame();

        game.removePlayer(player);
        gamePlayerMap.remove(player);
        player.sendMessage(messages.getMessage(MessageName.game_leave));
        gamePlayer.getPreJoinInformation().restoreInformation();

        PlayerLeaveGameEvent leaveGameEvent = new PlayerLeaveGameEvent(gamePlayer);
        leaveGameEvent.callEvent();

        if(game instanceof ControllableGame controllableGame){
            controllableGame.removeController(plugin);
        }
    }

    public boolean isOnGame(Player player, MinigameName minigameName){
        if(!gamePlayerMap.containsKey(player)) return false;
        return gamePlayerMap.get(player).getGame().getName().equals(minigameName);
    }


    public boolean isOnGame(Player player){
        return gamePlayerMap.containsKey(player);
    }

    public GamePlayer getGamePlayer(Player player){
        return gamePlayerMap.get(player);
    }


    @Nullable
    private MinigameName selectRandomAvailableMinigame(){
        List<GameFile> enabledGames = plugin.getGameFilesManager().getEnabledGames();
        if(enabledGames.isEmpty()) return null;
        Collections.shuffle(enabledGames);
        for (GameFile enabledGame : enabledGames) {
            MinigameName name = enabledGame.getMinigameName();
            boolean canStartGame = !nameGameMap.containsKey(name);
            if (canStartGame) return name;
        }

        return null;

    }


}
