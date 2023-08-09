package world.propertyInstance.impl;

import world.enums.Type;
import world.propertyInstance.api.AbstractPropertyInstance;
import world.value.generator.api.ValueGenerator;

public class BooleanPropertyInstance extends AbstractPropertyInstance<Boolean> {
    public BooleanPropertyInstance(String name, ValueGenerator<Boolean> valueGenerator) {
        super(name, Type.BOOLEAN, valueGenerator);
    }
}
