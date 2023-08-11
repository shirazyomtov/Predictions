package world.entity.instance;

import world.propertyInstance.api.Property;

import java.util.Map;

public class EntityInstance {

    private final String name;
    private final Map<String, Property> allProperty;

    private final int count;

    public EntityInstance(String name, Map<String, Property> allProperty, int count) {
        this.name = name;
        this.allProperty = allProperty;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public Map<String, Property> getAllProperty() {
        return allProperty;
    }

    public int getCount() {
        return count;
    }
}
