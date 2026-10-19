package jaime.ge.gameEvent.games.redGreenLight;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.gameFiles.RedGreenLightGameFile;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.enums.Status;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import jaime.ge.gameEvent.games._model.interfaces.ControllableGame;
import jaime.ge.gameEvent.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RedGreenLight extends Game implements ControllableGame {

    @Getter
    private final int forgivingTimeInTicks;
    @Getter
    private boolean redLight;
    @Getter
    private final Location[] spawnZone;
    @Getter
    private final Location[] finishLine;
    private BukkitRunnable redGreenLightTask;
    private final int fastestTime;
    private final int slowestTime;
    @Getter
    private final boolean pvp;
    private final String redLightSound;
    private final String greenLightSound;

    private GamePlayer controller;
    private boolean manualMode;

    @Getter
    private final ItemStack manualRedLightItem;
    @Getter
    private final ItemStack manualGreenLightItem;

    private String redLightMessage;
    private String greenLightMessage;

    public RedGreenLight(RedGreenLightGameFile gameFile) {
        super(gameFile, MinigameName.RED_GREEN_LIGHT);
        forgivingTimeInTicks = gameFile.getForgivingTimeInTicks();
        fastestTime = gameFile.getFastestTime();
        slowestTime = gameFile.getSlowestTime();
        redLight = false;
        spawnZone = gameFile.getSpawnZone();
        finishLine = gameFile.getFinishLine();
        pvp = gameFile.isPvp();
        redLightSound = gameFile.getRedLightSound();
        greenLightSound = gameFile.getGreenLightSound();

        manualRedLightItem = ItemBuilder.of(Material.REDSTONE_TORCH)
                .name("&dSwitch").build();

        manualGreenLightItem = ItemBuilder.of(Material.SOUL_TORCH)
                .name("&dSwitch").build();

    }

    @Override
    public void start(GameEvents plugin) {
        redLightMessage = plugin.getMessagesFile().getMessage(MessageName.rg_message_red_light);
        greenLightMessage = plugin.getMessagesFile().getMessage(MessageName.rg_message_green_light);

        startRedGreenLightTask(plugin);
        giveControllerItems();
    }

    @Override
    public void stop(){
        cancelRedGreenLightTask();
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), true, null);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void stop(GamePlayer winner){
        cancelRedGreenLightTask();
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), false, winner);
        event.callEvent();
        setStatus(Status.FINISHING);
    }


    @Override
    public void eliminatePlayer(GamePlayer gamePlayer) {
        if(gamePlayer.getGame() != this) return;
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.setAlive(false);
        addSpectator(gamePlayer);
        checkConditionsToEndByLeave();
    }

    @Override
    protected void checkConditionsToEndByLeave() {
        if(getPlayers().isEmpty()) return;
        if(!inGame()) return;
        if(playerCount() < getMinPlayers()){
            stop(getPlayers().getFirst());
        }
    }



    private void startRedGreenLightTask(GameEvents plugin) {
        // Inicia el primer cambio de luz
        switchLight(plugin);
    }

    public void switchLight(GameEvents plugin) {
        redLight = !redLight;
        sendMessageToAll(redLight ? redLightMessage : greenLightMessage, null);
        playSoundToAll(redLight ? redLightSound : greenLightSound, 1f, 1f);

        if(isManualMode()) return;

        if (redGreenLightTask != null) {
            redGreenLightTask.cancel();
        }
        redGreenLightTask = new BukkitRunnable() {
            @Override
            public void run() {
                switchLight(plugin);
            }
        };
        redGreenLightTask.runTaskLater(plugin, getRandomTime(fastestTime, slowestTime));
    }



    private void cancelRedGreenLightTask() {
        if (redGreenLightTask != null) {
            redGreenLightTask.cancel();
            redGreenLightTask = null;
            redLight = false;
        }
    }


    @Override
    public boolean hasController() {
        return controller != null;
    }

    @Override
    public boolean isManualMode() {
        return manualMode;
    }

    @Override
    public boolean playerCanBeController(Player player) {
        return (player.hasPermission("ge.manual.*")
                || player.hasPermission("ge.manual.rg")) && !hasController();
    }

    @Override
    public GamePlayer getController() {
        return controller;
    }

    @Override
    public void addController(GamePlayer gamePlayer) {
        if(hasController()) return;
        removePlayer(gamePlayer.getPlayer());
        controller = gamePlayer;
        manualMode = true;
        gamePlayer.setAlive(false);
        gamePlayer.getPlayer().setAllowFlight(true);
    }

    @Override
    public void removeController(GameEvents plugin) {
        if(!hasController()) return;

        manualMode = false;
        controller.getPlayer().setAllowFlight(false);

        // Si el juego ya empezó, el controlador directamente sale del juego
        // y se reactiva el modo automático.
        if(inGame()){
            if(getAllPlayers().contains(controller))
                plugin.getGameManager().removePlayer(controller.getPlayer());
            controller = null;
            switchLight(plugin);
            return;
        }

        controller.setAlive(true);
        addPlayer(controller);
        controller = null;
    }

    @Override
    public boolean isController(GamePlayer gamePlayer) {
        return controller.equals(gamePlayer);
    }


    @Override
    public void giveControllerItems() {
        if(!hasController()) return;
        Player player = controller.getPlayer();

        player.getInventory().remove(Material.COMMAND_BLOCK);
        player.getInventory().setItem(0, manualRedLightItem);
    }

    @Override
    protected List<GamePlayer> getExtraPlayers() {
        return controller == null ? Collections.emptyList() : List.of(controller);
    }




}
