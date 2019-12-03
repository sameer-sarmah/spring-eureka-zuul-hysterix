package core.exception;

public class CoreException extends Exception {
    private String message;

    public CoreException(String message){
        super(message);
    }

    public CoreException(String message,Throwable throwable){
        super(message,throwable);
    }
}
