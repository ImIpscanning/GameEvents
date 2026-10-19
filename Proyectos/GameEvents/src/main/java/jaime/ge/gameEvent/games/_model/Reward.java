package jaime.ge.gameEvent.games._model;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.games._model.enums.RewardActionType;
import jaime.ge.gameEvent.games._model.enums.RewardType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

@Getter
public class Reward {
    private final RewardType type;
    private final List<String> actions;
    private final String permission;
    private static final Pattern ACTION_PATTERN = Pattern.compile("^(\\w+):\\s*(.+)$");


    public Reward(RewardType type, List<String> actions, String permission) {
        this.type = type;
        this.actions = actions;
        this.permission = permission;

    }


    public void executeActions(GamePlayer gamePlayer, GameEvents plugin){
        Player player = gamePlayer.getPlayer();
        if(!canExecute(player)) return;
        for(String action : actions){
            executeAction(player, action, plugin);
        }
    }

    private void executeAction(Player player, String action, GameEvents plugin) {
        RewardActionType actionType = getActionType(action);
        String actionContent = getActionContent(action);
        actionContent = parsePlaceholders(actionContent, player, plugin);
        switch (actionType){
            case COMMAND -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), actionContent);
            }
            case MESSAGE -> {
                player.sendMessage(colorize(actionContent));
            }
            case null -> Bukkit.getConsoleSender().sendMessage(colorize("&cGameEvents: Action type not valid " +
                    "for action with content: " + actionContent));
        }
    }


    private String parsePlaceholders(String actionContent, Player player, GameEvents plugin) {
        actionContent = actionContent
                .replace("%player%", player.getName())
                .replace("%prefix%", plugin.getMessagesFile().getPrefix());
        return actionContent;
    }

    private RewardActionType getActionType(String string){
        String type = actionParser(string, true);
        if(type == null){
            return null;
        }
        return RewardActionType.valueOf(type.toUpperCase());
    }

    private String getActionContent(String string){
        return actionParser(string, false);
    }

    private String actionParser(String action, boolean shouldReturnActionType) {
        Matcher matcher = ACTION_PATTERN.matcher(action);
        if (matcher.find()) {
            String actionType = matcher.group(1);
            String actionContent = matcher.group(2);
            return shouldReturnActionType ? actionType : actionContent;
        }
        return null;
    }


    public boolean requiresPermission(){
        return permission != null;
    }

    private boolean canExecute(Player player) {
        return !requiresPermission() || player.hasPermission(permission);
    }


    public boolean shouldApplyTo(GamePlayer gamePlayer, GamePlayer winner) {
        return type.appliesTo(gamePlayer, winner);
    }


}
