package com.xiaobai.rpcdemo.core.consumer.config;

import com.xiaobai.rpcdemo.core.consumer.entity.RemoteService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * remote service holder bean
 *
 * @author yinzhaojing
 * @date 2021-04-28 16:46:10
 */
public class RemoteServiceHolder {
    private Map<String, List<RemoteService>> remoteServiceMap;

    public void init() {
        this.remoteServiceMap = new HashMap<>();
        RemoteService remoteService = new RemoteService();
        remoteService.setProviderName("testProvider");
        remoteService.setImpl("com.xiaobai.rpctest.test.TestImpl");
        remoteService.setUrl("http://127.0.0.1:8081");
        List<RemoteService> list = new ArrayList<>();
        list.add(remoteService);
        this.remoteServiceMap.put("com.xiaobai.rpctest.test.TestService", list);
    }

    public List<RemoteService> getRemoteService(String name) {
        synchronized (this.remoteServiceMap) {
            return this.remoteServiceMap.get(name);
        }
    }
}
