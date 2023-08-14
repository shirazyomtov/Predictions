package world.propertyInstance.impl;

import org.w3c.dom.ranges.Range;
import world.enums.Type;
import world.propertyInstance.api.AbstractPropertyInstance;
import world.range.RangeImpl;
import world.value.generator.api.ValueGenerator;

import java.io.Serializable;

public class IntegerPropertyInstance extends AbstractPropertyInstance<Integer> implements Serializable {

    public IntegerPropertyInstance(String name, ValueGenerator<Integer> valueGenerator, RangeImpl range) {
        super(name, Type.DECIMAL, valueGenerator, range);
    }

}
