package world.rule.action.condition;

import exceptions.ObjectNotExist;
import exceptions.OperationNotSupportedType;
import jaxb.schema.generated.*;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.enums.ComparisonOperator;
import world.propertyInstance.api.Property;
import world.rule.action.Action;
import world.rule.action.expression.ExpressionIml;

import java.util.List;

public class Condition  extends Action {

    private final PRDCondition condition;

    private final PRDThen thenActions;

    private final PRDElse elseActions;


    public Condition(String entityName, PRDCondition condition, PRDThen thenActions, PRDElse elseActions)
    {
        super(entityName, ActionType.CONDITION);
        this.condition = condition;
        this.thenActions = thenActions;
        this.elseActions = elseActions;
    }
    @Override
    public void operation(EntityInstance entity) throws ObjectNotExist, ClassCastException, OperationNotSupportedType {
        boolean flag = false;
        if (condition.getSingularity().equals("single")) {
            flag = checkIfConditionIsTrue(entity, condition);
        }
        else{
            List<PRDCondition> conditions = condition.getPRDCondition();
            flag = checkCondition(entity, conditions, condition.getLogical());
        }
        performThenOrElse(flag);
    }

    private void performThenOrElse(boolean flag) {
        Action action;
        if(flag){
            for(PRDAction actionThen: thenActions.getPRDAction()){
//                action = new ActionFactory.createAction(Enum.valueOf(ActionType.class, prdAction.getType().toUpperCase()),
//                        prdAction.getEntity(), prdAction.getProperty(), prdAction.getBy(),
//                        prdAction.getValue(), prdAction.getPRDMultiply(), prdAction.getPRDDivide(),
//                        prdAction.getResultProp(), prdAction);
//                action.operation(entityInstance);
            }
        }
        else{
            if(elseActions != null){
                for(PRDAction actionElse: elseActions.getPRDAction()){

                }
            }
        }
    }

    private boolean checkCondition(EntityInstance entity, List<PRDCondition> conditions, String logical) throws ObjectNotExist, OperationNotSupportedType {
        if (logical.equals("or")) {
            for (PRDCondition condition : conditions) {
                if (condition.getSingularity().equals("multiple")) {
                    if (checkCondition(entity, condition.getPRDCondition(), condition.getLogical())) {
                        return true;
                    }
                } else {
                    if (checkIfConditionIsTrue(entity, condition)) {
                        return true;
                    }
                }
            }
            return false;
        }
        else if (logical.equals("and")) {
            for (PRDCondition condition : conditions) {
                if (condition.getSingularity().equals("multiple")) {
                    if (!checkCondition(entity, condition.getPRDCondition(), condition.getLogical())) {
                        return false;
                    }
                } else {
                    if (!checkIfConditionIsTrue(entity, condition)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkIfConditionIsTrue(EntityInstance entity, PRDCondition condition) throws ObjectNotExist, ClassCastException, OperationNotSupportedType {
        ExpressionIml expression = new ExpressionIml(condition.getValue());
        Object valueInCondition = expression.decipher(entity);
        Property property = entity.getAllProperty().get(condition.getProperty());
        Object propertyValue = property.getValue();
        boolean flag = false;
        switch (condition.getOperator()){
            case "=":
                flag = checkIfPropertyIsEqualToValue(propertyValue, valueInCondition);
                break;
            case "!=":
                flag = checkIfPropertyIsNotEqualToValue(propertyValue, valueInCondition);
                break;
            case "bt":
                flag = checkIfPropertyIsLessOrBiggerThanValue(property, valueInCondition, ComparisonOperator.BIGGERTHAN);
                break;
            case "lt":
                flag = checkIfPropertyIsLessOrBiggerThanValue(property, valueInCondition, ComparisonOperator.LESSTHAN);
                break;
        }

        return flag;
    }

    private boolean checkIfPropertyIsLessOrBiggerThanValue(Property property, Object valueInCondition, ComparisonOperator operator) throws ClassCastException, OperationNotSupportedType {
        String stringValue;
        switch (property.getType()) {
            case DECIMAL:
                stringValue = (String) valueInCondition;
                Integer intValue = Integer.parseInt(stringValue);
                Integer propertyValue = (Integer) property.getValue();
                switch (operator) {
                    case LESSTHAN:
                        return propertyValue < intValue;
                    case BIGGERTHAN:
                        return propertyValue > intValue;
                }
                break;
            case FLOAT:
                stringValue = (String) valueInCondition;
                Float floatValue = Float.parseFloat(stringValue);
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
