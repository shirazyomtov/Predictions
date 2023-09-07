package exceptions;

public class OperationNotSupportedType extends RuntimeException{

    private  final  String actionType;

    private final String typeOfValue;

    private final String EXCEPTION_MESSAGE = "The operation %s you attempted is not supported on %s type ";

    public OperationNotSupportedType(String actionType, String typeOfValue) {
        this.actionType = actionType;
        this.typeOfValue = typeOfValue;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, actionType, typeOfValue);
    }

}
