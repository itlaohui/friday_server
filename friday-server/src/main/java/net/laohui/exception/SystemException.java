package net.laohui.exception;

/**
 * 系统异常
 */
public class SystemException extends RuntimeException {
    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException() {
        super();
    }
}
