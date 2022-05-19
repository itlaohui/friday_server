package net.laohui.controller;

import net.laohui.util.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CaptchaController {

    @GetMapping(value = "captchaImage")
    @ResponseBody
    public String captchaImage() {
        return "";
    }


    //获取验证码
    @GetMapping(value = "captcha")
    @ResponseBody
    public String captcha() {
        return "";
    }


    //获取验证码
    @GetMapping(value = "captcha/{id}")
    @ResponseBody
    public String captcha(String id) {
        return "";
    }

    //获取验证码
    @GetMapping(value = "getCode")
    @ResponseBody
    public ResponseResult<String> getCode() {
        return ResponseResult.success("验证码发送成功", null);
    }
}
