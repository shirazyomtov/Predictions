package world.value.generator.random.impl.numeric;

import java.io.Serializable;

public class RandomFloatGenerator extends AbstractNumericRandomGenerator<Float> implements Serializable {

    public RandomFloatGenerator(Float from, Float to) {
        super(from, to);
    }

    @Override
    public Float generateValue() {
        if (from != null && to != null) {
            return from + random.nextFloat() * (to - from);
        }
        else{
            return random.nextFloat();
        }
    }
}
