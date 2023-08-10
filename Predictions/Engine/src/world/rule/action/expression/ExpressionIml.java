package world.rule.action.expression;

import exceptions.ObjectNotExist;
import history.History;
import world.auxiliaryFunctions.AuxiliaryFunctionsImpl;
import world.entity.instance.EntityInstance;
import world.propertyInstance.api.Property;
import world.worldInstance.WorldInstance;

import static world.enums.AuxiliaryFunction.*;

public class ExpressionIml implements Expression {
    private final String expressionName;

    public ExpressionIml(String expressionName) {
        this.expressionName = expressionName;
    }

    @Override
    public Object decipher(String entityName) throws ObjectNotExist, NumberFormatException {
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
        else if (checkIfValueIsProperty(entityName) != null) {
            object = checkIfValueIsProperty(entityName);
        }
        else {
            object = expressionName;
        }
        return object;
    }


    private Object checkIfValueIsProperty(String entityName) {
        WorldInstance world = History.getInstance().getSimulation().getWorldInstance();
        for(EntityInstance entityInstance: world.getEntityInstanceList()){
            if(entityInstance.getName().equals(entityName)){
                    if(entityInstance.getAllProperty().containsKey(expressionName)){
                        return entityInstance.getAllProperty().get(expressionName).generateValue();
                    }
                }
            }

        return null;
    }

}
