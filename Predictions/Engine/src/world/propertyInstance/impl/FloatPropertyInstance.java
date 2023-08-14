package world.propertyInstance.impl;

import world.enums.Type;
import world.propertyInstance.api.AbstractPropertyInstance;
import world.range.RangeImpl;
import world.value.generator.api.ValueGenerator;

import java.io.Serializable;

public class FloatPropertyInstance extends AbstractPropertyInstance<Float> implements Serializable {

    public FloatPropertyInstance(String name, ValueGenerator<Float> value, RangeImpl range)
    {
        super(name, Type.FLOAT, value, range);
    }
}
