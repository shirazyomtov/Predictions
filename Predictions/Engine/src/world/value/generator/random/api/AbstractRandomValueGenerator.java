package world.value.generator.random.api;

import world.value.generator.api.ValueGenerator;

import java.util.Random;

public abstract class AbstractRandomValueGenerator<T> implements ValueGenerator<T> {
    protected final Random random;

    protected AbstractRandomValueGenerator() {
        random = new Random();
    }
}
