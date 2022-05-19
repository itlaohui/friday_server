package net.laohui.controller;

import com.alibaba.fastjson.JSONObject;
import net.laohui.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("oauth")
public class OauthController {

    HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping(value = "callback_qq")
    public ResponseResult<JSONObject> callback_qq() {
        JSONObject jsonObject = new JSONObject();
        return ResponseResult.success(jsonObject);
    }

    @GetMapping(value = "callback_github")
    public ResponseResult<JSONObject> callback_github() {
        JSONObject jsonObject = new JSONObject();
        return ResponseResult.success(jsonObject);
    }

    @GetMapping(value = "callback_weibo")
    public ResponseResult<JSONObject> callback_weibo() {
        JSONObject jsonObject = new JSONObject();
        return ResponseResult.success(jsonObject);
    }

    /**
     * 获取第三方登录地址
     * @return ResponseResult
     */
    @GetMapping(value = "getOauthUrl")
    public ResponseResult<JSONObject> getOauthUrl() {
        String type = request.getParameter("type");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("url", "http://localhost:8080/oauth/callback_" + type);
        return ResponseResult.success(jsonObject);
    }

    /**
     * 第三方登录回调
     */
    public ResponseResult<JSONObject> oauthLogin() {
        JSONObject jsonObject = new JSONObject();
        return ResponseResult.success(jsonObject);
    }
}
