package jaime.ge.gameEvent.configurator.model;

import jaime.ge.gameEvent.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultiInventory {

    private final List<ItemStack[]> inventories;
    public MultiInventory(List<ItemStack[]> baseContents){
        inventories = new ArrayList<>();
        if(baseContents != null){
            inventories.addAll(baseContents);
        }
    }

    public boolean isEmpty(){
        return inventories.isEmpty();
    }

    protected void addInventory(ItemStack[] contents){
        inventories.add(contents);
    }

    public List<ItemStack[]> getInventories() {
        return List.copyOf(inventories.stream()
                .filter(Objects::nonNull) // 🔹 Filtra elementos nulos
                .toList()); // Convierte de nuevo a lista
    }

    protected ItemStack[] removeLast(){
        return inventories.removeLast();
    }

    protected void remove(int intdex){
        inventories.remove(intdex);
    }

    public ItemStack[] getInventory(int index){
        return inventories.get(index);
    }


    public static MultiInventory fromConfigurationSection(ConfigurationSection section){
        List<ItemStack[]> baseContents = new ArrayList<>();

        for(String inventory : section.getKeys(false)){
            ConfigurationSection inventorySection = section.getConfigurationSection(inventory);

            ItemStack[] contents = ItemUtils.deserializeInventory(inventorySection);
            baseContents.add(contents);
        }

        return new MultiInventory(baseContents);
    }


}
