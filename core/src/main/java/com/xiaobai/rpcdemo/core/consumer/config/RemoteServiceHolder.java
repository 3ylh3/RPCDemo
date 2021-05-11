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
    private SyncRemoteServiceListener syncRemoteServiceListener;

    private static final int PAGE_NO = 0;
    private static final int PAGE_SIZE = 65536;
    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceHolder.class);

    public void init(RpcProperties rpcProperties) {
        logger.info("consumer init start...");
        this.remoteServiceMap = new ConcurrentHashMap<>();
        this.providerSet = new ConcurrentHashSet<>();
        this.syncRemoteServiceListener = new SyncRemoteServiceListener(this.remoteServiceMap);
        try{
            if(StringUtils.isBlank(rpcProperties.getNacosAddress())) {
                throw new ConsumerSyncException("nacos address is null");
            }
            NamingService naming = NamingFactory.createNamingService(rpcProperties.getNacosAddress());
            //获取nacos中注册的所有服务
            ListView<String> services = naming.getServicesOfServer(PAGE_NO, PAGE_SIZE);
            List<String> list = services.getData();
            for(String service : list) {
                //缓存providerName
                this.providerSet.add(service);
                //获取服务对应健康实例
                List<Instance> instances = naming.selectInstances(service, true);
                RemoteServiceMapUtil.updateRemoteServiceMap(service, this.remoteServiceMap, instances);
            }
            //监听提供者
            for(String provider : this.providerSet) {
                naming.subscribe(provider, this.syncRemoteServiceListener);
            }
            logger.info("consumer init success");
        } catch (Exception e) {
            logger.error("consumer init exception:{}", e.getMessage());
            throw new ConsumerSyncException(e.getMessage());
        }
    }

    public List<RemoteService> getRemoteService(String name) {
        return this.remoteServiceMap.get(name);
    }
}
