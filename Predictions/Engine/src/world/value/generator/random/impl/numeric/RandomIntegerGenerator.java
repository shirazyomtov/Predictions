package world.value.generator.random.impl.numeric;

public class RandomIntegerGenerator extends AbstractNumericRandomGenerator<Integer> {

    public RandomIntegerGenerator(Integer from, Integer to) {
        super(from, to);
    }

    @Override
    public Integer generateValue() {
        if(from !=null && to != null) {
            return random.nextInt(to - from + 1) + from;
        }
        else{
            return random.nextInt();
        }
    }
}
