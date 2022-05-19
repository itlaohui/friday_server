package net.laohui.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import net.laohui.service.JwtToken;
import net.laohui.service.errorHandler.authenticationException;
import net.laohui.util.ResponseResult;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@Log4j2
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        log.warn("JwtFilter -> isAccessAllowed -> mappedValue: {}", request.get);
        if (WebUtils.toHttp(request).getMethod().equals("OPTIONS")) {
            // OPTIONS请求不检查token
            return true;
        }
        if (this.isLoginAttempt(request, response)) {
            // 有头部信息，验证头部信息
            try {
                this.executeLogin(request, response);
                return true;
            } catch (Exception e) {
                return true;
            }
        } else {
            return true;
        }
     }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        String authorization = this.getAuthzHeader(request);
        log.warn("JwtFilter -> executeLogin -> Authorization: {}", authorization);
        JwtToken jwtToken = new JwtToken(authorization);
        this.getSubject(request, response).login(jwtToken);
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        this.sendChallenge(request, response);
        return true;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String authorization = this.getAuthzHeader(request);
        return isNotEmpty(authorization);
    }

    private void response401(ServletResponse response, String msg) {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String value = objectMapper.writeValueAsString(ResponseResult.error(401, msg));
            log.warn("JwtFilter->response401已返回401: {}", value);
            out.append(value);
        } catch (IOException e) {
            log.error("直接返回Response信息出现IOException异常:{}", e.getMessage());
            throw new authenticationException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }

//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        log.warn("JwtFilter -> preHandle");
//        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
//        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
//        String origin = isNotEmpty(httpServletRequest.getHeader("Origin")) ? httpServletRequest.getHeader("Origin") : "*";
//        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
//        httpServletResponse.addHeader("Access-Control-Allow-Origin", origin);
//        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
//        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Authorization, token");
//        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
//        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//        }
//        return super.preHandle(httpServletRequest, httpServletResponse);
//    }
}
