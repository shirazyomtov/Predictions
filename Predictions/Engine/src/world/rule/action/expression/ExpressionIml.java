package world.rule.action.expression;

import exceptions.FormatException;
import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import world.auxiliaryFunctions.AuxiliaryFunctionsImpl;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.worldInstance.WorldInstance;

import java.io.Serializable;

import static world.enums.AuxiliaryFunction.*;

public class ExpressionIml implements Expression, Serializable {
    private final String expressionName;

    private final String propertyName;

    public ExpressionIml(String expressionName, String propertyName) {
        this.expressionName = expressionName;
        this.propertyName = propertyName;
    }

    @Override
    public String decipher(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, NumberFormatException, OperationNotCompatibleTypes, FormatException {
        String object = null ;
        if (checkOptionByFunctionName(expressionName)) {
            int index = expressionName.indexOf("(");
            String functionName = expressionName.substring(0, index).trim();
            String value = expressionName.substring(index + 1, expressionName.length() - 1).trim();
            if (functionName.equals(ENVIRONMENT.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.environment(value, worldInstance, getTypeOfProperty(entity, propertyName)).toString();
            }
            else if (functionName.equals(RANDOM.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.random(value).toString();
            }
            else if (functionName.equals(EVALUATE.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.evaluate(value, worldInstance, getTypeOfProperty(entity, propertyName)).toString();
            }
            else if (functionName.equals(PERCENT.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.percent(value, entity, worldInstance, propertyName).toString();
            }
            else if (functionName.equals(TICKS.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.ticks(value, worldInstance).toString();
            }
        }
        else if (checkIfValueIsProperty(entity) != null) {
            Type propertyType = getTypeOfProperty(entity, propertyName);
            Type valueType = getTypeOfProperty(entity, expressionName);
            if(checkSameType(propertyType, valueType)) {
                object = checkIfValueIsProperty(entity).toString();
            }
            else{
                throw new OperationNotCompatibleTypes(propertyType.toString(), valueType.toString());
            }
        }
        else {
            object = expressionName;
        }
        return object;
    }

    private Type getTypeOfProperty(EntityInstance entity, String property) {
        for(Property propertyInstance: entity.getAllProperty().values()){
            if(propertyInstance.getName().equals(property)){
                return propertyInstance.getType();
            }
        }
        return null;
    }

    private static boolean checkSameType(Type propertyType, Type valueType) {
        if ((propertyType.equals(Type.DECIMAL) || (propertyType.equals(Type.FLOAT))) && (valueType.equals(Type.DECIMAL) || (valueType.equals(Type.FLOAT)))) {
            return  true;
        }
        return propertyType.equals(valueType);
    }


    private Object checkIfValueIsProperty(EntityInstance entity) {
        return entity.getPropertyValue(expressionName, true);
    }

    public String getExpressionName() {
        return expressionName;
    }
}
