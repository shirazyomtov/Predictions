package exceptions;

public class FormatException extends RuntimeException{
    private final String EXCEPTION_MESSAGE = "The format you entered is incorrect, please enter in the following format: {name of entity in the system}.{name of property of the entity that you entered}";
    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE);
    }
}
