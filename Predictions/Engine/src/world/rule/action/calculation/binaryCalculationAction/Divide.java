package world.rule.action.calculation.binaryCalculationAction;

import exceptions.ObjectNotExist;
import exceptions.OperationNotCompatibleTypes;
import world.entity.instance.EntityInstance;
import world.enums.CalculationBinaryTypeAction;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.worldInstance.WorldInstance;

import java.io.Serializable;

public class Divide extends BinaryAction implements Serializable {
    public Divide(String entityName, CalculationBinaryTypeAction typeOfCalculation, String resultPropertyName, String argument1, String argument2) {
        super(entityName, typeOfCalculation, resultPropertyName, argument1, argument2);
    }

    @Override
    public boolean operation(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException, OperationNotCompatibleTypes {
        String valueArg1 = this.getArgument1().decipher(entity, worldInstance);
        String valueArg2 = this.getArgument2().decipher(entity, worldInstance);
        Property resultProp = entity.getAllProperty().get(getResultPropertyName());
        Type type = resultProp.getType();
        try {
            if (type.equals(Type.FLOAT)) {
                Float number = Float.parseFloat(valueArg1);
                Float number2 = Float.parseFloat(valueArg2);
                if(number2 == 0){
                    throw new ArithmeticException("You can not divide by zero");
                }
                resultProp.setValue(number / number2);
            }
            return false;
        }
        catch (NumberFormatException | ClassCastException e){
            throw new NumberFormatException("At least one of the values  that you provide in the action " + getActionType() + " is not a " + type);
        }
    }
}
