package world.rule.action.calculation.binaryCalculationAction;

import exceptions.ObjectNotExist;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;

public class Divide extends BinaryAction{
    public Divide(String entityName, String resultPropertyName, String argument1, String argument2) {
        super(entityName, resultPropertyName, argument1, argument2);
    }

    @Override
    public void operation(EntityInstance entity) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException {
        String valueArg1 = this.getArgument1().decipher(entity);
        String valueArg2 = this.getArgument2().decipher(entity);
        Property resultProp = entity.getAllProperty().get(getResultPropertyName());
        Type type = resultProp.getType();
        try {
            if (type.equals(Type.FLOAT)) {
                Float number = Float.parseFloat(valueArg1);
                Float number2 = Float.parseFloat(valueArg2);
                resultProp.setValue(number / number2);
            }
        }
        catch (ArithmeticException e){
            throw new ArithmeticException("You can not divide by zero");
        }
        catch (ClassCastException e){
            throw new ClassCastException("At least one of the value that you provide in the action " + getActionType() + " is not a " + type);
        }
    }
}
