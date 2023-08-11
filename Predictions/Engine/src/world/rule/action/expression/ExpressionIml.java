package world.rule.action.expression;

import exceptions.ObjectNotExist;
import world.auxiliaryFunctions.AuxiliaryFunctionsImpl;
import world.entity.instance.EntityInstance;

import static world.enums.AuxiliaryFunction.*;

public class ExpressionIml implements Expression {
    private final String expressionName;

    public ExpressionIml(String expressionName) {
        this.expressionName = expressionName;
    }

    @Override
    public Object decipher(EntityInstance entity) throws ObjectNotExist, NumberFormatException {
        Object object = null ;
        if (checkOptionByFunctionName(expressionName)) {
            int index = expressionName.indexOf("(");
            String functionName = expressionName.substring(0, index).trim();
            String value = expressionName.substring(index + 1, expressionName.length() - 1).trim();
            if (functionName.equals(ENVIRONMENT.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.environment(value);
            }
            else if (functionName.equals(RANDOM.getFunctionName())) {
                object = AuxiliaryFunctionsImpl.random(value);
            }
        }
        else if (checkIfValueIsProperty(entity) != null) {
            object = checkIfValueIsProperty(entity);
        }
        else {
            object = expressionName;
        }
        return object;
    }


    private Object checkIfValueIsProperty(EntityInstance entity) {
        if(entity.getAllProperty().containsKey(expressionName)){
            return entity.getAllProperty().get(expressionName).generateValue();
        }
        return null;
    }

}
