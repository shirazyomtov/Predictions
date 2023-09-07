package world.rule.action.calculation.binaryCalculationAction;

import exceptions.EntityNotDefine;
import exceptions.FormatException;
import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import jaxb.schema.generated.PRDAction;
import world.entity.instance.EntityInstance;
import world.enums.CalculationBinaryTypeAction;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.rule.action.Action;
import world.worldInstance.WorldInstance;

import java.io.Serializable;

public class Divide extends BinaryAction implements Serializable {
    public Divide(PRDAction prdAction, CalculationBinaryTypeAction typeOfCalculation) {
        super(prdAction.getEntity(), typeOfCalculation, prdAction.getResultProp(), prdAction.getPRDDivide().getArg1(), prdAction.getPRDDivide().getArg2(), prdAction.getPRDSecondaryEntity());
    }

    @Override
    public Action operation(EntityInstance primaryEntity, WorldInstance worldInstance, EntityInstance secondaryEntity, String secondEntityName) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotCompatibleTypes, FormatException, EntityNotDefine {
        EntityInstance entityInstance = checkAndGetAppropriateInstance(primaryEntity, secondaryEntity, secondEntityName);
        if (entityInstance != null) {
            String valueArg1;
            String valueArg2;
            if (entityInstance == secondaryEntity) {
                valueArg1 = this.getArgument1().decipher(entityInstance, worldInstance, primaryEntity, secondEntityName);
                valueArg2 = this.getArgument2().decipher(entityInstance, worldInstance, primaryEntity, secondEntityName);
            } else {
                valueArg1 = this.getArgument1().decipher(entityInstance, worldInstance, secondaryEntity, secondEntityName);
                valueArg2 = this.getArgument2().decipher(entityInstance, worldInstance, secondaryEntity, secondEntityName);
            }
            if (valueArg1 != null && valueArg2 != null) {
                Property resultProp = entityInstance.getAllProperty().get(getResultPropertyName());
                Type type = resultProp.getType();
                try {
                    if (type.equals(Type.FLOAT)) {
                        Float number = Float.parseFloat(valueArg1);
                        Float number2 = Float.parseFloat(valueArg2);
                        if (number2 == 0) {
                            throw new ArithmeticException("You can not divide by zero");
                        }
                        resultProp.setValue(number / number2);
                    }
                    return null;
                } catch (NumberFormatException | ClassCastException e) {
                    throw new NumberFormatException("At least one of the values  that you provide in the action " + getActionType() + " is not a " + type);
                }
            }
            else{
                return null;
            }
        }
        else {
            return null;
        }
    }
}
