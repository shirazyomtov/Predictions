package world.rule.action;

import exceptions.EntityNotDefine;
import exceptions.FormatException;
import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import jaxb.schema.generated.PRDAction;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.rule.action.expression.ExpressionIml;
import world.worldInstance.WorldInstance;

import java.io.Serializable;

public class Set extends Action implements Serializable {
    private final String propertyName;
    private final ExpressionIml expression;

        public Set(PRDAction prdAction) {
        super(prdAction.getEntity(), ActionType.SET, prdAction.getPRDSecondaryEntity());
        this.propertyName = prdAction.getProperty();
        this.expression = new ExpressionIml(prdAction.getValue(), propertyName);
    }


    @Override
    public Action operation(EntityInstance primaryEntity, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, NumberFormatException, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        EntityInstance entityInstance = checkAndGetAppropriateInstance(primaryEntity, secondaryEntity);
        String value;
        if(entityInstance == secondaryEntity) {
            value = expression.decipher(entityInstance, worldInstance, primaryEntity);
        }
        else{
            value = expression.decipher(entityInstance, worldInstance, secondaryEntity);
        }
          Property property = entityInstance.getAllProperty().get(propertyName);
          Type type = property.getType();
        try {
            if(type.equals(Type.DECIMAL)) {
                Integer number = Integer.parseInt(value);
                property.setValue(number);
            }
            else if (type.equals(Type.FLOAT)) {
                Float number = Float.parseFloat(value);
                property.setValue(number);
            }
            else if (type.equals(Type.BOOLEAN)) {
                if(value.equals("true") || value.equals("false")){
                    property.setValue(value);
                }
                else {
                    throw new NumberFormatException();
                }
            }
            else if (type.equals(Type.STRING)) {
                property.setValue(value);
            }
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("The value: " + value + " that you provide in the action " + getActionType() + " is not a " + type);
        }
        return null;
    }
}
