package net.laohui.util;

import lombok.Data;
import net.laohui.enumerate.ResponseResultEnum;

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

    public static <T> ResponseResult<T> error(Integer code, String message) {
        ResponseResult<T> response = new ResponseResult<>();
        response.setCode(code);
        response.setStatus(false);
        response.setMessage(message);
        return response;
    }
}
