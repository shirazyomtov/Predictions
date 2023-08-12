package world.rule.action;

import exceptions.ObjectNotExist;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.rule.action.expression.ExpressionIml;

public class Set extends Action {
    private final String propertyName;
    private final ExpressionIml expression;

    public Set(String entityName, String propertyName, String expression) {
        super(entityName, ActionType.SET);
        this.propertyName = propertyName;
        this.expression = new ExpressionIml(expression);
    }

    @Override
    public void operation(EntityInstance entity) throws ObjectNotExist {
        String value = expression.decipher(entity);
        Property property = entity.getAllProperty().get(propertyName);
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
            }
            else if (type.equals(Type.STRING)) {
                property.setValue(value);
            }
        }
        catch (ClassCastException e){
            throw new ClassCastException("This value that you provide in the action " + getActionType() + " is not a " + type);
        }
    }
}
