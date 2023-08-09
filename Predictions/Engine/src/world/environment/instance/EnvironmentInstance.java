package world.environment.instance;

import world.propertyInstance.api.AbstractPropertyInstance;
import world.propertyInstance.api.Property;

public class EnvironmentInstance {
    private final Property property;

    public EnvironmentInstance(Property property) {
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }

    @Override
    public String toString() {
        return "EnvironmentInstance{" +
                "property=" + property +
                '}';
    }
}
