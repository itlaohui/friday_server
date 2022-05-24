package net.laohui.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import net.laohui.api.bean.RemoteConfig;
import net.laohui.api.service.RemoteConfigService;
import net.laohui.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "config")
public class RemoteConfigController {

    @Reference(version = "1.0.0", timeout = 60000)
    RemoteConfigService remoteConfigService;

    HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping(value = "getConfig")
    public ResponseResult<RemoteConfig> getConfig() {
        String key = request.getParameter("key");
        RemoteConfig config = remoteConfigService.getConfig(key);
        return ResponseResult.success(config);
    }

    @PostMapping(value = "setConfig")
    public ResponseResult<RemoteConfig> setConfig(@RequestParam("config") RemoteConfig config) {
        return ResponseResult.success(config);
    }

}
