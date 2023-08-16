package exceptions;

public class FilePathException extends Exception{

    public enum ErrorType{
        FILE_NAME_CONTAINS_LESS_THAN_5_CHARACTERS("The file path contains less than 5 characters."),
        NOT_ENDS_WITH_XML("The path of the file does not contain an extension of .xml");

        private final String errorMessage;

        ErrorType(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getMessage() {
            return this.errorMessage;
        }
    }

    private final ErrorType errorType;

    public FilePathException(ErrorType e) {
        errorType = e;
    }

    @Override
    public String getMessage() {
        return String.format(errorType.getMessage());
    }
}
