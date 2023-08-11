package world.auxiliaryFunctions;

import exceptions.ObjectNotExist;
import history.History;
import world.value.generator.random.impl.numeric.RandomIntegerGenerator;
import world.worldInstance.WorldInstance;

public final class AuxiliaryFunctionsImpl {

    public static Object environment(String environmentName) throws ObjectNotExist {
        if (History.getInstance().getSimulation().getWorldInstance().getEnvironmentInstanceMap().containsKey(environmentName)) {
            return History.getInstance().getSimulation().getWorldInstance().getEnvironmentInstanceMap().get(environmentName).getProperty().getValue();
        } else {
            throw new ObjectNotExist(environmentName, "environment");
        }
    }

    public static Integer random(String number) throws NumberFormatException {
        try {
            Integer num = Integer.parseInt(number);
            RandomIntegerGenerator randomIntegerGenerator = new RandomIntegerGenerator(0, num);
            return randomIntegerGenerator.generateValue();
        } catch (Exception exception) {
            throw new NumberFormatException("You did not provide an integer value for the random function");
        }
    }
}
