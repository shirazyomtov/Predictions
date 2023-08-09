package world.entity.instance;

import world.propertyInstance.api.Property;

import java.util.List;

public class EntityInstance {

    private final String name;
    private final List<Property> allProperty;
    public EntityInstance(String name, List<Property> allProperty) {
        this.name = name;
        this.allProperty = allProperty;
    }

    public String getName() {
        return name;
    }
}
