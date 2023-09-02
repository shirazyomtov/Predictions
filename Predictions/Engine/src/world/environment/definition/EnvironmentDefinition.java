package world.environment.definition;

import jaxb.schema.generated.PRDEnvProperty;
import world.enums.Type;
import world.range.RangeImpl;

import java.io.Serializable;

public class EnvironmentDefinition implements Serializable {

    private final String name;

    private final Type type;

    private RangeImpl range = null;

    public EnvironmentDefinition(PRDEnvProperty prdEnvProperty){
        this.name = prdEnvProperty.getPRDName();
        this.type = Enum.valueOf(Type.class, prdEnvProperty.getType().toUpperCase());
        if(prdEnvProperty.getPRDRange()!= null) {
            this.range = new RangeImpl((float) prdEnvProperty.getPRDRange().getFrom(), (float) prdEnvProperty.getPRDRange().getTo());
        }
    }

    public String getName() {
        return this.name;
    }

    public Type getType(){
        return this.type;
    }

    public RangeImpl getRange() {
        return this.range;
    }

    public void checkValidationFloatEnvironment(String value)throws NumberFormatException, IndexOutOfBoundsException{
        try {
            Float userInput = Float.parseFloat(value);
            checkIfInputInRange(userInput, false);
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("You must enter a float number that matches the type environment " + name);
        }
    }

    public void checkValidationDecimalEnvironment(String value)throws NumberFormatException, IndexOutOfBoundsException {
        try {
            Integer userInput = Integer.parseInt(value);
            checkIfInputInRange(userInput, true);
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("You must enter a integer number that matches the type environment " + name);
        }
    }

    private void checkIfInputInRange(Object userInput, boolean isDecimal) throws NumberFormatException, IndexOutOfBoundsException{
        if (range != null){
            if(!isDecimal) {
                if ((float) userInput < range.getFrom() || (float) userInput > range.getTo()) {
                    throw new IndexOutOfBoundsException("You need to enter a float number from " + range.getFrom() + " to " + range.getTo());
                }
            }
            else {
                if ((int) userInput < range.getFrom().intValue() || (int) userInput > range.getTo().intValue()) {
                    throw new IndexOutOfBoundsException("You need to enter a integer number from " + range.getFrom() + " to " + range.getTo());
                }
            }
        }
    }

    public void checkValidationBoolEnvironment(String value) throws IllegalArgumentException {
        if(!value.equals("true") && !value.equals("false")){
            throw new IllegalArgumentException("You must enter a boolean value that matches the type environment " + name);
        }
    }
}
