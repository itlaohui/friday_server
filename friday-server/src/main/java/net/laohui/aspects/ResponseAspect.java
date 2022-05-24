package net.laohui.aspects;

import lombok.extern.log4j.Log4j2;
import net.laohui.util.ResponseResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
@Log4j2
public class ResponseAspect {
    HttpServletRequest httpServletRequest;
    HttpServletResponse httpServletResponse;

    @Autowired
    ResponseAspect(HttpServletRequest httpServletRequest,
                   HttpServletResponse httpServletResponse) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    /**
     * 重写HttpStatusCode
     */
    @Around("execution(* net.laohui.controller.*.*(..))")
    public Object overrideHttpStatusCode(ProceedingJoinPoint pjp) throws Throwable {
        String method = httpServletRequest.getMethod();
        String uri = httpServletRequest.getRequestURI();
        String remoteAddr = httpServletRequest.getRemoteAddr();
        Object proceed = pjp.proceed();
        if (proceed instanceof ResponseResult) {
            ResponseResult<?> result = (ResponseResult<?>) proceed;
            Integer code = result.getCode();
            result.setCode(null);
            httpServletResponse.setStatus(code);
            log.info(remoteAddr + " " + uri + " " + method + " " + code);
            return result;
        } else {
            return proceed;
        }
    }
}
