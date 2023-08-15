package world.propertyInstance.impl;

import world.enums.Type;
import world.propertyInstance.api.AbstractPropertyInstance;
import world.range.RangeImpl;
import world.value.generator.api.ValueGenerator;

import java.io.Serializable;

public class StringPropertyInstance extends AbstractPropertyInstance<String> implements Serializable {

    public StringPropertyInstance(String name, ValueGenerator<String> valueGenerator, RangeImpl range) {
        super(name, Type.STRING, valueGenerator, range);
    }

}