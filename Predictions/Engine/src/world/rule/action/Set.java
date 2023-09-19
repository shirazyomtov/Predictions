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
import java.util.List;

public class Set extends Action implements Serializable {
    private final String propertyName;
    private final ExpressionIml expression;

        public Set(PRDAction prdAction) {
        super(prdAction.getEntity(), ActionType.SET, prdAction.getPRDSecondaryEntity());
        this.propertyName = prdAction.getProperty();
        this.expression = new ExpressionIml(prdAction.getValue(), propertyName);
    }


    @Override
    public List<Action> operation(EntityInstance primaryEntity, WorldInstance worldInstance, EntityInstance secondaryEntity, String secondEntityName, List<EntityInstance> proximity) throws ObjectNotExist, NumberFormatException, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        EntityInstance entityInstance = checkAndGetAppropriateInstance(primaryEntity, secondaryEntity, secondEntityName);
        if (entityInstance != null) {
            String value;
            if (entityInstance == secondaryEntity) {
                value = expression.decipher(entityInstance, worldInstance, primaryEntity, secondEntityName);
            } else {
                value = expression.decipher(entityInstance, worldInstance, secondaryEntity, secondEntityName);
            }
            if (value != null) {
                Property property = entityInstance.getAllProperty().get(propertyName);
                Type type = property.getType();
                try {
                    if (type.equals(Type.DECIMAL)) {
                        Integer number = Integer.parseInt(value);
                        property.setValue(number);
                    } else if (type.equals(Type.FLOAT)) {
                        Float number = Float.parseFloat(value);
                        property.setValue(number);
                    } else if (type.equals(Type.BOOLEAN)) {
                        if (value.equals("true") || value.equals("false")) {
                            property.setValue(value);
                        } else {
                            throw new NumberFormatException();
                        }
                    } else if (type.equals(Type.STRING)) {
                        property.setValue(value);
                    }
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("The value: " + value + " that you provide in the action " + getActionType() + " is not a " + type);
                }
                return null;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getExpression() {
        return expression.getExpressionName();
    }
}
