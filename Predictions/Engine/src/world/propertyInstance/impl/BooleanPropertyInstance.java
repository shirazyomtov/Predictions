package world.propertyInstance.impl;

import world.enums.Type;
import world.propertyInstance.api.AbstractPropertyInstance;
import world.range.RangeImpl;
import world.value.generator.api.ValueGenerator;

public class BooleanPropertyInstance extends AbstractPropertyInstance<Boolean> {
    public BooleanPropertyInstance(String name, ValueGenerator<Boolean> valueGenerator, RangeImpl range) {
        super(name, Type.BOOLEAN, valueGenerator, range);
    }
}
