package net.laohui.api.service;


import net.laohui.api.bean.RemoteConfig;

public interface RemoteConfigService {
    RemoteConfig getConfig(String key);
}
