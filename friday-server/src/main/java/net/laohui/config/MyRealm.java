package net.laohui.config;

import lombok.extern.log4j.Log4j2;
import net.laohui.pojo.RequestInfo;
import net.laohui.service.JwtToken;
import net.laohui.service.UserService;
import net.laohui.util.JWTUtil;
import net.laohui.util.RedisUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

@Log4j2
@Service
public class MyRealm extends AuthorizingRealm {

    // redis登录信标
    final String LOGIN_BEACON = "_LOGIN_BEACON_";
    // redis登录信标超时时间
    final long LOGIN_BEACON_MAX_TIME = 60; // 60 * 60 * 24
    @Autowired
    UserService userService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    HttpServletResponse httpServletResponse;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        log.warn("鉴权");
        return simpleAuthorizationInfo;
    }

    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.warn("验证方法执行 --> doGetAuthenticationInfo");
        RequestInfo requestInfo = RequestInfo.create(httpServletRequest);
        redisUtils.set(requestInfo.getRequestId(), requestInfo, LOGIN_BEACON_MAX_TIME);
        log.warn("requestInfo: {}", requestInfo);
        String token = (String) authenticationToken.getCredentials();
        log.warn("读取token --> {}", token);
        Map<String, Object> userInfo = JWTUtil.verifyToken(token);
        if (userInfo == null) {
            throw new AuthenticationException("Token无效，请重新登录");
        } else {
            log.info("tokenInfo:{}", userInfo);
        }
        String username = (String) userInfo.get("userName");
        Object redisUser = redisUtils.get(RedisConfig.LOGIN_BEACON + username);
        log.warn("redis试图读取的键为：{}， redis执行状态:{}", RedisConfig.LOGIN_BEACON + username, redisUser);
        if (Objects.isNull(redisUser)) {
            log.warn("redis中没有该用户:{}", username);
            throw new AuthenticationException("Token无效，请重新登录");
            // User user = userService.getUserByUserName(username);
//            if (Objects.isNull(user)) {
//                throw new UnknownAccountException("用户名或密码错误");
//            }
//            UserStatusEnum userStatusEnum = UserStatusEnum.valueOf(user.getUser_status());
//            if (!userStatusEnum.allowLogin()) {
//                throw new LockedAccountException(userStatusEnum.getMessage());
//            }
//            if (redisUtils.set(LOGIN_BEACON + user.getUser_account_name(),  new UserProfile(user), LOGIN_BEACON_MAX_TIME)) {
//                return new SimpleAuthenticationInfo(token, token, getName());
//            } else {
//                log.error("向redis中写入用户失败:{}", new UserProfile(user));
//                throw new AuthenticationException("登录失败，服务器错误");
//            }
        } else {
            log.warn("redis中有该用户:{}", username);
            return new SimpleAuthenticationInfo(token, token, getName());
        }
    }
}
