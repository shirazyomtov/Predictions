package world.entity.instance;

import world.entity.instance.location.Location;
import world.propertyInstance.api.Property;

import java.io.Serializable;
import java.util.Map;

public class EntityInstance implements Serializable {

    private final String name;
    private final Map<String, Property> allProperty;

    private Location location;

    public EntityInstance(String name, Map<String, Property> allProperty, Location location) {
        this.name = name;
        this.allProperty = allProperty;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Map<String, Property> getAllProperty() {
        return allProperty;
    }

    public Object getPropertyValue(String propertyName, boolean propertyValue){
        if(allProperty.containsKey(propertyName)){
                if (propertyValue) {
                    return allProperty.get(propertyName).getValue();
                }
                else{
                    return allProperty.get(propertyName).getTimeTheValueDosentChange();
                }
        }

        return null;
    }

    public Location getLocation() {
        return location;
    }
}
