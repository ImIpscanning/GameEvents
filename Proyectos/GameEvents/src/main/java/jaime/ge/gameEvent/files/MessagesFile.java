package jaime.ge.gameEvent.files;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.model.MessageName;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class MessagesFile {
    private YamlFile file;
    private FileConfiguration config;

    private Map<MessageName, String> messageStringMap;
    private Map<MessageName, List<String>> messageListStringMap;

    public MessagesFile(GameEvents plugin){
        file = new YamlFile("messages.yml", null, plugin, false);
        messageStringMap = new HashMap<>();
        messageListStringMap = new HashMap<>();
        reloadConfig();
    }

    public void reloadConfig(){
        file.registerConfig();
        config = file.getConfig();

        for(String key : config.getKeys(false)){
            addToStringOrListStringMap(config, key);
        }
    }

    private void addToStringOrListStringMap(FileConfiguration config, String key){
        if(config.isString(key)){
            messageStringMap.put(MessageName.valueOf(key), config.getString(key));
            return;
        }

        messageListStringMap.put(MessageName.valueOf(key), config.getStringList(key));
    }


    public String getMessage(MessageName messageName){
        return colorize(messageStringMap.get(messageName));
    }

    public String getPrefix(){
        return getMessage(MessageName.prefix);
    }

    public List<String> getListMessage(MessageName messageName){
        return messageListStringMap.get(messageName);
    }


}
