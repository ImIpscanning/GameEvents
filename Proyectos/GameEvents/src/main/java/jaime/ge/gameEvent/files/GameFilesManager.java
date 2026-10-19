package jaime.ge.gameEvent.files;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.model.ConfigurableAttribute;
import jaime.ge.gameEvent.files.gameFiles.*;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class GameFilesManager {

    private final RedGreenLightGameFile redGreenLight;
    private final TNTTagGameFile tntTagGameFile;
    private final MusicalChairsGameFile musicalChairsGameFile;
    private final TNTRunGameFile tntRunGameFile;
    private final GunGameGameFile gunGameGameFile;
    private final KnockbackGameFile knockbackGameFile;
    private final HungerGamesGameFile hungerGamesGameFile;
    private final SimonSaysGameFile simonSaysGameFile;
    private final ExplodingSheepGameFile explodingSheepGameFile;


    public GameFilesManager(GameEvents plugin) {
        redGreenLight = new RedGreenLightGameFile(plugin);
        tntTagGameFile = new TNTTagGameFile(plugin);
        musicalChairsGameFile = new MusicalChairsGameFile(plugin);
        tntRunGameFile = new TNTRunGameFile(plugin);
        gunGameGameFile = new GunGameGameFile(plugin);
        knockbackGameFile = new KnockbackGameFile(plugin);
        hungerGamesGameFile = new HungerGamesGameFile(plugin);
        simonSaysGameFile = new SimonSaysGameFile(plugin);
        explodingSheepGameFile = new ExplodingSheepGameFile(plugin);

    }

    public void reloadFiles(){
        for(MinigameName name : MinigameName.values()){
            if(getGameFile(name) != null){
                getGameFile(name).reloadConfig();
            }
        }
    }

    public GameFile getGameFile(MinigameName minigameName){
        switch (minigameName){
            case RED_GREEN_LIGHT -> {
                return redGreenLight;
            }
            case TNT_TAG -> {
                return tntTagGameFile;
            }
            case MUSICAL_CHAIRS -> {
                return musicalChairsGameFile;
            }case TNT_RUN -> {
                return tntRunGameFile;
            }

            case GUN_GAME -> {
                return gunGameGameFile;
            }

            case KNOCKBACK -> {
                return knockbackGameFile;
            }

            case HUNGER_GAMES -> {
                return hungerGamesGameFile;
            }

            case SIMON_SAYS -> {
                return simonSaysGameFile;
            }

            case EXPLODING_SHEEP -> {
                return explodingSheepGameFile;
            }

            case null, default -> {
                return null;
            }

        }

    }

    public void loadConfiguration(Map<String, ConfigurableAttribute<?>> attributeMap, MinigameName name){
        GameFile gameFile = getGameFile(name);
        gameFile.configureGameAttributes(attributeMap);
        gameFile.reloadConfig();
    }

    public List<GameFile> getEnabledGames(){
        List<GameFile> enabledGames = new ArrayList<>();
        for(MinigameName value : MinigameName.values()){
            GameFile gameFile = getGameFile(value);
            if(gameFile != null && gameFile.isEnabled()) enabledGames.add(gameFile);
        }
        return enabledGames;
    }
}
