package world.rule.action.condition;

import exceptions.*;
import jaxb.schema.generated.PRDAction;
import jaxb.schema.generated.PRDCondition;
import jaxb.schema.generated.PRDElse;
import jaxb.schema.generated.PRDThen;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.rule.action.Action;
import world.rule.action.expression.ExpressionIml;
import world.worldInstance.WorldInstance;

import java.io.Serializable;
import java.util.Objects;

public class SingleCondition extends AbstractCondition implements Serializable {
    private final String operator;
    private final String value;
    private final String propertyName;


    public SingleCondition(PRDThen prdThen, PRDElse prdElse, PRDCondition prdCondition, PRDAction.PRDSecondaryEntity prdSecondaryEntity)  {
        super(prdThen, prdElse, prdCondition.getEntity(), "single", prdSecondaryEntity);
        this.operator = prdCondition.getOperator();
        this.value = prdCondition.getValue();
        this.propertyName = prdCondition.getProperty();
    }

    @Override
    public Action operation(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        boolean flag = false;
        Action killOrReplace = null;
        flag = checkIfConditionIsTrue(entity, worldInstance, secondaryEntity);
        killOrReplace = performThenOrElse(flag, entity, worldInstance, secondaryEntity);
        return killOrReplace;
    }

    public boolean checkIfConditionIsTrue(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, ClassCastException, OperationNotSupportedType, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        EntityInstance entityInstance = checkAndGetAppropriateInstance(entity, secondaryEntity);
        ExpressionIml expression = new ExpressionIml(value, propertyName);
            String valueInCondition = expression.decipher(entityInstance, worldInstance);
            Property property = entityInstance.getAllProperty().get(propertyName);
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
