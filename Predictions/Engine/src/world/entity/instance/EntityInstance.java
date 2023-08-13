package world.entity.instance;

import world.propertyInstance.api.Property;

import java.util.Map;

public class EntityInstance {

    private final String name;
    private final Map<String, Property> allProperty;


    public EntityInstance(String name, Map<String, Property> allProperty) {
        this.name = name;
        this.allProperty = allProperty;
    }

    public String getName() {
        return name;
    }

    public Map<String, Property> getAllProperty() {
        return allProperty;
    }
}
