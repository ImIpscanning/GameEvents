package jaime.ge.gameEvent.commands;


import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.gameFiles.GameFile;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

@Command("ge")
public class CommandTree {

    @Dependency
    private GameEvents plugin;

    @CommandPlaceholder
    @CommandPermission("ge.help")
    public void defaultCommand(BukkitCommandActor actor){
        List<String> message = plugin.getMessagesFile().getListMessage(MessageName.help);
        for(String s : message){
            actor.sender().sendMessage(colorize(s));
        }
    }

    @Subcommand("configure <game>")
    @CommandPermission("ge.configure")
    public void configure(BukkitCommandActor actor,
                          MinigameName game){

        plugin.getGameConfiguratorManager().startConfiguring(actor.asPlayer(), game);
    }


    @Subcommand("enable <game>")
    @CommandPermission("ge.configure")
    public void enable(BukkitCommandActor actor, MinigameName game){
        plugin.getGameConfiguratorManager().enableGame(actor.asPlayer(), game);
    }

    @Subcommand("disable <game>")
    @CommandPermission("ge.configure")
    public void disable(BukkitCommandActor actor, MinigameName game){
        plugin.getGameConfiguratorManager().disableGame(actor.asPlayer(), game);
    }

    @Subcommand("startNextGame")
    @CommandPermission("ge.start")
    public void startNextGame(BukkitCommandActor actor){
        plugin.getGameManager().hurryGameRotation();
        actor.sender().sendMessage(colorize("&aDone"));
    }

    @Subcommand("start <game>")
    @CommandPermission("ge.start")
    public void start(BukkitCommandActor actor, MinigameName game){
        GameFile gameFile = plugin.getGameFilesManager().getGameFile(game);
        if(!gameFile.isEnabled()){
            actor.sender().sendMessage(colorize("&cGame &4" + game + " &cis disabled"));
            return;
        }
        if(plugin.getGameManager().isGameActive(game)){
            actor.sender().sendMessage(colorize("&cGame &4" + game + " &calready active"));
            return;
        }
        plugin.getGameManager().startGame(game);
        actor.sender().sendMessage(colorize("&aGame &2" + game + "&a activated."));
    }

    @Subcommand("join <game>")
    @CommandPermission("ge.join")
    public void join(BukkitCommandActor actor, MinigameName game){
        plugin.getGameManager().joinPlayer(actor.requirePlayer(), game);
    }

    @Subcommand("leave")
    @CommandPermission("ge.join")
    public void leave(BukkitCommandActor actor){
        plugin.getGameManager().removePlayer(actor.requirePlayer());
    }

    @Subcommand("reload")
    @CommandPermission("ge.reload")
    public void reload(BukkitCommandActor actor){
        plugin.reloadPlugin();
        actor.sender().sendMessage(colorize("&aPlugin reloaded."));
    }

    @Subcommand("stop <game>")
    @CommandPermission("ge.stop")
    public void stop(BukkitCommandActor actor, MinigameName game){
        if(!plugin.getGameManager().isGameActive(game)){
            actor.sender().sendMessage(colorize("&cGame &4" + game + " &cnot active"));
            return;
        }
        plugin.getGameManager().stopGame(game);
        actor.sender().sendMessage(colorize("&cGame &4" + game + " &cstopped"));
    }
}
