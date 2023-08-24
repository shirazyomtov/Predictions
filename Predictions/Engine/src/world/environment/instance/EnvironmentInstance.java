package world.environment.instance;

import world.propertyInstance.api.AbstractPropertyInstance;
import world.propertyInstance.api.Property;

import java.io.Serializable;

public class EnvironmentInstance implements Serializable {
    private final Property property;

    public EnvironmentInstance(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }
}
