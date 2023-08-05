package exceptions;

public class ObjectNotExist extends Exception{

    private final String objectName;

    private  final  String className;

    private final String EXCEPTION_MESSAGE = "The %s name: %s that you define in the action not exists in the %s names ";

    public ObjectNotExist(String objectName, String className) {
        this.objectName = objectName;
        this.className = className;
    }
    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, className, objectName, className);
    }
}
