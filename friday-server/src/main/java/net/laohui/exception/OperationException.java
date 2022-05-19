package net.laohui.exception;

/**
 * 操作异常
 */
public class OperationException extends RuntimeException {
    public OperationException(String message) {
        super(message);
    }

    public OperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationException() {
        super();
    }
}
