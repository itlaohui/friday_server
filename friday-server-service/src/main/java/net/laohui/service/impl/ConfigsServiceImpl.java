package net.laohui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.laohui.api.bean.ConfigExt;
import net.laohui.api.bean.domain.Configs;
import net.laohui.api.service.ConfigsService;
import net.laohui.dao.ConfigExtMapper;
import net.laohui.dao.ConfigsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0", timeout = 60000)
@Transactional
public class ConfigsServiceImpl implements ConfigsService {

    ConfigsMapper configsMapper;
    ConfigExtMapper configExtMapper;

    @Autowired
    ConfigsServiceImpl(ConfigsMapper configsMapper,
                       ConfigExtMapper configExtMapper) {
        this.configsMapper = configsMapper;
        this.configExtMapper = configExtMapper;
    }

    public Configs getConfig(String name) {
        return configsMapper.getConfig(name);
    }

    @Override
    public Page<Configs> getConfigList(String keywords, int page, int size) {
        Page<Configs> pageOffset = new Page<>(page, size, false);
        QueryWrapper<Configs> queryWrapper = new QueryWrapper<>();
        queryWrapper.or(e -> {
            e.like("name", keywords);
            e.like("title", keywords);
        });
        return configsMapper.selectPage(pageOffset, queryWrapper);
    }

    @Override
    public Page<ConfigExt> getConfigExtList(String keywords, int page, int size) {
        Page<ConfigExt> pageOffset = new Page<>(page, size, false);
        if (keywords.equals("")) {
            return pageOffset;
        }
        QueryWrapper<ConfigExt> queryWrapper = new QueryWrapper<>();
        queryWrapper.or(e -> {
            e.like("key", keywords);
            e.like("value", keywords);
            e.like("title", keywords);
        });
        return configExtMapper.selectPage(pageOffset, queryWrapper);
    }
}
