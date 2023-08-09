package world.propertyInstance.impl;

import world.enums.Type;
import world.propertyInstance.api.AbstractPropertyInstance;
import world.value.generator.api.ValueGenerator;

public class IntegerPropertyInstance extends AbstractPropertyInstance<Integer> {

    public IntegerPropertyInstance(String name, ValueGenerator<Integer> valueGenerator) {
        super(name, Type.DECIMAL, valueGenerator);
    }

}
