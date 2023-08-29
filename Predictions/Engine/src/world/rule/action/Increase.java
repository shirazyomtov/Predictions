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

public class Increase extends Action implements Serializable {

    private final String propertyName;
    private final ExpressionIml expression;

        public Increase(PRDAction prdAction) {
        super(prdAction.getEntity(), ActionType.INCREASE, prdAction.getPRDSecondaryEntity());
        this.propertyName = prdAction.getProperty();
        this.expression = new ExpressionIml(prdAction.getBy(), propertyName);
    }


    @Override
    public Action operation(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, NumberFormatException, ClassCastException, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        EntityInstance entityInstance = checkAndGetAppropriateInstance(entity, secondaryEntity);
        String by = expression.decipher(entityInstance, worldInstance);
        Property property = entityInstance.getAllProperty().get(propertyName);
        Type type = property.getType();
        try {
            if(type.equals(Type.DECIMAL)) {
                Integer number = Integer.parseInt(by);
                property.setValue(number + (Integer) property.getValue());
            }
            else if (type.equals(Type.FLOAT)) {
                Float number = Float.parseFloat(by);
                property.setValue(number + (Float) property.getValue());
            }
            return null;
        }

        catch (NumberFormatException | ClassCastException e){
            throw new NumberFormatException("The value " + by + " that you provide in the action " + getActionType() + " is not a " + type);
        }
    }
}
