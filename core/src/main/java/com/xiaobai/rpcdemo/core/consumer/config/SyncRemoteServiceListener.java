package com.xiaobai.rpcdemo.core.consumer.config;

import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xiaobai.rpcdemo.core.consumer.entity.RemoteService;
import com.xiaobai.rpcdemo.core.consumer.utils.RemoteServiceMapUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * nacos provider监听listener
 *
 * @author yinzhaojing
 * @date 2021-05-11 16:52:14
 */
public class SyncRemoteServiceListener implements EventListener {
    private Map<String, List<RemoteService>> remoteServiceMap;

    private static final Logger logger = LoggerFactory.getLogger(SyncRemoteServiceListener.class);

    public SyncRemoteServiceListener(Map<String, List<RemoteService>> remoteServiceMap) {
        this.remoteServiceMap = remoteServiceMap;
    }

    public void setRemoteServiceMap(Map<String, List<RemoteService>> remoteServiceMap) {
        this.remoteServiceMap = remoteServiceMap;
    }

    /**
     * 监听nacos中provider变化事件，更新本地缓存
     * @param event
     */
    @Override
    public void onEvent(Event event) {
        String providerName = ((NamingEvent) event).getServiceName();
        logger.info("{} changed,start update local cache",providerName);
        //清除本地缓存
        for(Map.Entry<String, List<RemoteService>> entry : this.remoteServiceMap.entrySet()) {
            String interfaceName = entry.getKey();
            List<RemoteService> remoteServiceList = entry.getValue();
            List<RemoteService> tmpList = new ArrayList<>();
            for(RemoteService remoteService : remoteServiceList) {
                if(!StringUtils.equals(providerName, remoteService.getProviderName())) {
                    tmpList.add(remoteService);
                }
            }
            this.remoteServiceMap.put(interfaceName, tmpList);
        }
        List<Instance> list = ((NamingEvent) event).getInstances();
        RemoteServiceMapUtil.updateRemoteServiceMap(providerName, this.remoteServiceMap, list);
        logger.info("update local cache success,providerName:{}", providerName);
    }
}
