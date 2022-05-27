package net.laohui.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.log4j.Log4j2;
import net.laohui.api.bean.domain.User;
import net.laohui.config.Producer;
import net.laohui.api.service.UserService;
import net.laohui.util.JWTUtil;
import net.laohui.util.RedisUtils;
import net.laohui.util.ResponseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@Controller
public class TestController {

    @Reference(version = "1.0.0", timeout = 60000)
    UserService userService;

    HttpServletRequest request;
    RedisUtils redisUtils;
    Producer producer;
    // redis登录信标
    final String LOGIN_BEACON = "_LOGIN_BEACON_";
    // redis登录信标超时时间
    final long LOGIN_BEACON_MAX_TIME = 60 * 60; // 60 * 60 * 24

    @Autowired
    public TestController(HttpServletRequest request, RedisUtils redisUtils,
                          Producer producer) {
        this.request = request;
        this.redisUtils = redisUtils;
        this.producer = producer;
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseResult<Object> test_login() {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return ResponseResult.success("已经登录");
        }
        try {
            User user = userService.getUserByUserName(username);
            Map<String, Object> userInfo = new LinkedHashMap<>();
            userInfo.put("userName", user.getUser_account_name());
            userInfo.put("nickName", user.getUser_nickname());
            userInfo.put("phoneNumber", user.getUser_phone_number());
            userInfo.put("email", user.getUser_email());
            userInfo.put("loginTime", System.currentTimeMillis());
            String jwtToken = JWTUtil.createToken(userInfo);
            redisUtils.set(LOGIN_BEACON + user.getUser_account_name(), userInfo, LOGIN_BEACON_MAX_TIME);
            return ResponseResult.success("登录成功", jwtToken);
        } catch (Exception e) {
            return ResponseResult.error(401,  e.getMessage());
        }
    }

    @GetMapping(value = "sendMessage")
    @ResponseBody
    public String sendMessage(
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "message", required = false) String message) {
        producer.sendMsg(topic, message);
        return "sendMessage";
    }

    @GetMapping(value = "user/getUser")
    @ResponseBody
    public ResponseResult<User> getUserById(@RequestParam("id") String userId) {

        User user = userService.getUserByUserId(Integer.valueOf(userId));
        return ResponseResult.success(user);
    }
}
