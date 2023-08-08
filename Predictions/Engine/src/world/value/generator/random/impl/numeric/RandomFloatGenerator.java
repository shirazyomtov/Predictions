package world.value.generator.random.impl.numeric;

public class RandomFloatGenerator extends AbstractNumericRandomGenerator<Float> {

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
