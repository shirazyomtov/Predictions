package world.rule.action.condition;

import exceptions.ObjectNotExist;
import exceptions.OperationNotSupportedType;
import jaxb.schema.generated.PRDElse;
import jaxb.schema.generated.PRDThen;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.rule.action.expression.ExpressionIml;

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
    public void operation(EntityInstance entity) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType {
        boolean flag = false;
        flag = checkIfConditionIsTrue(entity);
        performThenOrElse(flag, entity);
    }

    public boolean checkIfConditionIsTrue(EntityInstance entity) throws ObjectNotExist, ClassCastException, OperationNotSupportedType {
        ExpressionIml expression = new ExpressionIml(value);
        Object valueInCondition = expression.decipher(entity);
        Property property = entity.getAllProperty().get(propertyName);
        Object propertyValue = property.getValue();
        boolean flag = false;
        switch (operator){
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
