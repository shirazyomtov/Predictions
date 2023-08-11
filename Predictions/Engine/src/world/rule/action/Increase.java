package world.rule.action;

import exceptions.ObjectNotExist;
import history.History;
import world.entity.instance.EntityInstance;
import world.enums.ActionType;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.rule.action.expression.ExpressionIml;

public class Increase extends Action{

    private final String propertyName;
    private final ExpressionIml expression;

    public Increase(String entityName, String propertyName, String expression) {
        super(entityName, ActionType.INCREASE);
        this.propertyName = propertyName;
        this.expression = new ExpressionIml(expression);
    }

    @Override
    public void operation(EntityInstance entity) throws ObjectNotExist, NumberFormatException, ClassCastException {
        Object by = expression.decipher(this.getEntityName());
        String stringBy = (String) by;
        Property property = entity.getAllProperty().get(propertyName);
        Integer value = null;
        if(property.generateValue() instanceof Integer){
            value = (Integer) property.generateValue();
        }
        Type type = property.getType();
        try {
            if(type.equals(Type.DECIMAL)) {
                Integer number = Integer.parseInt(stringBy);
                property.setValue(number + value);
            }
            else if (type.equals(Type.FLOAT)) {
                Float number = Float.parseFloat(stringBy);
                property.setValue(number + (Float) property.generateValue());
            }
        }
        catch (ClassCastException e){
            throw new ClassCastException("This value that you provide in the action " + getActionType() + " is not a " + type);
        }
    }
}
