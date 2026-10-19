package jaime.ge.gameEvent.configurator.model;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MultiInventoryAttribute extends ConfigurableAttribute<MultiInventory>{
    public MultiInventoryAttribute(String name, MultiInventory multiInventory) {
        super(name, MultiInventory.class, multiInventory);
        checkConfigured();
    }

    @Override
    public String toString(){
        return getValue().getInventories().size() + " inventories";
    }


    public void addInventory(ItemStack[] contents){
        getValue().addInventory(contents);
        checkConfigured();
    }

    public void removeInventory(int index){
        getValue().remove(index);
        checkConfigured();
    }

    public ItemStack[] removeLast(){
        ItemStack[] item = getValue().removeLast();
        checkConfigured();
        return item;
    }

    private void checkConfigured() {
        this.isConfigured = !getValue().isEmpty();
    }

}
