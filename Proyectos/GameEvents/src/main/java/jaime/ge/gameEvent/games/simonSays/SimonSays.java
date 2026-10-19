package jaime.ge.gameEvent.games.simonSays;

import jaime.ge.gameEvent.GameEvents;
import jaime.ge.gameEvent.files.gameFiles.SimonSaysGameFile;
import jaime.ge.gameEvent.files.model.MessageName;
import jaime.ge.gameEvent.games.Game;
import jaime.ge.gameEvent.games._model.GamePlayer;
import jaime.ge.gameEvent.games._model.enums.Status;
import jaime.ge.gameEvent.games._model.events.GameFinishEvent;
import jaime.ge.gameEvent.games._model.interfaces.ControllableGame;
import jaime.ge.gameEvent.games.simonSays.model.NewOrderEvent;
import jaime.ge.gameEvent.games.simonSays.model.Order;
import jaime.ge.gameEvent.games.simonSays.model.PlayerCompleteOrderEvent;
import jaime.ge.gameEvent.utils.ItemBuilder;
import jaime.ge.gameEvent.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static jaime.ge.gameEvent.utils.ItemUtils.getMaterialsFromStringList;
import static jaime.ge.gameEvent.utils.MessageUtils.colorize;

@Getter
public class SimonSays extends Game implements ControllableGame {

    private final GameEvents plugin;

    private final List<Order> availableOrders;
    private final String prefix;
    private final List<String> fakePrefixes;
    private final int midGame;
    private final int lateGame;
    private final int baseTimer;
    private final int lateGameTimer;
    private final List<Material> itemsToCraft;
    private final List<Material> blocksToInteractWith;
    private final int totalRounds;
    private final Map<Order, String> orderMessages;

    private BukkitRunnable orderTimingTask;
    private boolean orderTimingTaskRunning;

    private BukkitRunnable fakeOrderTimingTask;
    private boolean fakeOrderTimingTaskRunning;


    private Material activeMaterial;
    private Player activePlayer;
    private Order activeOrder;
    private List<GamePlayer> playersWithCompletionPending;
    private boolean fake;
    private int round;
    private final Map<Material, List<ItemStack>> ingredientsToCraftItems;

    private final String midGameMessage;
    private final String lateGameMessage;

    @Getter
    private ItemStack jumpManualItem;
    @Getter
    private ItemStack craftManualItem;
    @Getter
    private ItemStack sneakManualItem;
    @Getter
    private ItemStack clickManualItem;
    private GamePlayer controller;

    private boolean manualMode;


    public SimonSays(SimonSaysGameFile gameFile, GameEvents plugin) {
        super(gameFile);
        this.plugin = plugin;

        availableOrders = loadOrders(gameFile.getAvailableOrders());
        prefix = gameFile.getOrderPrefix();
        fakePrefixes = new ArrayList<>(gameFile.getFakeOrderPrefixes());

        midGame = gameFile.getMidGame();
        lateGame = gameFile.getLateGame();
        baseTimer = gameFile.getBaseTimer();
        lateGameTimer = gameFile.getLateGameTimer();

        orderMessages = gameFile.getOrderMessages();

        itemsToCraft = getMaterialsFromStringList(gameFile.getItemsToCraft(), "Simon Says");
        ingredientsToCraftItems = new HashMap<>();
        saveItemsNecessaryToCraft();

        blocksToInteractWith = getMaterialsFromStringList(gameFile.getBlocksToInteract(), "Simon Says");
        totalRounds = gameFile.getTotalRounds();

        midGameMessage = plugin.getMessagesFile().getMessage(MessageName.ss_message_mid_game);
        lateGameMessage = plugin.getMessagesFile().getMessage(MessageName.ss_message_late_game);

        playersWithCompletionPending = new ArrayList<>();

        loadManualItems();

    }

    private void loadManualItems() {
        // TODO proximamente items custom

        craftManualItem = ItemBuilder.of(Material.MACE)
                .name("&eCraft item &7(Random)")
                .hideAll()
                .build();

        jumpManualItem = ItemBuilder.of(Material.SLIME_BLOCK)
                .name("&aJump")
                .hideAll()
                .build();

        sneakManualItem = ItemBuilder.of(Material.GOLDEN_LEGGINGS)
                .name("&eSneak")
                .hideAll()
                .build();

        clickManualItem = ItemBuilder.of(Material.BREEZE_ROD)
                .name("&bClick &7(Block/Player)")
                .hideAll()
                .build();

    }

    private void saveItemsNecessaryToCraft() {
        new BukkitRunnable(){
            @Override
            public void run() {
                for(Material material : itemsToCraft){
                    if(ingredientsToCraftItems.containsKey(material)) continue;
                    ingredientsToCraftItems.put(material, ItemUtils.getCraftingIngredients(material));

                    if(plugin.getConfigFile().isDebug()){
                        Bukkit.getConsoleSender().sendMessage(colorize("&aGame Event > Saved recipe for: " + material
                                +". Recipe: " + ingredientsToCraftItems.get(material)));
                    }

                }
            }
        }.runTask(plugin);
    }

    private List<Order> loadOrders(List<String> availableOrders) {
        List<Order> orders = new ArrayList<>();
        for(String s:availableOrders){
            for(Order order : Order.values()){
                if(order.toString().equalsIgnoreCase(s)){
                    orders.add(order);
                    break;
                }
            }
        }

        if(orders.isEmpty()){
            Bukkit.getConsoleSender().sendMessage(colorize("&fGameEvents > &c&lNo orders could be loaded for Simon Says!"));
            Bukkit.getConsoleSender().sendMessage(colorize("&c&lGame will shut down. Please disable it until orders are configured."));
            stop();
            return null;
        }

        if(plugin.getConfigFile().isDebug()){
            Bukkit.getConsoleSender().sendMessage(colorize("&fGameEvents > &a&lOrders loaded: " + orders));
        }
        return orders;
    }

    @Override
    protected void start(GameEvents plugin) {
        round = 0;

        giveControllerItems();
        if(manualMode) return;
        newOrder();
    }

    public int calcularTemporizador(int base, int rondaActual, int rondasTotales) {
        if (rondaActual < 1 || rondaActual > rondasTotales || base <= 0 || rondasTotales == 1) {
            throw new IllegalArgumentException("Parámetros inválidos.");
        }

        // Calcula un factor decreciente entre 1 (en la primera ronda) y cercano a 0 (en la última ronda)
        double factor = 1.0 - ((double)(rondaActual - 1) / (rondasTotales - 1));

        // Aplica el factor al temporizador base y lo redondea a int
        return (int) Math.max(1, Math.round(base * factor)); // mínimo de 1 para que nunca sea 0
    }
    private int getTimerBasedOnStage(){
        if(round < midGame){
            return calcularTemporizador(baseTimer, round, midGame - 1);
        }
        if(round < lateGame){
            return 0;
        }

        return calcularTemporizador(lateGameTimer, round, 50);


    }
    private void startTimer(){
        if(orderTimingTaskRunning) return;
        orderTimingTask = new BukkitRunnable() {
            int timer = getTimerBasedOnStage();
            @Override
            public void run() {
                timer--;
                playSoundToAll("BLOCK_LEVER_CLICK", 0.6f, 1.75f);
                sendMessageToAll(  "&c⚠ &7" + timer, null);
                if(timer == 0){
                    eliminatePendingPlayers();
                    cancel();
                    activeOrder = null;
                    orderTimingTaskRunning = false;
                }
            }
        };
        orderTimingTask.runTaskTimer(plugin, 0, 20L);
        orderTimingTaskRunning = true;
    }

    private void startFakeOrderTimer() {
        if(fakeOrderTimingTaskRunning) return;
        fakeOrderTimingTask = new BukkitRunnable() {
            int timer = getTimerBasedOnStage();
            @Override
            public void run() {
                timer--;
                playSoundToAll("BLOCK_LEVER_CLICK", 0.6f, 1.75f);
                sendMessageToAll(  "&c⚠ &7" + timer, null);
                if(timer == 0){
                    cancel();
                    fakeOrderTimingTaskRunning = false;
                    activeOrder = null;

                    if(manualMode) return;
                    newOrder();
                }
            }
        };
        fakeOrderTimingTask.runTaskTimer(plugin, 0, 20L);
        fakeOrderTimingTaskRunning = true;
    }
    public void stopTimerTasks(){
        if(orderTimingTask != null) {
            orderTimingTask.cancel();
        }
        orderTimingTaskRunning = false;

        if(fakeOrderTimingTask != null) {
            fakeOrderTimingTask.cancel();
        }
        fakeOrderTimingTaskRunning = false;
        activeOrder = null;
    }
    public void evaluateTimer(){
        // Se llama cuando se crea una nueva orden, cuando un jugador completa
        // una orden, y cuando se elimina un jugador por cualquier motivo
        if(orderTimingTaskRunning || fakeOrderTimingTaskRunning){
            return;
        }

        if(plugin.getConfigFile().isDebug()){
            Bukkit.getConsoleSender().sendMessage(colorize("&eEvaluating: Round " + round +", Midgame: "
                    + midGame + ", Lategame: " + lateGame));
        }

        if(round < midGame){
            // Cuando estamos en el early game, solo al último jugador
            // le cuenta el temporizador.
            if(playersWithCompletionPending.size() == 1){
                startTimer();
            }
            return;
        }
        // Cuando estamos en el mid game, no hay temporizador
        // El último en completar es eliminado
        if(round < lateGame){
            if(playersWithCompletionPending.size() == 1){
                eliminatePlayer(playersWithCompletionPending.getFirst());
                return;
            }
        }

        if(round > midGame){
            // Cuando estamos en él late game, el temporizador empieza
            // a la primera que alguien completa una orden y también muere el último que lo completa
            if(playersWithCompletionPending.size() == playerCount() - 1){
                startTimer();
            }
            if(playersWithCompletionPending.size() == 1){
                eliminatePlayer(playersWithCompletionPending.getFirst());
            }
        }
    }



    private void eliminatePendingPlayers() {
        for(GamePlayer player : new ArrayList<>(playersWithCompletionPending)){
            eliminatePlayer(player);
        }
    }

    public void newOrder(){
        stopTimerTasks();
        round++;
        sendStageMessage();
        playersWithCompletionPending = new ArrayList<>(getPlayers());
        activePlayer = null;
        activeMaterial = null;

        if(playerCount() <= 2){
            availableOrders.remove(Order.CLICK_PLAYER);
        }

        Collections.shuffle(availableOrders);
        activeOrder = availableOrders.getFirst();
        resetAndTryToFakeOrder();
        updateOrderTarget();

        if(fake){
            startFakeOrderTimer();
        }

        if(activeOrder.equals(Order.CRAFT_ITEM)){
            giveNecessaryItemsToCraft();
        }


        NewOrderEvent event = new NewOrderEvent(activeOrder, fake, this);
        event.callEvent();
        sendMessageToAll((fake ? fakePrefixes.getFirst() : prefix)
                + orderMessages.get(activeOrder)
                        .replace("%item%", activeMaterial == null ? "": activeMaterial.toString())
                , activePlayer);
    }

    public boolean newManualOrder(Order order, Object target, boolean fakeOrder){
        if(activeOrder != null) return false;
        if(order == Order.CLICK_PLAYER && playerCount() <= 2){
            if(plugin.getConfigFile().isDebug()){
                Bukkit.getConsoleSender().sendMessage(colorize("&cFail reason: CLICK_PLAYER and 2 or less players"));
            }
            return false;
        }
        stopTimerTasks();
        round++;
        sendStageMessage();
        playersWithCompletionPending = new ArrayList<>(getPlayers());
        activePlayer = null;
        activeMaterial = null;
        if(!tryUpdateManualTarget(order, target)){
            return false;
        }
        activeOrder = order;
        fake = fakeOrder;
        if(fake){
            startFakeOrderTimer();
        }

        if(activeOrder.equals(Order.CRAFT_ITEM)){
            giveNecessaryItemsToCraft();
        }
        NewOrderEvent event = new NewOrderEvent(activeOrder, fake, this);
        event.callEvent();
        sendMessageToAll((fake ? fakePrefixes.getFirst() : prefix)
                        + orderMessages.get(activeOrder)
                        .replace("%item%", activeMaterial == null ? "": activeMaterial.toString())
                , activePlayer);

        return true;
    }

    private boolean tryUpdateManualTarget(Order order, Object target) {
        return switch (order){
            case CRAFT_ITEM -> {
                Collections.shuffle(itemsToCraft);
                activeMaterial = itemsToCraft.getFirst();
                yield true;
            }
            case CLICK_PLAYER -> {
                if(target instanceof Player player){
                    activePlayer = player;
                    yield true;
                }

                yield false;
            }
            case RIGHT_CLICK_BLOCK -> {

                if(target instanceof Material material){

                    activeMaterial = material;
                    yield true;

                }

                yield false;
            }

            case null, default -> true;
        };
    }

    private void sendStageMessage() {
        if(round == midGame){
            sendMessageToAll(midGameMessage, null);
        }

        if(round == lateGame){
            sendMessageToAll(lateGameMessage, null);
        }
    }



    private void giveNecessaryItemsToCraft() {
        List<ItemStack> ingredients = ingredientsToCraftItems.get(activeMaterial);
        if(ingredients.isEmpty()){
            Bukkit.getConsoleSender().sendMessage(colorize("&cGameEvents > There was an error gathering crafting" +
                    "ingredientes for: " + activeMaterial + ". Craft item order skipped."));
            newOrder();
            return;
        }
        for(GamePlayer gamePlayer : playersWithCompletionPending){
            gamePlayer.getPlayer().getInventory().addItem(ingredients.toArray(new ItemStack[0]));
        }
    }

    public void completeOrder(GamePlayer gamePlayer){
        if(!playersWithCompletionPending.contains(gamePlayer)) return;
        playersWithCompletionPending.remove(gamePlayer);

        if(fake){
            eliminatePlayer(gamePlayer);
        } else {
            playSoundToPlayer(gamePlayer, "ENTITY_PLAYER_LEVELUP", 1F, 1.5F);
        }

        PlayerCompleteOrderEvent completeOrderEvent = new PlayerCompleteOrderEvent(activeOrder, fake, gamePlayer);
        completeOrderEvent.callEvent();
        evaluateTimer();

        if(activeOrder.equals(Order.CRAFT_ITEM)){
            gamePlayer.getPlayer().getInventory().clear();
        }

        if(playersWithCompletionPending.isEmpty()){
            stopTimerTasks();
        }

        if(manualMode) return;
        evaluateConditionsForNextOrder();
    }

    public void evaluateConditionsForNextOrder(){
        if(!inGame()) return;
        if(playersWithCompletionPending.isEmpty()){
            newOrder();
        }
    }
    private void resetAndTryToFakeOrder() {
        fake = false;
        Random random = new Random();
        int probabilidad = random.nextInt(100);
        if(probabilidad < 15){
            fake = true;
        }
    }

    private void updateOrderTarget() {
        switch (activeOrder){
            case CRAFT_ITEM -> {
                Collections.shuffle(itemsToCraft);
                activeMaterial = itemsToCraft.getFirst();
            }
            case CLICK_PLAYER -> {
                Random random = new Random();
                GamePlayer randomGamePlayer = getPlayers().get(random.nextInt(getPlayers().size()));
                activePlayer = randomGamePlayer.getPlayer();
                playersWithCompletionPending.remove(randomGamePlayer);
            }
            case RIGHT_CLICK_BLOCK -> {
                Collections.shuffle(blocksToInteractWith);
                activeMaterial = blocksToInteractWith.getFirst();
            }
        }
    }



    @Override
    public void stop() {
        playersWithCompletionPending.clear();
        stopTimerTasks();
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), true, null);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void stop(GamePlayer winner) {
        playersWithCompletionPending.clear();
        stopTimerTasks();
        GameFinishEvent event = new GameFinishEvent(this, new ArrayList<>(getPlayers())
                , new ArrayList<>(getSpectators()), false, winner);
        event.callEvent();
        setStatus(Status.FINISHING);
    }

    @Override
    public void eliminatePlayer(GamePlayer gamePlayer) {
        if(gamePlayer.getGame() != this) return;
        playSoundToPlayer(gamePlayer, "ENTITY_GENERIC_HURT", 1f, 1f);
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.setAlive(false);
        addSpectator(gamePlayer);
        playersWithCompletionPending.remove(gamePlayer);

        if(playersWithCompletionPending.isEmpty() || playersWithCompletionPending.size() == 1){
            checkConditionsToEndByLeave();
        }
    }

    public boolean isOrderActive(Order order){
        if(activeOrder == null) return false;
        return activeOrder.equals(order);
    }

    @Override
    public boolean hasController() {
        return controller != null;
    }

    @Override
    public boolean isManualMode() {
        return manualMode;
    }

    @Override
    public boolean playerCanBeController(Player player) {
        return (player.hasPermission("ge.manual.*")
                || player.hasPermission("ge.manual.ss")) && !hasController();
    }

    @Override
    public GamePlayer getController() {
        return controller;
    }

    @Override
    public void addController(GamePlayer gamePlayer) {
        if(hasController()) return;
        removePlayer(gamePlayer.getPlayer());
        controller = gamePlayer;
        manualMode = true;
        gamePlayer.setAlive(false);
        gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
        gamePlayer.getPlayer().setAllowFlight(true);
    }

    @Override
    public void removeController(GameEvents plugin) {
        if(!hasController()) return;

        manualMode = false;
        controller.getPlayer().setAllowFlight(false);

        // Si el juego ya empezó, el controlador directamente sale del juego
        // y se reactiva el modo automático.
        if(inGame()){
            if(getAllPlayers().contains(controller))
                plugin.getGameManager().removePlayer(controller.getPlayer());
            controller = null;
            if(activeOrder == null) newOrder();
            return;
        }

        controller.setAlive(true);
        addPlayer(controller);
        controller.getPlayer().setGameMode(GameMode.ADVENTURE);

        controller = null;
    }

    @Override
    public boolean isController(GamePlayer gamePlayer) {
        return controller == gamePlayer;
    }

    @Override
    public void giveControllerItems() {
        if(!hasController()) return;
        Player player = controller.getPlayer();

        player.getInventory().remove(Material.COMMAND_BLOCK);
        player.getInventory().setItem(0, jumpManualItem);
        player.getInventory().setItem(1, sneakManualItem);
        player.getInventory().setItem(2, craftManualItem);
        player.getInventory().setItem(3, clickManualItem);
    }


    @Override
    protected List<GamePlayer> getExtraPlayers() {
        return controller == null ? Collections.emptyList() : List.of(controller);
    }
}
