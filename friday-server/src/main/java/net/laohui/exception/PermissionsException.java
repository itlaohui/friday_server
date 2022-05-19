package net.laohui.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 验证错误异常
 */
public class PermissionsException extends AuthenticationException {
    public PermissionsException() {
        super();
    }

    public PermissionsException(String message) {
        super(message);
    }

    public PermissionsException(String message, Throwable cause) {
        super(message, cause);
    }
}
