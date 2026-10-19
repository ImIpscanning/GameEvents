package jaime.ge.gameEvent.configurator;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.events.GameDisableEvent;
import jaime.ge.gameEvent.configurator.events.GameEnableEvent;
import jaime.ge.gameEvent.configurator.events.PlayerStartConfiguringEvent;
import jaime.ge.gameEvent.configurator.model.ConfigurableAttribute;
import jaime.ge.gameEvent.files.gameFiles.GameFile;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class GameConfiguratorManager {
    private final GameEvents plugin;
    private final Map<Player, Map<String, ConfigurableAttribute<?>>> configurators;
    private final Map<MinigameName, Player> alreadyBeingConfiguredGames;

    public GameConfiguratorManager(GameEvents plugin) {
        this.plugin = plugin;
        configurators = new HashMap<>();
        alreadyBeingConfiguredGames = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(new ConfiguratorListener(plugin, this), plugin);
    }


    public void startConfiguring(Player player, MinigameName minigameName){
        if(configurators.containsKey(player)){
            player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_already_configuring)));
            return;
        }

        if(alreadyBeingConfiguredGames.containsKey(minigameName)){
            player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_already_configuring_other)
                    .replace("%player%", alreadyBeingConfiguredGames.get(minigameName).getName())
                    ));
            return;
        }

        Map<String, ConfigurableAttribute<?>> attributeMap = getConfigurableAttributes(minigameName);
        if(attributeMap == null){
            player.sendMessage(colorize("&cGame corrupted. Contact developers."));
            return;
        }

        boolean gameActive = plugin.getGameManager().isGameActive(minigameName);
        boolean gameEnabled =plugin.getGameFilesManager().getGameFile(minigameName).isEnabled();
        if(gameActive || gameEnabled){
            player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_stop_and_disable)));
            return;
        }


        configurators.put(player, attributeMap);
        alreadyBeingConfiguredGames.put(minigameName, player);
        PlayerStartConfiguringEvent event = new PlayerStartConfiguringEvent(player, attributeMap, minigameName);
        event.callEvent();
    }

    public void removeConfigurator(Player player){
        configurators.remove(player);
        alreadyBeingConfiguredGames.remove(getConfiguredMinigameByPlayer(player));
    }

    private Map<String, ConfigurableAttribute<?>> getConfigurableAttributes(MinigameName name){
        return plugin.getGameFilesManager().getGameFile(name).getConfigurableAttributes();
    }

    public boolean isConfiguring(Player player){
        return configurators.containsKey(player);
    }

    public ConfigurableAttribute<?> getAttributeOfIndex(Player player, int index){
        List<String> keys = configurators.get(player).keySet().stream().toList();
        if(index > keys.size()-1){
            return null;
        }

        String key = keys.get(index);
        return configurators.get(player).get(key);
    }

    private MinigameName getConfiguredMinigameByPlayer(Player player){
        return alreadyBeingConfiguredGames
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == player)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public void sendConfiguration(Player player) {
        MinigameName name = getConfiguredMinigameByPlayer(player);
        plugin.getGameFilesManager().loadConfiguration(configurators.get(player), name);
        removeConfigurator(player);
    }


    public void enableGame(CommandSender sender, MinigameName name){
        GameFile gameFile = plugin.getGameFilesManager().getGameFile(name);
        if(gameFile == null) {
            sender.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_unknown)));
            return;
        }
        if(!gameFile.isProperlyConfigured()) {
            sender.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_not_configured)
                    .replace("%game%", String.valueOf(name))));
            return;
        }

        gameFile.enable();
        GameEnableEvent gameEnableEvent = new GameEnableEvent(name);
        gameEnableEvent.callEvent();
        sender.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_enabled)
                .replace("%game%", gameFile.getFancyName())));
    }

    public void disableGame(CommandSender sender, MinigameName name){
        GameFile gameFile = plugin.getGameFilesManager().getGameFile(name);
        if(gameFile == null) {
            sender.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_unknown)));
            return;
        }

        gameFile.disable();
        GameDisableEvent gameDisableEvent = new GameDisableEvent(name);
        gameDisableEvent.callEvent();
        sender.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.game_disabled)
                .replace("%game%", gameFile.getFancyName())));
    }
}
