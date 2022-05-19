package net.laohui.api.service;


import net.laohui.api.bean.Config;

public interface RemoteConfigService {
    Config getConfig(String key);
}
