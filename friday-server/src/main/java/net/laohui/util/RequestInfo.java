package net.laohui.util;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class RequestInfo implements Serializable {
    private String requestId;
    private Long requestTime;
    private String requestMethod;
    private Map<String, String[]> requestBody;
    private String requestQueryString;
    private String requestUrl;
    private Map<String, String> requestHeader;
    private String requestRemoteAddress;

    public static RequestInfo create(HttpServletRequest request) {
        RequestInfo requestInfo = new RequestInfo();
        UUID uuid = UUID.randomUUID();
        requestInfo.setRequestId(uuid.toString());
        requestInfo.setRequestMethod(request.getMethod());
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = new LinkedHashMap<>();
        while (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            headers.put(s, request.getHeader(s));
        }
        requestInfo.setRequestQueryString(request.getQueryString());
        requestInfo.setRequestHeader(headers);
        requestInfo.setRequestRemoteAddress(request.getRemoteAddr());
        requestInfo.setRequestUrl(request.getRequestURL().toString());
        if (request.getMethod().equals("POST")) {
            requestInfo.setRequestBody(request.getParameterMap());
        }
        return requestInfo;
    }

    private RequestInfo() {
        this.requestTime = System.currentTimeMillis();
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, Object> asMap() {
        return ClassToMap.asMap(this);
    }
}
