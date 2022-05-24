package net.laohui.util;

import lombok.Data;
import net.laohui.enumerate.ResponseResultEnum;

/**
 * @author laohui
 * @desc 响应结果封装，会由切面拦截更改状态码
 * @param <T>
 */
@Data
public class ResponseResult<T> {
    private Integer code;
    private String message;
    private boolean status;
    private T data;
    private long timestamp;

    private ResponseResult() {
        timestamp = System.currentTimeMillis();
    }

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> response = new ResponseResult<>();
        response.setCode(ResponseResultEnum.RC200.getCode());
        response.setStatus(true);
        response.setMessage(ResponseResultEnum.RC200.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> ResponseResult<T> success(String message, T data) {
        ResponseResult<T> response = new ResponseResult<>();
        response.setCode(ResponseResultEnum.RC200.getCode());
        response.setStatus(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> ResponseResult<T> error(Integer httpStatusCode, String message) {
        ResponseResult<T> response = new ResponseResult<>();
        response.setCode(httpStatusCode);
        response.setStatus(false);
        response.setMessage(message);
        return response;
    }

    public static <T> ResponseResult<T> error(Integer httpStatusCode, T data) {
        ResponseResult<T> response = new ResponseResult<>();
        response.setCode(httpStatusCode);
        response.setStatus(false);
        response.setData(data);
        return response;
    }
}
