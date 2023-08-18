package world.auxiliaryFunctions;

import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import exceptions.OperationNotSupportedType;
import world.enums.Type;
import world.environment.instance.EnvironmentInstance;
import world.value.generator.random.impl.numeric.RandomIntegerGenerator;
import world.worldInstance.WorldInstance;

public final class AuxiliaryFunctionsImpl {

    public static Object environment(String environmentName, WorldInstance worldInstance, Type type) throws ObjectNotExist, OperationNotCompatibleTypes {
        if (worldInstance.getEnvironmentInstanceMap().containsKey(environmentName)) {
            Type typeEnvironment = worldInstance.getEnvironmentInstanceMap().get(environmentName).getProperty().getType();
            if (checkSameType(typeEnvironment, type)) {
                return worldInstance.getEnvironmentInstanceMap().get(environmentName).getProperty().getValue();
            }
            else {
                throw new OperationNotCompatibleTypes(typeEnvironment.toString(), type.toString());
            }
        }
        else {
            throw new ObjectNotExist(environmentName, "environment");
        }
    }

    private static boolean checkSameType(Type environment, Type type) {
        if ((type.equals(Type.DECIMAL) || (type.equals(Type.FLOAT))) && (environment.equals(Type.DECIMAL) || (environment.equals(Type.FLOAT)))) {
            return  true;
        }
        return environment.equals(type);
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
