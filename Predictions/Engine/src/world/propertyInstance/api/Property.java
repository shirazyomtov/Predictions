package world.propertyInstance.api;

import world.enums.Type;
import world.value.generator.api.ValueGenerator;

public interface Property {
    String getName();
    Type getType();

    void setValue(Object value);
    Object generateValue();
//    void setValueGenerator(Object valueGenerator);
}