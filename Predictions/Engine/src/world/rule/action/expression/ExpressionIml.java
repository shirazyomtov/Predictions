package world.rule.action.expression;

import exceptions.ObjectNotExist;
import world.auxiliaryFunctions.AuxiliaryFunctionsImpl;
import world.entity.instance.EntityInstance;
import world.worldInstance.WorldInstance;

import java.io.Serializable;

import static world.enums.AuxiliaryFunction.*;

public class ExpressionIml implements Expression, Serializable {
    private final String expressionName;

    public ExpressionIml(String expressionName) {
        this.expressionName = expressionName;
    }

    @Override
    public String decipher(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, NumberFormatException {
        String object = null ;
        if (checkOptionByFunctionName(expressionName)) {
            int index = expressionName.indexOf("(");
            String functionName = expressionName.substring(0, index).trim();
            String value = expressionName.substring(index + 1, expressionName.length() - 1).trim();
            if (functionName.equals(ENVIRONMENT.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.environment(value, worldInstance).toString();
            }
            else if (functionName.equals(RANDOM.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.random(value).toString();
            }
        }
        else if (checkIfValueIsProperty(entity) != null) {
            object = checkIfValueIsProperty(entity).toString();
        }
        else {
            object = expressionName;
        }
        return object;
    }


    private Object checkIfValueIsProperty(EntityInstance entity) {
        if(entity.getAllProperty().containsKey(expressionName)){
            return entity.getAllProperty().get(expressionName).getValue();
        }
        return null;
    }

}
