package jaime.ge.gameEvent.games.explodingSheep;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.gameFiles.ExplodingSheepGameFile;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.Status;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import jaime.ge.gameEvent.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExplodingSheep extends Game {

    private final int explodeTimer;
    @Getter
    private final Location[] gameZone;

    private float sheepSpawnDelay = 4;

    private final BukkitRunnable tickTask;
    private final BukkitRunnable sheepSpawnTask;


    private final List<ExplosiveSheep> sheepList;

    public ExplodingSheep(ExplodingSheepGameFile gameFile) {
        super(gameFile);

        explodeTimer = gameFile.getExplodeTimer();
        gameZone = gameFile.getGameZone();

        sheepList = new ArrayList<>();

        tickTask = new BukkitRunnable() {
            @Override
            public void run() {
                List<ExplosiveSheep> snapshot = new ArrayList<>(sheepList);
                for (ExplosiveSheep sheep : snapshot) {
                    sheep.tick();
                    if (sheep.isDead()) {
                        sheepList.remove(sheep);
                    }
                }
            }
        };




        sheepSpawnTask = createSpawnRunnable();
    }

    private @NotNull BukkitRunnable createSpawnRunnable() {
        final BukkitRunnable sheepSpawnTask;
        sheepSpawnTask = new BukkitRunnable() {
            float i = 0.0f;
            int timesSpawned = 0;

            @Override
            public void run() {
                if (i >= sheepSpawnDelay) {
                    timesSpawned++;
                    for (GamePlayer gamePlayer : getPlayers()) {
                        spawnSheep(gamePlayer.getPlayer());
                    }
                    i = 0.0f;
                }

                if(timesSpawned >= 8 && sheepSpawnDelay > 0.25f){
                    sheepSpawnDelay -= 0.25f;
                    timesSpawned = 0;
                }

                i += 0.25f;
            }
        };
        return sheepSpawnTask;
    }

    private Location findSpawnLocation(Location center) {
        World world = center.getWorld();
        Random random = new Random();
        int spawnRadius = 4;

        for (int attempt = 0; attempt < 10; attempt++) {
            // Posición aleatoria dentro del radio
            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = random.nextDouble() * spawnRadius;
            int x = center.getBlockX() + (int) (Math.cos(angle) * distance);
            int z = center.getBlockZ() + (int) (Math.sin(angle) * distance);

            // Buscar desde arriba hacia abajo hasta encontrar un bloque sólido sobre el jugador
            int maxY = Math.min(world.getMaxHeight() - 1, center.getBlockY() + 15);
            int minY = center.getBlockY() + 2;

            for (int y = maxY; y >= minY; y--) {
                Block block = world.getBlockAt(x, y, z);
                if (block.getType().isSolid()) {
                    Location spawnLoc = new Location(world, x + 0.5, y - 1, z + 0.5);
                    // Verificar que haya espacio para la oveja
                    if (spawnLoc.getBlock().isEmpty() && spawnLoc.clone().add(0, 1, 0).getBlock().isEmpty()) {
                        if (LocationUtils.isInsideArea(spawnLoc, gameZone)) {
                            return spawnLoc;
                        }
                    }
                    break;
                }
            }
        }

        // Fallback: si no se encontró una buena ubicación, spawnea sobre el jugador
        return LocationUtils.isInsideArea(center, gameZone) ? center.clone().add(0, 6, 0) : null;
    }


    public void spawnSheep(Player player){
        Location safeLocation = findSpawnLocation(player.getLocation());
        if(safeLocation == null) return;
        ExplosiveSheep sheep = new ExplosiveSheep(safeLocation, explodeTimer);
        sheepList.add(sheep);
    }

    @Override
    protected void start(GameEvents plugin) {
        tickTask.runTaskTimer(plugin, 0, 20L);
        sheepSpawnTask.runTaskTimer(plugin, 0, 5L);
    }

    @Override
    public void stop() {
        for(ExplosiveSheep explosiveSheep : sheepList){
            explosiveSheep.despawn();
        }
        sheepList.clear();

        try{
            tickTask.cancel();
            sheepSpawnTask.cancel();
        } catch (IllegalStateException e){
            // They never started in first place
        }
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), true, null);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void stop(GamePlayer winner) {
        for(ExplosiveSheep explosiveSheep : sheepList){
            explosiveSheep.despawn();
        }
        sheepList.clear();
        tickTask.cancel();
        sheepSpawnTask.cancel();
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), false, winner);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void eliminatePlayer(GamePlayer gamePlayer) {
        if(gamePlayer.getGame() != this){
            Bukkit.getConsoleSender().sendMessage("Game not good!");
        }
        playSoundToPlayer(gamePlayer, "ENTITY_GENERIC_HURT", 1f, 1f);
        gamePlayer.getPlayer().getInventory().clear();
        addSpectator(gamePlayer);
        checkConditionsToEndByLeave();
    }
}
