package world.auxiliaryFunctions;

import exceptions.FormatException;
import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.rule.action.expression.ExpressionIml;
import world.value.generator.random.impl.numeric.RandomIntegerGenerator;
import world.worldInstance.WorldInstance;

public final class AuxiliaryFunctionsImpl {

    public static Object environment(String value, WorldInstance worldInstance, Type type) throws ObjectNotExist, OperationNotCompatibleTypes {
        if (worldInstance.getEnvironmentInstanceMap().containsKey(value)) {
            Type typeEnvironment = worldInstance.getEnvironmentInstanceMap().get(value).getProperty().getType();
            if (checkSameType(typeEnvironment, type)) {
                return worldInstance.getEnvironmentInstanceMap().get(value).getProperty().getValue();
            }
            else {
                throw new OperationNotCompatibleTypes(typeEnvironment.toString(), type.toString());
            }
        }
        else {
            throw new ObjectNotExist(value, "environment");
        }
    }

    private static boolean checkSameType(Type environment, Type type) {
        if ((type.equals(Type.DECIMAL) || (type.equals(Type.FLOAT))) && (environment.equals(Type.DECIMAL) || (environment.equals(Type.FLOAT)))) {
            return  true;
        }
        return environment.equals(type);
    }

    public static Integer random(String value) throws NumberFormatException {
        try {
            Integer num = Integer.parseInt(value);
            RandomIntegerGenerator randomIntegerGenerator = new RandomIntegerGenerator(0, num);
            return randomIntegerGenerator.generateValue();
        } catch (Exception exception) {
            throw new NumberFormatException("You did not provide an integer value for the random function");
        }
    }

    public static Float percent(String value, EntityInstance entity, WorldInstance worldInstance, String propertyName) throws ObjectNotExist, OperationNotCompatibleTypes, FormatException, ClassCastException{
        int index = value.indexOf(",");
        String expression1 = value.substring(0, index).trim();
        String expression2 = value.substring(index + 1).trim();
        ExpressionIml expressionIml1 = new ExpressionIml(expression1, propertyName);
        ExpressionIml expressionIml2  = new ExpressionIml(expression2, propertyName);
        String value1 = expressionIml1.decipher(entity, worldInstance);
        String value2 = expressionIml2.decipher(entity, worldInstance);
        return (Float.parseFloat(value1) * Float.parseFloat(value2)) / 100;

    }

    public static Object evaluate(String value, WorldInstance worldInstance,  Type typePropertyName) throws FormatException, ObjectNotExist, OperationNotCompatibleTypes {
        return checkFormat(value, worldInstance, true, typePropertyName);
    }
    public static Integer ticks(String value,  WorldInstance worldInstance) throws FormatException, ObjectNotExist, ClassCastException, OperationNotCompatibleTypes {
       Object propertyValue = checkFormat(value, worldInstance, false, null);
       return (Integer) propertyValue;
    }


    private static Object checkFormat(String value, WorldInstance worldInstance, boolean propertyValueCheck, Type typePropertyName) throws FormatException, ObjectNotExist, OperationNotCompatibleTypes {
        int index = value.indexOf(".");
        if(index == -1){
            throw new FormatException();
        }
        String entity = value.substring(0, index).trim();
        String property = value.substring(index + 1).trim();
        if(typePropertyName != null){
            Type type = worldInstance.isEntityExists(entity).getAllProperty().get(property).getType();
            if(!checkSameType(typePropertyName, type)){
                throw new OperationNotCompatibleTypes(typePropertyName.toString(), type.toString());
            }
        }
        EntityInstance entityInstance = worldInstance.isEntityExists(entity);
        if (entityInstance != null) {
            Object propertyValue = entityInstance.getPropertyValue(property, propertyValueCheck);
            if(propertyValue != null){
                return propertyValue;
            }
            else {
                throw new ObjectNotExist(property, "property");
            }
        }
        else {
            throw new ObjectNotExist(entity, "entity");
        }
    }
}
