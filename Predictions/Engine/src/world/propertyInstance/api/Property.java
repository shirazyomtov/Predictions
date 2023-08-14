package world.propertyInstance.api;

import world.enums.Type;
import world.value.generator.api.ValueGenerator;

import java.io.Serializable;

public interface Property extends Serializable {
    String getName();
    Type getType();
    Object getValue();
    void setValue(Object val);
    Object generateValue();
}