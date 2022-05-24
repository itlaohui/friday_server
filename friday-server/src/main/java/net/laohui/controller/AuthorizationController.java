package net.laohui.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import net.laohui.api.service.UserService;
import net.laohui.config.RedisConfig;
import net.laohui.enumerate.UserStatusEnum;
import net.laohui.api.bean.User;
import net.laohui.api.bean.UserRequestBody;
import net.laohui.api.bean.UserProfile;
import net.laohui.util.JWTUtil;
import net.laohui.util.RedisUtils;
import net.laohui.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;

import static net.laohui.config.RedisConfig.LOGIN_BEACON_MAX_TIME;
import static org.apache.logging.log4j.util.Strings.isBlank;

/**
 * 验证控制器
 * 用以校验其他应用持有令牌是否经过验证
 */

@Log4j2
@RestController
public class AuthorizationController {
    @Reference(version = "1.0.0", timeout = 60000)
    UserService userService;

    HttpServletRequest request;

    HttpServletResponse response;

    RedisUtils redisUtils;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @RequestMapping(value = "login", method = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET })
    @ResponseBody
    public ResponseResult<JSONObject> login() {
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
            log.info("select user is : {}", user.toString());
            user.setLogin_ip(request.getRemoteHost());
            user.setLogin_time(new Date());
            user.setLogin_city(request.getRemoteAddr());
            user.setLogin_browser_ua(parameterUserAgent);
            userService.updateUserByUserId(user.getUser_id(), user);
            UserProfile userProfile = new UserProfile(user);
            if (!redisUtils.set(RedisConfig.LOGIN_BEACON + user.getUser_account_name(),userProfile, LOGIN_BEACON_MAX_TIME)) {
                log.error("Redis用户信息写入失败{}", userProfile);
                return ResponseResult.error(500, "登录失败！");
            }
            String token = JWTUtil.createToken(userProfile.asMap());
            JSONObject JSON = new JSONObject(userProfile.asMap());
            JSON.put("redirect", parameterRedirect);
            JSON.put("username", user.getUser_account_name());
            JSON.put("nickname", user.getUser_nickname());
            JSON.put("sex", user.getUser_sex());
            JSON.put("token", token);
            return ResponseResult.success(JSON);
        } else {
            return ResponseResult.error(400, "登录失败！用户名或密码错误！");
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

}
