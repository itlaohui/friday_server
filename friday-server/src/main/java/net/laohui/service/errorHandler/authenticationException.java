package net.laohui.service.errorHandler;

public class authenticationException extends RuntimeException {

    public authenticationException(String message) {
        super(message);
    }

    public authenticationException() {
        super();
    }
}
