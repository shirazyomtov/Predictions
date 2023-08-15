package world.rule.action;

import exceptions.ObjectNotExist;
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

    public Increase(String entityName, String propertyName, String expression) {
        super(entityName, ActionType.INCREASE);
        this.propertyName = propertyName;
        this.expression = new ExpressionIml(expression);
    }

    @Override
    public boolean operation(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, NumberFormatException, ClassCastException {
        String by = expression.decipher(entity, worldInstance);
        Property property = entity.getAllProperty().get(propertyName);
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
            return false;
        }

        catch (NumberFormatException | ClassCastException e){
            throw new NumberFormatException("The value " + by + " that you provide in the action " + getActionType() + " is not a " + type);
        }
    }
}
