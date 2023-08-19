package world.rule.action.condition;

import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import exceptions.OperationNotSupportedType;
import jaxb.schema.generated.PRDElse;
import jaxb.schema.generated.PRDThen;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.rule.action.expression.ExpressionIml;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.util.Objects;

public class SingleCondition extends AbstractCondition implements Serializable {
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
    public boolean operation(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes {
        boolean flag = false;
        boolean kill = false;
        flag = checkIfConditionIsTrue(entity, worldInstance);
        kill = performThenOrElse(flag, entity, worldInstance);
        return kill;
    }

    public boolean checkIfConditionIsTrue(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, ClassCastException, OperationNotSupportedType, OperationNotCompatibleTypes {
            ExpressionIml expression = new ExpressionIml(value, propertyName);
            String valueInCondition = expression.decipher(entity, worldInstance);
            Property property = entity.getAllProperty().get(propertyName);
            boolean flag = false;
            switch (operator) {
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
        try {
            switch (property.getType()) {
                case DECIMAL:
                    Float floatValue = Float.parseFloat(valueInCondition);
                    Integer propertyValue = (Integer) property.getValue();
                    switch (operator) {
                        case LESSTHAN:
                            return (float)propertyValue < floatValue;
                        case BIGGERTHAN:
                            return (float)propertyValue > floatValue;
                    }
                    break;
                case FLOAT:
                    Float floatValue2 = Float.parseFloat(valueInCondition);
                    Float propertyFloatValue = (Float) property.getValue();
                    switch (operator) {
                        case LESSTHAN:
                            return propertyFloatValue < floatValue2;
                        case BIGGERTHAN:
                            return propertyFloatValue > floatValue2;
                    }
                    break;
                case BOOLEAN:
                    throw new OperationNotSupportedType("condition", "boolean");
                case STRING:
                    throw new OperationNotSupportedType("condition", "string");
            }
        }
        catch (NumberFormatException | ClassCastException e){
            throw new ClassCastException("The value " + valueInCondition + " that you provide in the action " + getActionType() + " is not a " + property.getType());
        }
        return false;
    }

    private boolean checkIfPropertyIsNotEqualToValue(Property property, String valueInCondition)throws OperationNotCompatibleTypes {
        return !(checkIfPropertyIsEqualToValue(property, valueInCondition));
    }

    private boolean checkIfPropertyIsEqualToValue(Property property, String valueInCondition)throws ClassCastException , OperationNotCompatibleTypes{
        try {
            boolean flag = false;
            switch (property.getType()) {
                case DECIMAL:
                    Float floatValue = Float.parseFloat(valueInCondition);
                    Integer propertyValue = (Integer) property.getValue();
                    flag = Objects.equals((float) propertyValue, floatValue);
                    break;
                case FLOAT:
                    Float floatValue2 = Float.parseFloat(valueInCondition);
                    Float propertyFloatValue = (Float) property.getValue();
                    flag = propertyFloatValue.equals(floatValue2);
                    break;
                case BOOLEAN:
                    if(valueInCondition.equals("true") || valueInCondition.equals("false")) {
                        flag = property.getValue().toString().equals(valueInCondition);
                    }
                    else{
                        throw new ClassCastException();
                    }
                    break;
                case STRING:
                    if(isNumber(valueInCondition)) {
                        throw new OperationNotCompatibleTypes(Type.STRING.toString(), "Number");
                    }
                    else {
                        String propertyStringValue = (String) property.getValue();
                        flag = propertyStringValue.equals(valueInCondition);
                    }
                    break;
            }

            return flag;
        }

        catch (NumberFormatException | ClassCastException e){
            throw new ClassCastException("The value " + valueInCondition + " that you provide in the action " + getActionType() + " is not a " + property.getType());
        }
    }

    private boolean isNumber(String number){
        try {
            Float.parseFloat(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
