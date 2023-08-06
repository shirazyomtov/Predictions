package exceptions;

public class FileException extends Exception{

    private final String path;
    private final String EXCEPTION_MESSAGE = "The path %s you provided is not a valid path, the file is not an XML file";

    public FileException(String path)
    {
        this.path = path;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, path);
    }
}
