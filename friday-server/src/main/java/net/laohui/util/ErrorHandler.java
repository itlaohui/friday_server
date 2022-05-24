package net.laohui.util;

import lombok.extern.log4j.Log4j2;
import net.laohui.enumerate.ResponseResultEnum;
import net.laohui.exception.OperationException;
import net.laohui.exception.SystemException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@Log4j2
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<String> exception(Exception e) {
        if (e instanceof IllegalStateException && e.getMessage().contains("getWriter()")) {
            log.error("getWriter错误: {}", e.getMessage());
            return null;
        } else {
            log.error("发生错误: ex={}", e.getMessage(), e);
            return ResponseResult.error(ResponseResultEnum.RC500.getCode(), ResponseResultEnum.RC500.getMessage());
        }
    }

    /**
     * 捕获shiro的验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseResult<String> AuthenticationException(AuthenticationException e) {
        log.error("验证错误: {}", e.getMessage(), e);
        return ResponseResult.error(ResponseResultEnum.RC401.getCode(), e.getMessage());
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseResult<String> UnauthenticatedException(UnauthenticatedException e) {
        log.error("验证错误: {}", e.getMessage());
        return ResponseResult.error(ResponseResultEnum.RC401.getCode(), ResponseResultEnum.RC401.getMessage());
    }

    /**
     * 捕获自定义异常: SystemException
     * @param e
     * @return
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<String> SystemException(SystemException e) {
        String message = e.getMessage();
        log.error("系统错误: {}", message);
        return ResponseResult.error(ResponseResultEnum.RC500.getCode(), isNotEmpty(message) ? message : ResponseResultEnum.RC500.getMessage());
    }

    /**
     * 捕获自定义异常: OperationException
     * @param e
     * @return
     */
    @ExceptionHandler(OperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<String> OperationException(OperationException e) {
        String message = e.getMessage();
        log.error("操作错误: {}", message);
        return ResponseResult.error(ResponseResultEnum.RC400.getCode(), isNotEmpty(message) ? message : ResponseResultEnum.RC400.getMessage());
    }
}
