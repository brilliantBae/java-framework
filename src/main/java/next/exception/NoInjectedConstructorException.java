package next.exception;

public class NoInjectedConstructorException extends RuntimeException {
    public NoInjectedConstructorException(String message) {
        super(message);
    }
}
