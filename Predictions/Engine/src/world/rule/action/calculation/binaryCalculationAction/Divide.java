package world.rule.action.calculation.binaryCalculationAction;

import exceptions.ObjectNotExist;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;
import world.worldInstance.WorldInstance;

public class Divide extends BinaryAction{
    public Divide(String entityName, String resultPropertyName, String argument1, String argument2) {
        super(entityName, resultPropertyName, argument1, argument2);
    }

    @Override
    public boolean operation(EntityInstance entity, WorldInstance worldInstance) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException {
        String valueArg1 = this.getArgument1().decipher(entity, worldInstance);
        String valueArg2 = this.getArgument2().decipher(entity, worldInstance);
        Property resultProp = entity.getAllProperty().get(getResultPropertyName());
        Type type = resultProp.getType();
        try {
            if (type.equals(Type.FLOAT)) {
                Float number = Float.parseFloat(valueArg1);
                Float number2 = Float.parseFloat(valueArg2);
                resultProp.setValue(number / number2);
            }
            return false;
        }
        catch (ArithmeticException e){
            throw new ArithmeticException("You can not divide by zero");
        }
        catch (ClassCastException e){
            throw new ClassCastException("At least one of the value that you provide in the action " + getActionType() + " is not a " + type);
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("The value that you provide in the action " + getActionType() + " is not a " + type);
        }
    }
}
