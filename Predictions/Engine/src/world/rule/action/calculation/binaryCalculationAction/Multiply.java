package world.rule.action.calculation.binaryCalculationAction;

import exceptions.ObjectNotExist;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;

public class Multiply extends BinaryAction {


    public Multiply(String entityName, String resultPropertyName, String argument1, String argument2) {
        super(entityName, resultPropertyName, argument1, argument2);
    }

    @Override
    public void operation(EntityInstance entity) throws ObjectNotExist, NumberFormatException, ClassCastException {
        Object valueArg1 = this.getArgument1().decipher(entity);
        Object valueArg2 = this.getArgument2().decipher(entity);
        String stringValueArg1 = (String) valueArg1;
        String stringValueArg2= (String) valueArg2;
        Property resultProp = entity.getAllProperty().get(getResultPropertyName());
        Type type = resultProp.getType();
        try {
            if (type.equals(Type.DECIMAL)){
                Integer number = Integer.parseInt(stringValueArg1);
                Integer number2 = Integer.parseInt(stringValueArg2);
                resultProp.setValue(number * number2);
            }
            else if (type.equals(Type.FLOAT)) {
                Float number = Float.parseFloat(stringValueArg1);
                Float number2 = Float.parseFloat(stringValueArg2);
                resultProp.setValue(number * number2);
            }
        }
        catch (ClassCastException e){
            throw new ClassCastException("At least one of the value that you provide in the action " + getActionType() + " is not a " + type);
        }
    }
}

