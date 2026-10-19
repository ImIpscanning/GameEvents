package jaime.ge.gameEvent.configurator.model;

import java.util.ArrayList;
import java.util.List;

public class StringListAttribute extends ConfigurableAttribute<String[]>{

    private final List<String> list;

    public StringListAttribute(String name, List<String> initialValue) {
        super(name, String[].class, initialValue.toArray(new String[0]));
        list = new ArrayList<>(initialValue);
        checkConfigured();
    }

    private void checkConfigured() {
        if(list == null){
            isConfigured = false;
            return;
        }

        isConfigured = !list.isEmpty();
    }

    public void addString(String string){
        list.add(string);
        setValue(list.toArray(new String[0]));
    }

    public void removeLast(){
        list.removeLast();
        setValue(list.toArray(new String[0]));
    }

    @Override
    public String toString(){
        return String.join(", ", getList());
    }

    public List<String> getList(){
        return List.of(getValue());
    }
}
