package jaime.ge.gameEvent.games.tntTag;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.gameFiles.TNTTagGameFile;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import jaime.ge.gameEvent.games.tntTag.events.PlayerTagPlayerEvent;
import jaime.ge.gameEvent.games.tntTag.events.TNTTagExplodeEvent;
import jaime.ge.gameEvent.games.tntTag.events.TaggersAssignedEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TNTTag extends Game {

    private final boolean explodeAtRandomTime;
    private final int fastestExplodeTime;
    private final int slowestExplodeTime;

    private final String taggedSound;
    private final String explosionSound;

    private final List<GamePlayer> taggers;
    private final ItemStack headItem;

    private BukkitRunnable explodeTimer;

    public TNTTag(TNTTagGameFile gameFile) {
        super(gameFile, MinigameName.TNT_TAG);
        explodeAtRandomTime = gameFile.isExplodeRandomTime();
        fastestExplodeTime = gameFile.getFastestExplodeTime();
        slowestExplodeTime = gameFile.getSlowestExplodeTime();
        taggedSound = gameFile.getTaggedSound();
        explosionSound = gameFile.getExplosionSound();
        headItem = gameFile.getTntItem();
        taggers = new ArrayList<>();
    }

    @Override
    protected void start(GameEvents plugin) {
        assignTaggers();
        startExplodeTimer(plugin);
    }

    private void assignTaggers(){
        if(playerCount() < 2) checkConditionsToEndByLeave();
        taggers.clear();
        boolean lessThan3Players = playerCount() <= 3;
        int numberOfTaggers = lessThan3Players ? 1 : playerCount() / 3;
        List<GamePlayer> shuffledPlayerList = new ArrayList<>(getPlayers());
        Collections.shuffle(shuffledPlayerList);

        for(int i = 0; i<numberOfTaggers; i++){
            addTagger(shuffledPlayerList.get(i));
        }

        TaggersAssignedEvent event = new TaggersAssignedEvent(taggers, this);
        event.callEvent();
    }

    private void startExplodeTimer(GameEvents plugin) {
        final int desiredTimeToExplode = explodeAtRandomTime ? getRandomTime() : fastestExplodeTime;
        sendMessageToAll(plugin.getMessagesFile().getMessage(MessageName.tg_message_timer_announce)
                .replace("%s%", desiredTimeToExplode + ""), null);
        explodeTimer = new BukkitRunnable() {
            int seconds = 0;
            @Override
            public void run() {
                seconds++;
                playSoundToAll("BLOCK_LEVER_CLICK", 1f, 1.75f);
                int remainingTime = desiredTimeToExplode - seconds;
                if(remainingTime <= 5){
                    sendMessageToAll(plugin.getMessagesFile().getMessage(MessageName.tg_message_countdown)
                            .replace("%s%", remainingTime + ""), null);
                }
                if(seconds >= desiredTimeToExplode){
                    explode(plugin);
                    cancel();
                }
            }
        };
        explodeTimer.runTaskTimer(plugin, 0L, 20L);
    }

    private int getRandomTime() {
        return ThreadLocalRandom.current().nextInt(fastestExplodeTime, slowestExplodeTime + 1);
    }

    private void addTagger(GamePlayer gamePlayer){
        taggers.add(gamePlayer);
        Player player = gamePlayer.getPlayer();
        player.getInventory().setHelmet(headItem);
        playSoundToPlayer(gamePlayer, taggedSound, 1f, 1f);
    }

    private void removeTagger(GamePlayer gamePlayer){
        taggers.remove(gamePlayer);
        Player player = gamePlayer.getPlayer();
        player.getInventory().setHelmet(null);
        playSoundToPlayer(gamePlayer, taggedSound, 1f, 1.5f);
    }

    public void tagPlayer(GamePlayer tagger, GamePlayer tagged){
        if(!taggers.contains(tagger)) return;
        removeTagger(tagger);
        addTagger(tagged);
        PlayerTagPlayerEvent event = new PlayerTagPlayerEvent(tagger, tagged);
        event.callEvent();

    }


    private void explode(GameEvents plugin){
        TNTTagExplodeEvent event = new TNTTagExplodeEvent(this, new ArrayList<>(taggers));
        event.callEvent();
        playSoundToAll(explosionSound, 1f, 1f);
        for(GamePlayer gamePlayer : taggers){
            gamePlayer.getPlayer().getLocation().createExplosion(4, false, false);
        }
        eliminatePlayers(taggers);
        if(playerCount() == 1){
            stop(getPlayers().getFirst());
            return;
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                assignTaggers();
                startExplodeTimer(plugin);
            }
        }.runTaskLater(plugin, 80L);
    }

    @Override
    public void stop() {
        taggers.clear();
        if(explodeTimer != null ) explodeTimer.cancel();
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers()),
                new ArrayList<>(getSpectators()), true, null);
        event.callEvent();
    }

    @Override
    public void stop(GamePlayer winner) {
        taggers.clear();
        explodeTimer.cancel();
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers()),
                new ArrayList<>(getSpectators()), false, winner);
        event.callEvent();
    }

    @Override
    public void eliminatePlayer(GamePlayer gamePlayer) {
        if(gamePlayer.getGame() != this) return;
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.setAlive(false);
        addSpectator(gamePlayer);
    }

    private void eliminatePlayers(List<GamePlayer> gamePlayers){
        for(GamePlayer gamePlayer : gamePlayers){
            eliminatePlayer(gamePlayer);
        }
    }


}
