package world.propertyInstance.impl;

import world.enums.Type;
import world.propertyInstance.api.AbstractPropertyInstance;
import world.range.RangeImpl;
import world.value.generator.api.ValueGenerator;

import java.io.Serializable;

public class BooleanPropertyInstance extends AbstractPropertyInstance<Boolean> implements Serializable {
    public BooleanPropertyInstance(String name, ValueGenerator<Boolean> valueGenerator, RangeImpl range) {
        super(name, Type.BOOLEAN, valueGenerator, range);
    }
}
