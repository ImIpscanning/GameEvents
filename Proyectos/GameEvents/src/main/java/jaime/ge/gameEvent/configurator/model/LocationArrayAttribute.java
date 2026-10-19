package jaime.ge.gameEvent.configurator.model;

import lombok.Getter;
import org.bukkit.Location;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

@Getter
public class LocationArrayAttribute extends ConfigurableAttribute<Location[]>{

    private final int desiredSize;

    public LocationArrayAttribute(String name, Location[] initialValue) {
        super(name, Location[].class, initialValue);
        this.desiredSize = initialValue.length;
    }

    public LocationArrayAttribute(String name, Location[] initialValue, int desiredSize) {
        super(name, Location[].class, initialValue);
        this.desiredSize = desiredSize;
    }

    @Override
    public String toString(){
        return getValue().length + " locations";
    }


    public Location get(int index){
        return getValue()[index];
    }

    @Override
    public boolean isConfigured(){
        if(getValue() == null) return false;
        for(Location location : getValue()){
            if(location == null) return false;
        }
        return true;
    }

    public void setValue(List<Location> value){
        setValue(value.toArray(value.toArray(new Location[0])));
    }
}
