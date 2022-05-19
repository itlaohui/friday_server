package net.laohui.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class REQUEST {
    private static HttpServletRequest request;
    private static HttpServletResponse response;

    private REQUEST() {
    }

    public static HttpServletRequest getRequest() {
        return request;
    }

    public static void setRequest(HttpServletRequest request) {
        REQUEST.request = request;
    }

    public static HttpServletResponse getResponse() {
        return response;
    }

    public static void setResponse(HttpServletResponse response) {
        REQUEST.response = response;
    }
}
