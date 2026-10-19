package jaime.ge.gameEvent.games.simonSays;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.games.GameManager;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.MinigameName;
import jaime.ge.gameEvent.games._model.events.PlayerLeaveGameEvent;
import jaime.ge.gameEvent.games.simonSays.model.Order;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SimonSaysListener implements Listener {
    private final GameEvents plugin;
    private final GameManager gameManager;

    public SimonSaysListener(GameEvents plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }

    public boolean isNotInGame(Player player){
        return !gameManager.isOnGame(player, MinigameName.SIMON_SAYS);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        if(isNotInGame(player)) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        SimonSays game = (SimonSays) gamePlayer.getGame();
        if(!game.isOrderActive(Order.SNEAK)) return;

        boolean orderCompleted = event.isSneaking();
        if(orderCompleted){
            game.completeOrder(gamePlayer);
        }
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event){
        Player player = event.getPlayer();
        if(isNotInGame(player)) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        SimonSays game = (SimonSays) gamePlayer.getGame();
        if(!game.isOrderActive(Order.JUMP)) return;
        game.completeOrder(gamePlayer);
    }

    @EventHandler
    public void onPlayerRightClickPlayer(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        if(isNotInGame(player)) return;
        Entity entity = event.getRightClicked();
        if(!(entity instanceof Player rightClicked)){
            return;
        }
        if(isNotInGame(rightClicked)) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        SimonSays game = (SimonSays) gamePlayer.getGame();
        if(!game.isOrderActive(Order.CLICK_PLAYER)) return;
        if(game.getActivePlayer().equals(rightClicked)){
            game.completeOrder(gamePlayer);
        }
    }


    @EventHandler
    public void onPlayerCraftItem(CraftItemEvent event){
        Player player = (Player) event.getWhoClicked();
        if(isNotInGame(player)) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        SimonSays game = (SimonSays) gamePlayer.getGame();
        if(!game.isOrderActive(Order.CRAFT_ITEM)) return;
        CraftingInventory inventory = event.getInventory();
        ItemStack itemCrafted = inventory.getResult();
        assert itemCrafted != null;
        if(game.getActiveMaterial().equals(itemCrafted.getType())){
            inventory.remove(itemCrafted);
            inventory.clear();
            player.closeInventory();
            game.completeOrder(gamePlayer);
        }
    }

    @EventHandler
    public void onRightClickBlock(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(isNotInGame(player)) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        SimonSays game = (SimonSays) gamePlayer.getGame();
        if(!game.isOrderActive(Order.RIGHT_CLICK_BLOCK)) return;
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        assert block != null;
        if(block.getType().equals(game.getActiveMaterial())){
            game.completeOrder(gamePlayer);
        }
    }



    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerLeaveGameEvent event){
        if(event.getGameLeft() instanceof SimonSays game){
            if(!game.inGame()) return;
            game.evaluateTimer();
        }
    }

    @EventHandler
    public void onManualItemRightClickPlayer(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        if(isNotInGame(player)) return;
        if(!(event.getRightClicked() instanceof Player clickedPlayer)) return;
        if(event.getHand() != EquipmentSlot.HAND) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        SimonSays simonSays = (SimonSays) gamePlayer.getGame();
        event.setCancelled(true);
        if(!player.getInventory().getItemInMainHand().isSimilar(simonSays.getClickManualItem())) return;
        executeNewManualOrder(player, simonSays, Order.CLICK_PLAYER, clickedPlayer, false);
    }

    // NO FUNCIONA LA ORDEN DEL PALITO

    @EventHandler
    public void onManualItemLeftClickPlayer (EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player player)) return;
        if(!(event.getEntity() instanceof Player clickedPlayer)) return;
        if(isNotInGame(player)) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        SimonSays simonSays = (SimonSays) gamePlayer.getGame();
        event.setCancelled(true);
        if(!player.getInventory().getItemInMainHand().isSimilar(simonSays.getClickManualItem())) return;
        executeNewManualOrder(player, simonSays, Order.CLICK_PLAYER, clickedPlayer, true);

    }

    @EventHandler
    public void onManualItemInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(isNotInGame(player)) return;
        if(event.getAction() == Action.PHYSICAL) return;
        if(event.getItem() == null) return;
        if(event.getHand() != EquipmentSlot.HAND) return;
        GamePlayer gamePlayer = gameManager.getGamePlayer(player);
        SimonSays simonSays = (SimonSays) gamePlayer.getGame();
        event.setCancelled(true);

        ItemStack itemStack = event.getItem();
        Order order = getRepresentedOrder(itemStack, simonSays, event);
        if(order == null) return;
        Object target = getOrderTarget(order, event);

        executeNewManualOrder(player, simonSays, order, target, event.getAction().isLeftClick());
    }

    private void executeNewManualOrder(Player player, SimonSays game, Order order, Object target, boolean fake) {
        boolean executed = game.newManualOrder(order, target, fake);
        if(!executed){
            player.playSound(player ,Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1f, 0.9f);
        } else {
            player.playSound(player ,Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1.3f);
        }
    }

    private Object getOrderTarget(Order order, PlayerInteractEvent event) {
        if(order == Order.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock() != null) return event.getClickedBlock().getType();
        }
        return null;
    }

    private Order getRepresentedOrder(ItemStack itemStack, SimonSays simonSays, PlayerInteractEvent event) {
        if(itemStack.isSimilar(simonSays.getJumpManualItem())) return Order.JUMP;
        if(itemStack.isSimilar(simonSays.getCraftManualItem())) return Order.CRAFT_ITEM;
        if(itemStack.isSimilar(simonSays.getSneakManualItem())) return Order.SNEAK;
        if(itemStack.isSimilar(simonSays.getClickManualItem()) && (event.getAction()
                == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) return Order.RIGHT_CLICK_BLOCK;
        return null;
    }


}
