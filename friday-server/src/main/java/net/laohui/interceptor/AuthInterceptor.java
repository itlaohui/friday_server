package net.laohui.interceptor;

import lombok.extern.log4j.Log4j2;
import net.laohui.service.errorHandler.authenticationException;
import net.laohui.util.JWTUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

@Log4j2
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String authorization = request.getHeader("authorization");
        log.warn("authorization: {}", authorization);
        Map<String, Object> userInfo = JWTUtil.verifyToken(authorization);
        if (Objects.isNull(userInfo)) {
            throw new authenticationException("用户未登录");
        }
        request.setAttribute("userInfo",userInfo);
        return true;
    }
}
