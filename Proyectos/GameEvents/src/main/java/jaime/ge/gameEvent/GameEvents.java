package jaime.ge.gameEvent;

import jaime.ge.gameEvent.commands.CommandTree;
import jaime.ge.gameEvent.configurator.GameConfiguratorManager;
import jaime.ge.gameEvent.files.ConfigFile;
import jaime.ge.gameEvent.files.GameFilesManager;
import jaime.ge.gameEvent.files.MessagesFile;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.GameManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitLamp;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

@Getter
public final class GameEvents extends JavaPlugin {

    // Managers
    private GameManager gameManager;
    private GameFilesManager gameFilesManager;
    private GameConfiguratorManager gameConfiguratorManager;

    // Files
    private MessagesFile messagesFile;
    private ConfigFile configFile;

    @Override
    public void onEnable() {
        loadFiles();
        loadManagers();
        registerCommands();

        Bukkit.getConsoleSender().sendMessage(colorize(getMessagesFile().getMessage(MessageName.prefix)
                + " enabled. Thanks for using the plugin!"));

    }

    private void loadFiles() {
        configFile = new ConfigFile(this);
        messagesFile = new MessagesFile(this);
    }

    private void registerCommands() {
        var lamp = BukkitLamp.builder(this).dependency(GameEvents.class, this).build();
        lamp.register(new CommandTree());
    }

    private void loadManagers() {
        gameFilesManager = new GameFilesManager(this);
        gameManager = new GameManager(this);
        gameConfiguratorManager = new GameConfiguratorManager(this);
    }

    public void reloadPlugin(){
        messagesFile.reloadConfig();
        gameFilesManager.reloadFiles();

        gameManager.reloadManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
