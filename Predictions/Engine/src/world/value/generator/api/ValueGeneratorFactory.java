package world.value.generator.api;

import world.value.generator.fixed.FixedValueGenerator;
import world.value.generator.random.impl.bool.RandomBooleanValueGenerator;
import world.value.generator.random.impl.numeric.RandomFloatGenerator;
import world.value.generator.random.impl.numeric.RandomIntegerGenerator;
import world.value.generator.api.ValueGenerator;
import world.value.generator.random.impl.string.RandomStringValueGenerator;

public interface ValueGeneratorFactory {

    static <T> ValueGenerator<T> createFixed(T value) {
        return new FixedValueGenerator<>(value);
    }

    static ValueGenerator<Boolean> createRandomBoolean() {
        return new RandomBooleanValueGenerator();
    }

    static ValueGenerator<Integer> createRandomInteger(Integer from, Integer to) {
        return new RandomIntegerGenerator(from, to);
    }

    static ValueGenerator<Float> createRandomFloat(Float from, Float to) {
        return new RandomFloatGenerator(from, to);
    }

    static ValueGenerator<String> createRandomString() {
        return new RandomStringValueGenerator();
    }

}
