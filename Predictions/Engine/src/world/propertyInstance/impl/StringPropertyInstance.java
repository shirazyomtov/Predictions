package world.propertyInstance.impl;

import world.enums.Type;
import world.propertyInstance.api.AbstractPropertyInstance;
import world.value.generator.api.ValueGenerator;

public class StringPropertyInstance extends AbstractPropertyInstance<String> {

    public StringPropertyInstance(String name, ValueGenerator<String> valueGenerator) {
        super(name, Type.STRING, valueGenerator);
    }

}