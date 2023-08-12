package world.propertyInstance.impl;

import world.enums.Type;
import world.propertyInstance.api.AbstractPropertyInstance;
import world.range.RangeImpl;
import world.value.generator.api.ValueGenerator;

public class FloatPropertyInstance extends AbstractPropertyInstance<Float> {

    public FloatPropertyInstance(String name, ValueGenerator<Float> value, RangeImpl range)
    {
        super(name, Type.FLOAT, value, range);
    }
}
