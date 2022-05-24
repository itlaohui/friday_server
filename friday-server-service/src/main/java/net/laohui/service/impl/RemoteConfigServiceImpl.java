package net.laohui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.laohui.api.bean.RemoteConfig;
import net.laohui.api.service.RemoteConfigService;
import net.laohui.dao.RemoteConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0", timeout = 60000)
@Transactional
public class RemoteConfigServiceImpl implements RemoteConfigService {

    RemoteConfigMapper remoteConfigMapper;

    @Autowired
    RemoteConfigServiceImpl(RemoteConfigMapper remoteConfigMapper) {
        this.remoteConfigMapper = remoteConfigMapper;
    }

    public RemoteConfig getConfig(String key) {
        RemoteConfig config = new RemoteConfig();
        config.setConfig_key(key);
        QueryWrapper<RemoteConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(config);
        return remoteConfigMapper.selectOne(queryWrapper);
    }
}
