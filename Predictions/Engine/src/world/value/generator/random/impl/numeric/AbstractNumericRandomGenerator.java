package world.value.generator.random.impl.numeric;

import world.value.generator.random.api.AbstractRandomValueGenerator;

import java.io.Serializable;

public abstract class AbstractNumericRandomGenerator<T> extends AbstractRandomValueGenerator<T> implements Serializable {

    protected final T from;
    protected final T to;

    protected AbstractNumericRandomGenerator(T from, T to) {
        this.from = from;
        this.to = to;
    }

}
