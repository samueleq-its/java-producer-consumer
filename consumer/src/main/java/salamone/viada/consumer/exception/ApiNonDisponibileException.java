package salamone.viada.consumer.exception;

public class ApiNonDisponibileException extends RuntimeException {
    public ApiNonDisponibileException(String message) {
        super(message);
    }

    public ApiNonDisponibileException(String message, Throwable cause) {
        super(message, cause);
    }
}