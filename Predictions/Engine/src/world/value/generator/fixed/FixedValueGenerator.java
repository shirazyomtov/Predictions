package world.value.generator.fixed;

import world.value.generator.api.ValueGenerator;

import java.io.Serializable;

public class FixedValueGenerator<T> implements ValueGenerator<T>, Serializable {

    private final T fixedValue;

    public FixedValueGenerator(T fixedValue) {
        this.fixedValue = fixedValue;
    }

    @Override
    public T generateValue() {
        return fixedValue;
    }
}
