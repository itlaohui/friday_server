package net.laohui.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.log4j.Log4j2;
import net.laohui.api.bean.domain.User;
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
    public ResponseResult<Map<String, Object>> login(@RequestBody(required = false) UserRequestBody userRequestBody) {
        //body???????????????
        String userBodyUsername = Objects.isNull(userRequestBody) ? null : userRequestBody.getUsername();
        String userBodyPassword = Objects.isNull(userRequestBody) ? null : userRequestBody.getPassword();
//        String userBodyCode = Objects.isNull(userRequestBody) ? null : userRequestBody.getCode();
        //?????????????????????
        String parameterUsername = request.getParameter("username");
        String parameterPassword = request.getParameter("password");
        String parameterRedirect = request.getParameter("redirect");
        String parameterUserAgent = request.getHeader("User-Agent");
        //????????????????????????????????????????????????
        String username = isBlank(userBodyUsername) ? parameterUsername : userBodyUsername;
        String password = isBlank(userBodyPassword) ? parameterPassword : userBodyPassword;
        UserStatusEnum userStatus;
        if (isBlank(username) || isBlank(password)) {
            return ResponseResult.error(400, "?????????????????????????????????");
        }
        User user = userService.checkUser(username, password);
        if (user != null && user.getUser_id() > 0) {
            try {
                userStatus = UserStatusEnum.valueOf(user.getUser_status());
            } catch (IllegalArgumentException e) {
                log.error("??????????????????????????????!user={}", user);
                return ResponseResult.error(500, "??????????????????????????????????????????");
            } if (!userStatus.allowLogin()) {
                log.error("???????????????????????????????????????!user={}", user);
                return ResponseResult.error(500, userStatus.getMessage());
            }
            user.setLogin_ip(request.getRemoteHost());
            user.setLogin_time(new Date());
            user.setLogin_city(request.getRemoteAddr());
            user.setLogin_browser_ua(parameterUserAgent);
            userService.updateUserByUserId(user.getUser_id(), user);
            UserProfile userProfile = new UserProfile(user);
            if (!redisUtils.set(RedisConfig.LOGIN_BEACON + user.getUser_account_name(),userProfile, LOGIN_BEACON_MAX_TIME)) {
                log.error("Redis????????????????????????{}", userProfile);
                throw new OperationException("?????????????????????????????????");
            }
            Map<String, Object> userInfo = userProfile.asMap();
            String token = JWTUtil.createToken(userInfo);
            if (isBlank(token)) {
                log.error("JWT????????????!user={}", user);
                throw new OperationException("?????????????????????????????????");
            }
            userInfo.put("token", token);
            return ResponseResult.success(userInfo);
        } else {
            throw new PermissionsException("???????????????????????????????????????");
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
        User status = null;
        try {
            status = userService.addUser(user);
        } catch (DuplicateKeyException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("SQLIntegrityConstraintViolationException")) {
                return ResponseResult.error(500,"??????????????????");
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
            return ResponseResult.error(401, "???????????????????????????!");
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
            return ResponseResult.error(401, "???????????????????????????!");
        } else {
            redisUtils.del(RedisConfig.LOGIN_BEACON + userName);
            if (redisUtils.hasKey(RedisConfig.LOGIN_BEACON + userName)) {
                log.error("Redis????????????????????????{}", userName);
            } else {
                log.info("Redis????????????????????????{}", userName);
            }
            return ResponseResult.success("???????????????");
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
