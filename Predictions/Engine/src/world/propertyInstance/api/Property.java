package world.propertyInstance.api;

import world.enums.Type;

public interface Property {
    String getName();
    Type getType();
    Object generateValue();
}