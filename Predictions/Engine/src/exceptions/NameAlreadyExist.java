package exceptions;

public class NameAlreadyExist extends Exception {

    private final String name;

    private final String className;
    private final String EXCEPTION_MESSAGE = "The %s name: %s is already exists in the %s names,please enter a unique name for every %s name  ";
    // change later line 8

    public NameAlreadyExist(String name, String className)
    {
        this.name = name;
        this.className = className;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, className, name, className, className);
    }
}
