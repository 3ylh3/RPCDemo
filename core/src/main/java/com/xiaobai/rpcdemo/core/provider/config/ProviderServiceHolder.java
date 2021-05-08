package com.xiaobai.rpcdemo.core.provider.config;

import com.xiaobai.rpcdemo.core.provider.entity.ProviderService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供者service holder
 *
 * @author yinzhaojing
 * @date 2021-04-29 19:32:53
 */
public class ProviderServiceHolder {
    private Map<String, List<ProviderService>> remoteServiceMap;

    public ProviderServiceHolder() {
        this.remoteServiceMap = new ConcurrentHashMap<>();
    }

    public List<ProviderService> getServiceList(String className) {
        return this.remoteServiceMap.get(className);
    }

    public void setServiceList(String className, List<ProviderService> list) {
        this.remoteServiceMap.put(className, list);
    }

    public Map<String, List<ProviderService>> getServiceMap() {
        return this.remoteServiceMap;
    }
}
