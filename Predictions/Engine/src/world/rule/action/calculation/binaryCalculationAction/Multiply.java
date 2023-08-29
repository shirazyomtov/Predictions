package world.rule.action.calculation.binaryCalculationAction;

import exceptions.EntityNotDefine;
import exceptions.FormatException;
import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import jaxb.schema.generated.PRDAction;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.rule.action.Action;
import world.worldInstance.WorldInstance;

import java.io.Serializable;

public class Multiply extends BinaryAction implements Serializable {


    public Multiply(PRDAction prdAction) {
        super(prdAction.getEntity(), prdAction.getResultProp(), prdAction.getPRDMultiply().getArg1(), prdAction.getPRDMultiply().getArg2(), prdAction.getPRDSecondaryEntity());
    }


    @Override
    public Action operation(EntityInstance entity, WorldInstance worldInstance, EntityInstance secondaryEntity) throws ObjectNotExist, NumberFormatException, ClassCastException, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        EntityInstance entityInstance = checkAndGetAppropriateInstance(entity, secondaryEntity);
        String valueArg1 = this.getArgument1().decipher(entityInstance, worldInstance);
        String valueArg2 = this.getArgument2().decipher(entityInstance, worldInstance);
        Property resultProp = entityInstance.getAllProperty().get(getResultPropertyName());
        Type type = resultProp.getType();
        try {
            if (type.equals(Type.DECIMAL)){
                Integer number = Integer.parseInt(valueArg1);
                Integer number2 = Integer.parseInt(valueArg2);
                resultProp.setValue(number * number2);
            }
            else if (type.equals(Type.FLOAT)) {
                Float number = Float.parseFloat(valueArg1);
                Float number2 = Float.parseFloat(valueArg2);
                resultProp.setValue(number * number2);
            }
            return null;
        }
        catch (NumberFormatException | ClassCastException e){
            throw new NumberFormatException("At least one of the values  that you provide in the action " + getActionType() + " is not a " + type);
        }
    }
}

