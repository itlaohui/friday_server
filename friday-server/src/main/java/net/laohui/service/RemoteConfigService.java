package net.laohui.service;

import net.laohui.pojo.Config;

public interface RemoteConfigService {
    Config getConfig(String key);
}
