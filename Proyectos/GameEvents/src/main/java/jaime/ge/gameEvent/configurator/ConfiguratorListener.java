package jaime.ge.gameEvent.configurator;

import io.papermc.paper.event.player.AsyncChatEvent;
import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.configurator.events.PlayerStartConfiguringEvent;
import jaime.ge.gameEvent.configurator.model.*;
import jaime.ge.gameEvent.files.MessagesFile;
import jaime.ge.gameEvent.files.model.MessageName;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

public class ConfiguratorListener implements Listener {

    private final GameEvents plugin;
    private final Map<Player, Integer> configuratorsStep;
    private final Map<Player, ConfigurableAttribute<?>> playerAttributeMap;
    private final Map<Player, Location[]> playerLocationsMap;

    private final GameConfiguratorManager gcManager;


    public ConfiguratorListener(GameEvents plugin, GameConfiguratorManager gameConfiguratorManager) {
        this.plugin = plugin;
        configuratorsStep = new HashMap<>();
        playerAttributeMap = new HashMap<>();
        playerLocationsMap = new HashMap<>();
        this.gcManager = gameConfiguratorManager;
    }

    @EventHandler
    public void onPlayerStartConfiguring(PlayerStartConfiguringEvent event){
        Player player = event.getPlayer();
        configuratorsStep.put(player, 0);
        ConfigurableAttribute<?> configurableAttribute = gcManager.getAttributeOfIndex(player, 0);
        playerAttributeMap.put(player, configurableAttribute);
        giveAndRemoveLocationWand(player, configurableAttribute);
        sendStepMessage(player);
    }

    @EventHandler
    public void onPlayerQuitWhileConfiguring(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(!gcManager.isConfiguring(player)){
            return;
        }
        stopConfiguring(player, true);
    }


    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerSendMessage(AsyncChatEvent event){
        Player player = event.getPlayer();
        if(!gcManager.isConfiguring(player)){
            return;
        }


        ConfigurableAttribute<?> attribute = playerAttributeMap.get(player);
        boolean configured = attribute.isConfigured();
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        if(message.trim().equalsIgnoreCase("cancel")) {
            event.setCancelled(true);
            stopConfiguring(player, true);
            return;
        }

        if(message.trim().equalsIgnoreCase("skip")){
            event.setCancelled(true);
            if(configured){
                nextStep(player);
            } else {
                player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_cannot_skip)));
            }
            return;
        }

        if(message.trim().equalsIgnoreCase("add")){
            event.setCancelled(true);
            if(!attribute.isMultipleInventory()){
                player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_only_valid_for_inventories)));
                return;
            }

            addToMultipleInventoriesAttribute(attribute, player);
        }
        if(message.trim().equalsIgnoreCase("remove")){
            event.setCancelled(true);
            if(!attribute.isMultipleInventory()){
                player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_only_valid_for_inventories)));
                return;
            }

            removeFromMultipleInventoriesAttribute(attribute, player);
        }




        if(message.trim().equalsIgnoreCase("ready")){
            event.setCancelled(true);
            processReady(attribute, player);
            return;
        }

        // If player must input the value via chat
        if(attribute.isString()){
            @SuppressWarnings("unchecked")
            ConfigurableAttribute<String> stringAttr = (ConfigurableAttribute<String>) attribute;
            stringAttr.setValue(colorize(message));
            player.sendMessage(colorize(attribute.toString()));
            nextStep(player);
            return;
        }

        if(attribute.isInteger()){
            event.setCancelled(true);
            if(!message.matches("(0|[1-9]\\d*)")){
                player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_invalid_number).
                        replace("%value%", message)));
                return;
            }
            @SuppressWarnings("unchecked")
            ConfigurableAttribute<Integer> intAttribute = (ConfigurableAttribute<Integer>) attribute;
            intAttribute.setValue(Integer.valueOf(message));
            nextStep(player);
            return;
        }

        if(attribute.isBoolean()){
            event.setCancelled(true);
            boolean messageIsBoolean = message.equalsIgnoreCase("true") || message.equalsIgnoreCase("false");
            if(!messageIsBoolean){
                player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_invalid_bool).
                        replace("%value%", message)));
                return;
            }
            @SuppressWarnings("unchecked")
            ConfigurableAttribute<Boolean> booleanConfigurableAttribute = (ConfigurableAttribute<Boolean>) attribute;
            booleanConfigurableAttribute.setValue(Boolean.parseBoolean(message));
            player.sendMessage(attribute.toString());
            nextStep(player);
            return;
        }

        if(attribute.isMultipleInventory()){
            event.setCancelled(true);
            return;
        }

        if(attribute instanceof StringListAttribute stringListAttribute){
            event.setCancelled(true);
            stringListAttribute.addString(message);
            player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_added_value)+ stringListAttribute));
            return;
        }




    }

    private void removeFromMultipleInventoriesAttribute(ConfigurableAttribute<?> attribute, Player player) {
        if(!(attribute instanceof MultiInventoryAttribute multiInventoryAttribute)){
            return;
        }

        MultiInventory multiInventory = multiInventoryAttribute.getValue();
        if(multiInventory.isEmpty()) {
            player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_no_previous_item_inventory)));
            return;
        }

        player.getInventory().setContents(multiInventoryAttribute.removeLast());
        player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_removed_item_inventory)));

    }

    private void addToMultipleInventoriesAttribute(ConfigurableAttribute<?> attribute, Player player) {
        if(!(attribute instanceof MultiInventoryAttribute multiInventoryAttribute)){
            return;
        }
        if(player.getInventory().isEmpty()){
            player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_no_items)));
            return;
        }

        multiInventoryAttribute.addInventory(player.getInventory().getContents());
        player.getInventory().clear();

        player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_added_item_inventory) + multiInventoryAttribute.getValue().getInventories().size()));


    }

    private void stopConfiguring(Player player, boolean cancelled) {
        playerLocationsMap.remove(player);
        playerAttributeMap.remove(player);
        configuratorsStep.remove(player);
        if(cancelled){
            gcManager.removeConfigurator(player);
            player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_cancelled)));
            return;
        }
        gcManager.sendConfiguration(player);
        player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_finished)));
        return;
    }

    private void processReady(ConfigurableAttribute<?> attribute, Player player) {

        // check if Location[] attributes are ready
        if(attribute.getType().equals(Location[].class)){
            checkLocationArrayAttribute((LocationArrayAttribute) attribute, player);
        }

        if(attribute.isItem()){
            ItemStack item = player.getInventory().getItemInMainHand();
            if(item.isEmpty() || item.getType().isAir()){
                player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_invalid_item)));
                return;
            }
            @SuppressWarnings("unchecked")
            ConfigurableAttribute<ItemStack> itemAttribute = (ConfigurableAttribute<ItemStack>) attribute;
            itemAttribute.setValue(item);
        }

        if(!attribute.isConfigured()){
            player.sendMessage(colorize(plugin.getMessagesFile().getMessage(MessageName.config_not_configured)));
            return;
        }

        nextStep(player);
    }

    private void checkLocationArrayAttribute(LocationArrayAttribute attribute, Player player) {
        for(Location location : playerLocationsMap.get(player)){
            if(location == null){
                player.sendMessage(colorize("&cError found in location. Contact developers about this!"));
                return;
            }
        }

        attribute.setValue(playerLocationsMap.get(player));
    }

    @EventHandler
    private void onLocationWandInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getItem() == null){
            return;
        }
        if(!gcManager.isConfiguring(player)){
            return;
        }

        boolean usedLocationWand = event.getItem().getType().equals(Material.NETHERITE_AXE);
        boolean interactedOnBlock = event.getClickedBlock() != null;
        if(!usedLocationWand || !interactedOnBlock){
            return;
        }

        event.setCancelled(true);

        ConfigurableAttribute<?> attribute = playerAttributeMap.get(player);

        Location location = event.getClickedBlock().getLocation();
        String locationString = location.getX()+ " " + location.getY() + " " + location.getZ();

        // If simple location, set the value with any click
        if(attribute.getType().equals(Location.class)){
            @SuppressWarnings("unchecked")
            ConfigurableAttribute<Location> locationAttribute = (ConfigurableAttribute<Location>) attribute;
            locationAttribute.setValue(location);
            return;
        }

        // perdon por esto
        // If Location[], set values with left and right click
        if(attribute.getType().equals(Location[].class)){
            LocationArrayAttribute locationAttribute = (LocationArrayAttribute) attribute;

            if(locationAttribute.getDesiredSize() == 2){
                if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                    playerLocationsMap.get(player)[0] = location;
                    player.sendMessage(colorize("&aFirst location set to: " + locationString));
                    return;
                }
                if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                    playerLocationsMap.get(player)[1] = location;
                    player.sendMessage(colorize("&aSecond location set to: " + locationString));
                    return;
                }
                return;
            }
            // if there are multiple locations

            List<Location> locations = new ArrayList<>(Arrays.asList(playerLocationsMap.getOrDefault(player, new Location[0])));

            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                if(locations.contains(location)){
                    player.sendMessage(colorize("&cThere's already a location saved at this position"));
                    return;
                }
                locations.add(location);
                player.sendMessage(colorize("&aNew position added: " + locationString));
            }

            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if(!locations.contains(location)){
                    player.sendMessage(colorize("&cNo saved location found at this position"));
                    return;
                }
                locations.remove(location);
                player.sendMessage(colorize("&aRemoved location: " + locationString));
            }


            playerLocationsMap.put(player, locations.toArray(new Location[0]));

        }



    }


    private void nextStep(Player player) {
        int step = configuratorsStep.get(player) + 1;
        configuratorsStep.put(player, step);
        ConfigurableAttribute<?> configurableAttribute = gcManager.getAttributeOfIndex(player, step);

        boolean noMoreAttributesToConfigure = configurableAttribute == null;
        if(noMoreAttributesToConfigure){
            stopConfiguring(player, false);
            return;
        }

        playerAttributeMap.put(player, configurableAttribute);
        giveAndRemoveLocationWand(player, configurableAttribute);
        sendStepMessage(player);
    }

    /*
        Setups the Location[] in the map for the wand listener properly assigning values
        to the array
     */
    private void setupLocationArrayMapForPlayer(Player player, ConfigurableAttribute<?> attribute) {
        if(attribute.getType().equals(Location[].class)){
            LocationArrayAttribute locationArrayAttribute = (LocationArrayAttribute) attribute;
            int desiredSize = locationArrayAttribute.getDesiredSize();
            if(desiredSize == -1) desiredSize = 0;
            playerLocationsMap.put(player, new Location[desiredSize]);
        }

    }

    private void giveAndRemoveLocationWand(Player player, ConfigurableAttribute<?> attribute) {
        setupLocationArrayMapForPlayer(player, attribute);

        player.getInventory().remove(Material.NETHERITE_AXE);

        if(attribute.getType().equals(Location.class) || attribute.getType().equals(Location[].class) ){
            ItemStack locationWand = new ItemStack(Material.NETHERITE_AXE);
            ItemMeta meta = locationWand.getItemMeta();
            meta.setDisplayName(colorize(plugin.getConfigFile().getLocationWandName()));

            if (meta.hasLore()){
                Objects.requireNonNull(meta.lore()).clear();
            }
            locationWand.setItemMeta(meta);
            player.getInventory().addItem(locationWand);
        }

    }

    private void sendStepMessage(Player player) {
        ConfigurableAttribute<?> attribute = playerAttributeMap.get(player);
        List<String> configMessage = plugin.getMessagesFile().getListMessage(MessageName.config_message);

        for(String s : configMessage){
            player.sendMessage(colorize(s
                    .replace("%value%", attribute.getName())
                    .replace("%type%", getFormattedType(attribute))
                    .replace("%previous_value%", getPreviousValue(attribute))));
        }
    }

    private CharSequence getPreviousValue(ConfigurableAttribute<?> attribute) {
        if(attribute.isConfigured()){
            return attribute.toString();
        }
        return plugin.getMessagesFile().getMessage(MessageName.undefined_previous_value);
    }

    private String getFormattedType(ConfigurableAttribute<?> attribute) {
        MessagesFile messagesFile = plugin.getMessagesFile();
        return switch (attribute.getType().getSimpleName()){
            case "Integer" -> messagesFile.getMessage(MessageName.value_int);
            case "String" -> messagesFile.getMessage(MessageName.value_string);
            case "Location" -> messagesFile.getMessage(MessageName.value_location);
            case "Location[]" -> messagesFile.getMessage(MessageName.value_locations);
            case "Boolean" -> messagesFile.getMessage(MessageName.value_bool);
            case "ItemStack" -> messagesFile.getMessage(MessageName.value_item);
            case "String[]" -> messagesFile.getMessage(MessageName.value_item_string_list);
            case "MultiInventory" -> messagesFile.getMessage(MessageName.value_item_inventory_multiple);
            default -> "Unrecognized"  + attribute.getType().getSimpleName();

        };
    }
}
