package world.rule.action.calculation.binaryCalculationAction;

import exceptions.ObjectNotExist;
import history.History;
import world.entity.instance.EntityInstance;
import world.enums.Type;
import world.propertyInstance.api.Property;

public class Divide extends BinaryAction{
    public Divide(String entityName, String resultPropertyName, String argument1, String argument2) {
        super(entityName, resultPropertyName, argument1, argument2);
    }

    @Override
    public void operation(EntityInstance entity) throws ObjectNotExist, NumberFormatException, ClassCastException, ArithmeticException {
        Object valueArg1 = this.getArgument1().decipher(this.getEntityName());
        Object valueArg2 = this.getArgument2().decipher(this.getEntityName());
        Property resultProp = entity.getAllProperty().get(getResultPropertyName());
        Type type = resultProp.getType();
        try {
            if (type.equals(Type.FLOAT)) {
                Float number = null, number2 = null;
                if(valueArg1 instanceof Float){
                    number = (Float) valueArg1;
                }
                if(valueArg2 instanceof String){
                    String value = (String) valueArg2;
                    number2 = Float.parseFloat(value);
                }
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
