package jaime.ge.gameEvent.configurator.model;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class ConfigurableAttribute<T> {
    @Getter
    private final String name;
    @Getter
    private final Class<T> type;
    @Getter
    private T value;

    protected boolean isConfigured = false;

    public ConfigurableAttribute(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public ConfigurableAttribute(String name, Class<T> type, T initialValue) {
        this.name = name;
        this.type = type;
        this.value = initialValue;
        if(initialValue != null){
            this.isConfigured = true;
        }

        if(type.isArray()){
            checkAllElements();
        }
    }

    private void checkAllElements() {
        Object[] objects = (Object[]) getValue();
        if (objects == null || Arrays.stream(objects).anyMatch(Objects::isNull)) {
            reset();
        }
    }


    public void setValue(T value) {
        this.value = value;
        this.isConfigured = true;
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    public void reset() {
        this.value = null;
        this.isConfigured = false;
    }

    @Override
    public String toString() {
        return isConfigured ? value.toString() : "Not Configured";
    }

    public boolean isString(){
        return type.equals(String.class);
    }

    public boolean isInteger(){
        return type.equals(Integer.class);
    }

    public boolean isBoolean(){
        return type.equals(Boolean.class);
    }

    public boolean isItem(){return type.equals(ItemStack.class);}

    public boolean isInventory(){ return type.equals(ItemStack[].class);}

    public boolean isMultipleInventory(){return type.equals(MultiInventory.class);}


}

