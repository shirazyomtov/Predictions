package world.rule.action.condition;

import exceptions.ObjectNotExist;
import exceptions.OperationNotSupportedType;
import jaxb.schema.generated.PRDElse;
import jaxb.schema.generated.PRDThen;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.rule.action.expression.ExpressionIml;
import world.worldInstance.WorldInstance;

public class SingleCondition extends AbstractCondition{
    private String operator;
    private String value;
    private String propertyName;

    public SingleCondition(PRDThen prdThen, PRDElse prdElse, String operatorType, String value, String entityName, String propertyName) {
        super(prdThen, prdElse, entityName, "single");
        this.operator = operatorType;
        this.value = value;
        this.propertyName = propertyName;
    }

    @Override
    public boolean operation(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType {
        boolean flag = false;
        boolean kill = false;
        flag = checkIfConditionIsTrue(entity, worldInstance);
        kill = performThenOrElse(flag, entity, worldInstance);
        return kill;
    }

    public boolean checkIfConditionIsTrue(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, ClassCastException, OperationNotSupportedType {
        ExpressionIml expression = new ExpressionIml(value);
        String valueInCondition = expression.decipher(entity, worldInstance);
        Property property = entity.getAllProperty().get(propertyName);
        boolean flag = false;
        switch (operator){
            case "=":
                flag = checkIfPropertyIsEqualToValue(property, valueInCondition);
                break;
            case "!=":
                flag = checkIfPropertyIsNotEqualToValue(property, valueInCondition);
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

    private boolean checkIfPropertyIsLessOrBiggerThanValue(Property property, String valueInCondition, ComparisonOperator operator) throws ClassCastException, OperationNotSupportedType {
        String stringValue;
        switch (property.getType()) {
            case DECIMAL:
                Integer intValue = Integer.parseInt(valueInCondition);
                Integer propertyValue = (Integer) property.getValue();
                switch (operator) {
                    case LESSTHAN:
                        return propertyValue < intValue;
                    case BIGGERTHAN:
                        return propertyValue > intValue;
                }
                break;
            case FLOAT:
                Float floatValue = Float.parseFloat(valueInCondition);
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

    private boolean checkIfPropertyIsNotEqualToValue(Property property, String valueInCondition) {
        return !(checkIfPropertyIsEqualToValue(property, valueInCondition));
    }

    private boolean checkIfPropertyIsEqualToValue(Property property, String valueInCondition) {
        boolean flag = false;
        switch (property.getType()) {
            case DECIMAL:
                Integer intValue = Integer.parseInt(valueInCondition);
                Integer propertyValue = (Integer) property.getValue();
                flag = propertyValue.equals(intValue);
                break;
            case FLOAT:
                Float floatValue = Float.parseFloat(valueInCondition);
                Float propertyFloatValue = (Float) property.getValue();
                flag = propertyFloatValue.equals(floatValue);
                break;
            case BOOLEAN:
                flag = property.getValue().toString().equals(valueInCondition);
                break;
            case STRING:
                String propertyStringValue = (String) property.getValue();
                flag = propertyStringValue.equals(valueInCondition);
                break;
        }
        
        return  flag;
    }
}
