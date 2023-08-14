package world.value.generator.random.impl.bool;

import world.value.generator.random.api.AbstractRandomValueGenerator;

import java.io.Serializable;

public class RandomBooleanValueGenerator extends AbstractRandomValueGenerator<Boolean> implements Serializable {

    @Override
    public Boolean generateValue() {
        return random.nextDouble() < 0.5;
    } // check
}
