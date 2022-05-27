package net.laohui.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import net.laohui.api.bean.domain.Configs;
import net.laohui.api.service.ConfigsService;
import net.laohui.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "config")
public class RemoteConfigController {

    @Reference(version = "1.0.0", timeout = 60000)
    ConfigsService configsService;

    HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping(value = "getConfig")
    public ResponseResult<Configs> getConfig() {
        String key = request.getParameter("key");
        Configs config = configsService.getConfig(key);
        return ResponseResult.success(config);
    }

    @PostMapping(value = "setConfig")
    public ResponseResult<Configs> setConfig(@RequestParam("config") Configs config) {
        return ResponseResult.success(config);
    }

}
