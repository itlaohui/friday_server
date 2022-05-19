package net.laohui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.laohui.mapper.ConfigMapper;
import net.laohui.pojo.Config;
import net.laohui.service.RemoteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoteConfigServiceImpl implements RemoteConfigService {

    ConfigMapper configMapper;

    @Autowired
    public void setConfigMapper(ConfigMapper configMapper) {
        this.configMapper = configMapper;
    }

    public Config getConfig(String key) {
        Config config = new Config();
        config.setConfig_key(key);
        QueryWrapper<Config> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(config);
        return configMapper.selectOne(queryWrapper);
    }
}
