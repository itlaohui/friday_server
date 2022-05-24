package net.laohui.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.log4j.Log4j2;
import net.laohui.api.bean.User;
import net.laohui.api.service.UserService;
import net.laohui.config.RedisConfig;
import net.laohui.enumerate.UserStatusEnum;
import net.laohui.exception.OperationException;
import net.laohui.exception.PermissionsException;
import net.laohui.api.bean.UserRequestBody;
import net.laohui.api.bean.UserProfile;
import net.laohui.util.JWTUtil;
import net.laohui.util.RedisUtils;
import net.laohui.util.ResponseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static net.laohui.config.RedisConfig.LOGIN_BEACON_MAX_TIME;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Log4j2
@Controller
@RequestMapping(value = "user")
public class UserController {

    @Reference(version = "1.0.0", timeout = 60000)
    UserService userService;
    HttpServletRequest request;
    HttpServletResponse response;
    RedisUtils redisUtils;

    @Autowired
    public UserController(HttpServletRequest request, HttpServletResponse response,
                          RedisUtils redisUtils) {
        this.request = request;
        this.response = response;
        this.redisUtils = redisUtils;
    }

    @RequestMapping(value = "login", method = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET })
    @ResponseBody
    public ResponseResult<Map<String, Object>> login() {
        //body提交走这里
        UserRequestBody userBody = new UserRequestBody();
        String userBodyUsername = Objects.isNull(userBody) ? null : userBody.getUsername();
        String userBodyPassword = Objects.isNull(userBody) ? null : userBody.getPassword();
        String userBodyCode = Objects.isNull(userBody) ? null : userBody.getCode();
        //其他方式走这里
        String parameterUsername = request.getParameter("username");
        String parameterPassword = request.getParameter("password");
        String parameterRedirect = request.getParameter("redirect");
        String parameterUserAgent = request.getHeader("User-Agent");
        //确保所有请求方式都可以取得请求值
        String username = isBlank(userBodyUsername) ? parameterUsername : userBodyUsername;
        String password = isBlank(userBodyPassword) ? parameterPassword : userBodyPassword;
        UserStatusEnum userStatus;
        if (isBlank(username) || isBlank(password)) {
            return ResponseResult.error(400, "账户名和密码均为必须！");
        }
        User user = userService.checkUser(username, password);
        if (user != null && user.getUser_id() > 0) {
            try {
                userStatus = UserStatusEnum.valueOf(user.getUser_status());
            } catch (IllegalArgumentException e) {
                log.error("用户状态信息读取异常!user={}", user);
                return ResponseResult.error(500, "账户状态异常！暂时无法登录！");
            } if (!userStatus.allowLogin()) {
                log.error("禁止登录的用户正在尝试登录!user={}", user);
                return ResponseResult.error(500, userStatus.getMessage());
            }
            user.setLogin_ip(request.getRemoteHost());
            user.setLogin_time(new Date());
            user.setLogin_city(request.getRemoteAddr());
            user.setLogin_browser_ua(parameterUserAgent);
            userService.updateUserByUserId(user.getUser_id(), user);
            UserProfile userProfile = new UserProfile(user);
            if (!redisUtils.set(RedisConfig.LOGIN_BEACON + user.getUser_account_name(),userProfile, LOGIN_BEACON_MAX_TIME)) {
                log.error("Redis用户信息写入失败{}", userProfile);
                throw new OperationException("登录失败！服务器异常！");
            }
            Map<String, Object> userInfo = userProfile.asMap();
            String token = JWTUtil.createToken(userInfo);
            if (isBlank(token)) {
                log.error("JWT生成失败!user={}", user);
                throw new OperationException("登录失败！服务器异常！");
            }
            userInfo.put("token", token);
            return ResponseResult.success(userInfo);
        } else {
            throw new PermissionsException("登录失败！账号或密码错误！");
        }

    }

    @PostMapping(value = "register")
    @ResponseBody
    public ResponseResult<Object> register(HttpServletRequest httpServletRequest) {
        User user = new User();
        user.setUser_account_name(request.getParameter("username"));
        user.setUser_account_password(request.getParameter("password"));
        user.setUser_nickname(request.getParameter("nickname"));
        user.setUser_email(request.getParameter("email"));
        user.setUser_phone_number(request.getParameter("phonenumber"));
        HttpSession session = httpServletRequest.getSession();
        System.out.println(session);
        String sex = request.getParameter("sex");
        if (!isBlank(sex)) {
            int user_sex = Integer.parseInt(sex);
            if (user_sex >= 0 && user_sex <= 2) {
                user.setUser_sex(user_sex);
            } else {
                user.setUser_sex(2);
            }
        } else {
            user.setUser_sex(2);
        }
        boolean status = false;
        try {
            status = userService.addUser(user);
        } catch (DuplicateKeyException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("SQLIntegrityConstraintViolationException")) {
                return ResponseResult.error(500,"用户已存在！");
            }
        }

        return ResponseResult.success(status);

    }

    @RequiresAuthentication
    @GetMapping(value = "info")
    @ResponseBody
    public ResponseResult<LinkedHashMap> getUserInfo() {
        String authorization = request.getHeader("Authorization");
        Map<String, Object> tokenInfo = JWTUtil.verifyToken(authorization);
        Object userName = tokenInfo.get("userName");
        if (userName == null) {
            return ResponseResult.error(401, "状态异常，请先登录!");
        }
        LinkedHashMap userProfile = (LinkedHashMap) redisUtils.get(RedisConfig.LOGIN_BEACON + userName);

        return ResponseResult.success(userProfile);
    }

    @GetMapping(value = "logout")
    @ResponseBody
    public ResponseResult<String> logout() {
        String authorization = request.getHeader("Authorization");
        Map<String, Object> tokenInfo = JWTUtil.verifyToken(authorization);
        Object userName = tokenInfo.get("userName");
        if (userName == null) {
            return ResponseResult.error(401, "状态异常，请先登录!");
        } else {
            redisUtils.del(RedisConfig.LOGIN_BEACON + userName);
            if (redisUtils.hasKey(RedisConfig.LOGIN_BEACON + userName)) {
                log.error("Redis用户信息删除失败{}", userName);
            } else {
                log.info("Redis用户信息删除成功{}", userName);
            }
            return ResponseResult.success("退出成功！");
        }
    }

    @GetMapping(value = "sendCode")
    @ResponseBody
    public ResponseResult<String> sendCode() {
        return ResponseResult.success("ok");
    }

    @RequiresAuthentication
    @RequestMapping(value = "hasLogin", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    @ResponseBody
    public ResponseResult<String> hasUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return ResponseResult.success("ok");
        }
        return ResponseResult.error(401, "need login");
    }
}
