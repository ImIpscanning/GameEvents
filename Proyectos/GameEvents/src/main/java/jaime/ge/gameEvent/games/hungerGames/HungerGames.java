package jaime.ge.gameEvent.games.hungerGames;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.model.MultiInventory;
import jaime.ge.gameEvent.files.gameFiles.HungerGamesGameFile;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.enums.Status;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import jaime.ge.gameEvent.games._model.interfaces.RegenerativeChestsMap;
import jaime.ge.gameEvent.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class HungerGames extends Game implements RegenerativeChestsMap {

    private final MultiInventory loadouts;
    private final String eliminationSound;
    private final Location[] gameZone;

    private final Location[] spawnLocations;
    private final Map<GamePlayer, Location> usedSpawnLocations;

    private final List<Chest> savedChests;



    public HungerGames(HungerGamesGameFile gameFile, GameEvents plugin) {
        super(gameFile, MinigameName.HUNGER_GAMES);
        loadouts = gameFile.getLoadouts();
        eliminationSound = gameFile.getEliminationSound();
        spawnLocations = gameFile.getSpawnLocations();
        gameZone = gameFile.getGameZone();
        usedSpawnLocations = new HashMap<>();


        savedChests = new ArrayList<>();
        new BukkitRunnable(){
            @Override
            public void run() {
                saveChestLocations();
                restoreChestContents();
            }
        }.runTask(plugin);

    }

    @Override
    protected void start(GameEvents plugin) {
        playSoundToAll("ITEM_GOAT_HORN_SOUND_2", 1F, 1F);
    }

    @Override
    public void stop() {
        savedChests.clear();

        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), true, null);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void stop(GamePlayer winner) {
        savedChests.clear();

        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), false, winner);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void eliminatePlayer(GamePlayer gamePlayer) {

    }

    @Override
    public void saveChestLocations() {
        LocationUtils.iterateRegion(gameZone[0], gameZone[1], block -> {
            if (block.getType() == Material.CHEST) {
                BlockState state = block.getState();
                if (state instanceof Chest) {
                    savedChests.add((Chest) state);
                }
            }
        });
    }

    public void restoreChestContents() {
        List<ItemStack[]> inventories = new ArrayList<>(loadouts.getInventories());
        if (inventories.isEmpty() || savedChests.isEmpty()) {
            return;
        }

        Collections.shuffle(inventories);

        for (int i = 0; i < savedChests.size(); i++) {
            Chest chest = savedChests.get(i);
            ItemStack[] originalContents = inventories.get(i % inventories.size()); // Repetir si hay más cofres

            List<ItemStack> shuffledContents = new ArrayList<>(Arrays.asList(originalContents));
            Collections.shuffle(shuffledContents); // Mezclar los items dentro del inventario

            Inventory chestInventory = chest.getInventory();
            chestInventory.clear();

            for (int slot = 0; slot < shuffledContents.size() && slot < chestInventory.getSize(); slot++) {
                chestInventory.setItem(slot, shuffledContents.get(slot));
            }
        }
    }

    public void freeSpawnLocation(GamePlayer gamePlayer){
        usedSpawnLocations.remove(gamePlayer);

    }

    public Location getNextFreeSpawnLocation(GamePlayer gamePlayer){
        List<Location> availableLocations = new ArrayList<>();
        for (Location loc : spawnLocations) {
            if (!usedSpawnLocations.containsValue(loc)) {
                availableLocations.add(loc);
            }
        }

        if (availableLocations.isEmpty()) {
            return null; // O maneja el caso en que no haya ubicaciones disponibles
        }

        Location selectedLocation = availableLocations.get(ThreadLocalRandom.current().nextInt(availableLocations.size()));
        usedSpawnLocations.put(gamePlayer, selectedLocation);
        return selectedLocation;
    }
}
