package exceptions;

public class OperationNotCompatibleTypes extends RuntimeException{

    private final String typeProperty;

    private final String typeValue;

    private final String EXCEPTION_MESSAGE = "You cannot perform the operation between type %s and type %s ";


    public OperationNotCompatibleTypes(String typeProperty, String typeValue) {
        this.typeProperty = typeProperty;
        this.typeValue = typeValue;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, typeProperty, typeValue);
    }
}
