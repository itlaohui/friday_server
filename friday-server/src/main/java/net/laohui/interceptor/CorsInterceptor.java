package net.laohui.interceptor;

import lombok.extern.log4j.Log4j2;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@Log4j2
@Component
public class CorsInterceptor implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CorsInterceptor init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.warn("JwtFilter -> preHandle");
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        String origin = isNotEmpty(httpServletRequest.getHeader("Origin")) ? httpServletRequest.getHeader("Origin") : "*";
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.addHeader("Access-Control-Allow-Origin", origin);
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Authorization");
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
        }
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            log.error("CorsInterceptor -> doFilter", e);
        }
    }

    @Override
    public void destroy() { }


}
