package com.xiaobai.rpcdemo.core.consumer.config;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import com.xiaobai.rpcdemo.core.common.RpcProperties;
import com.xiaobai.rpcdemo.core.consumer.entity.RemoteService;
import com.xiaobai.rpcdemo.core.consumer.utils.RemoteServiceMapUtil;
import com.xiaobai.rpcdemo.core.exception.ConsumerSyncException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * remote service holder bean
 *
 * @author yinzhaojing
 * @date 2021-04-28 16:46:10
 */
public class RemoteServiceHolder {
    private Map<String, List<RemoteService>> remoteServiceMap;
    private Set<String> providerSet;
    private Map<String, Integer> orderMap;
    private NamingService naming;
    private SyncRemoteServiceListener syncRemoteServiceListener;

    private static final int PAGE_NO = 0;
    private static final int PAGE_SIZE = 65536;
    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceHolder.class);

    public void init(RpcProperties rpcProperties) {
        logger.info("consumer init start...");
        this.remoteServiceMap = new ConcurrentHashMap<>();
        this.providerSet = new ConcurrentHashSet<>();
        this.orderMap = new ConcurrentHashMap<>();
        this.syncRemoteServiceListener = new SyncRemoteServiceListener(this.remoteServiceMap);
        try{
            if(StringUtils.isBlank(rpcProperties.getNacosAddress())) {
                throw new ConsumerSyncException("nacos address is null");
            }
            this.naming = NamingFactory.createNamingService(rpcProperties.getNacosAddress());
            queryRemoteService(this.remoteServiceMap);
            logger.info("consumer init success");
        } catch (Exception e) {
            logger.error("consumer init exception:{}", e.getMessage());
            throw new ConsumerSyncException(e.getMessage());
        }
    }

    public List<RemoteService> getRemoteService(String providerName, String group, String name) {
        List<RemoteService> result = new ArrayList<>();
        List<RemoteService> list =  this.remoteServiceMap.get(name);
        for(RemoteService remoteService : list) {
            if(!StringUtils.isBlank(providerName) && !StringUtils.equals(remoteService.getProviderName(), providerName)) {
                continue;
            }
            if(!StringUtils.isBlank(group) && !StringUtils.equals(remoteService.getGroup(), group)) {
                continue;
            }
            result.add(remoteService);
        }
        return result;
    }

    /**
     * ??????30???????????????nacos???????????????????????????
     */
    @Scheduled(cron = "0/30 * * * * ? ")
    private void syncRemoteService() {
        logger.info("start sync remote service...");
        Map<String, List<RemoteService>> tmpMap = new ConcurrentHashMap<>();
        queryRemoteService(tmpMap);
        this.remoteServiceMap = tmpMap;
        this.syncRemoteServiceListener.setRemoteServiceMap(this.remoteServiceMap);
        logger.info("sync success");
    }

    /**
     * ??????nacos??????????????????????????????????????????
     * @return
     */
    private void queryRemoteService(Map<String, List<RemoteService>> remoteServiceMap) {
        try {
            //??????nacos????????????????????????
            ListView<String> services = this.naming.getServicesOfServer(PAGE_NO, PAGE_SIZE);
            List<String> list = services.getData();
            for (String service : list) {
                //??????providerName
                this.providerSet.add(service);
                //??????????????????????????????
                List<Instance> instances = this.naming.selectInstances(service, true);
                RemoteServiceMapUtil.updateRemoteServiceMap(service, remoteServiceMap, instances);
            }
            //???????????????
            for(String provider : this.providerSet) {
                this.naming.subscribe(provider, this.syncRemoteServiceListener);
            }
        } catch (Exception e){
            logger.error("sync remote service exception:{}", e.getMessage());
            throw new ConsumerSyncException(e.getMessage());
        }
    }

    public void setOrder(String key, Integer order) {
        this.orderMap.put(key, order);
    }

    public Integer getOrder(String key) {
        return this.orderMap.get(key);
    }
}
