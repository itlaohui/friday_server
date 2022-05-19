package net.laohui.controller;

import net.laohui.pojo.Config;
import net.laohui.service.RemoteConfigService;
import net.laohui.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "config")
public class RemoteConfigController {

    RemoteConfigService remoteConfigService;

    HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setRemoteConfigService(RemoteConfigService remoteConfigService) {
        this.remoteConfigService = remoteConfigService;
    }

    @GetMapping(value = "getConfig")
    public ResponseResult<Config> getConfig() {
        String key = request.getParameter("key");
        Config config = remoteConfigService.getConfig(key);
        return ResponseResult.success(config);
    }

    @PostMapping(value = "setConfig")
    public ResponseResult<Config> setConfig(@RequestParam("config") Config config) {
        return ResponseResult.success(config);
    }

}
