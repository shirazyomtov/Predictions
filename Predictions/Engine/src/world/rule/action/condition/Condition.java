package world.rule.action.condition;

import exceptions.ObjectNotExist;
import exceptions.OperationNotSupportedType;
import jaxb.schema.generated.PRDCondition;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.enums.ComparisonOperator;
import world.propertyInstance.api.Property;
import world.rule.action.Action;
import world.rule.action.expression.ExpressionIml;

import java.util.List;

public class Condition  extends Action {

    private final PRDCondition condition;

    public Condition(String entityName, PRDCondition condition)
    {
        super(entityName, ActionType.CONDITION);
        this.condition = condition;
//        this.expression = new ExpressionIml(condition.getValue());
    }
    @Override
    public void operation(EntityInstance entity) throws ObjectNotExist, ClassCastException, OperationNotSupportedType {
        if (condition.getSingularity().equals("single")) {
            performCondition(entity, condition.getValue());
        }
        else{
            List<PRDCondition> conditions = condition.getPRDCondition();
            checkCondition(entity, conditions);
        }
//        checkConditionPropertyThenAndElse(action, flag);
    }

    private void performCondition(EntityInstance entity, String expressionValue) throws ObjectNotExist, OperationNotSupportedType {
        boolean flag = false;
        flag = checkIfConditionIsTrue(entity, expressionValue);
    }

    private void checkCondition(EntityInstance entity, List<PRDCondition> conditions) throws ObjectNotExist, OperationNotSupportedType {
        for (PRDCondition condition : conditions) {
            if (condition.getSingularity().equals("multiple")) {
                checkCondition(entity, condition.getPRDCondition());
            }
            else {
                this.performCondition(entity, condition.getValue());
            }
        }
    }

    private boolean checkIfConditionIsTrue(EntityInstance entity, String expressionValue) throws ObjectNotExist, ClassCastException, OperationNotSupportedType {
        ExpressionIml expression = new ExpressionIml(expressionValue);
        Object valueInCondition = expression.decipher(entity);
        Property property = entity.getAllProperty().get(condition.getProperty());
        Object propertyValue = property.getValue();
        boolean flag = false;
        switch (condition.getLogical()){
            case "=":
                flag = checkIfPropertyIsEqualToValue(propertyValue, valueInCondition);
                break;
            case "!=":
                flag = checkIfPropertyIsNotEqualToValue(propertyValue, valueInCondition);
                break;
            case "Bt":
                flag = checkIfPropertyIsLessOrBiggerThanValue(property, valueInCondition, ComparisonOperator.BIGGERTHAN);
                break;
            case "Lt":
                flag = checkIfPropertyIsLessOrBiggerThanValue(property, valueInCondition, ComparisonOperator.LESSTHAN);
                break;
        }

        return flag;
    }

    private boolean checkIfPropertyIsLessOrBiggerThanValue(Property property, Object valueInCondition, ComparisonOperator operator) throws ClassCastException, OperationNotSupportedType {
        switch (property.getType()) {
            case DECIMAL:
                Integer intValue = (Integer) valueInCondition;
                Integer propertyValue = (Integer) property.getValue();
                switch (operator) {
                    case LESSTHAN:
                        return propertyValue < intValue;
                    case BIGGERTHAN:
                        return propertyValue > intValue;
                }
                break;
            case FLOAT:
                Float floatValue = (Float) valueInCondition;
                Float propertyFloatValue = (Float) property.getValue();
                switch (operator) {
                    case LESSTHAN:
                        return propertyFloatValue < floatValue;
                    case BIGGERTHAN:
                        return propertyFloatValue > floatValue;
                }
                break;
            case BOOLEAN:
                throw new OperationNotSupportedType("condition", "boolean");
            case STRING:
                throw new OperationNotSupportedType("condition", "string");
        }
        return false;
    }

    private boolean checkIfPropertyIsNotEqualToValue(Object propertyValue, Object valueInCondition) {
        return !(propertyValue.equals(valueInCondition));
    }

    private boolean checkIfPropertyIsEqualToValue(Object propertyValue, Object valueInCondition) {
        return propertyValue.equals(valueInCondition);
    }
}
