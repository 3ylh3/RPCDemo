package com.xiaobai.rpcdemo.core.consumer.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.xiaobai.rpcdemo.core.common.RpcProperties;
import com.xiaobai.rpcdemo.core.consumer.entity.RemoteService;
import com.xiaobai.rpcdemo.core.exception.ConsumerSyncException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * remote service holder bean
 *
 * @author yinzhaojing
 * @date 2021-04-28 16:46:10
 */
public class RemoteServiceHolder {
    private Map<String, List<RemoteService>> remoteServiceMap;

    private static final int PAGE_NO = 0;
    private static final int PAGE_SIZE = 65536;
    private static final String GROUP = "group";
    private static final String IMPL = "impl";
    private static final String URL_PREFIX = "http://";
    private static final String URL_MIDFIX = ":";
    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceHolder.class);

    public void init(RpcProperties rpcProperties) {
        logger.info("consumer init start...");
        this.remoteServiceMap = new ConcurrentHashMap<>();
        try{
            if(StringUtils.isBlank(rpcProperties.getNacosAddress())) {
                throw new ConsumerSyncException("nacos address is null");
            }
            NamingService naming = NamingFactory.createNamingService(rpcProperties.getNacosAddress());
            //获取nacos中注册的所有服务
            ListView<String> services = naming.getServicesOfServer(PAGE_NO, PAGE_SIZE);
            List<String> list = services.getData();
            for(String service : list) {
                //获取服务对应健康实例
                List<Instance> instances = naming.selectInstances(service, true);
                for(Instance instance : instances) {
                    //获取对应元数据
                    Map<String, String> instanceMetaData = instance.getMetadata();
                    //循环处理元数据，获取接口名、实现类以及group，缓存到本地
                    for(Map.Entry<String, String> entry : instanceMetaData.entrySet()) {
                        String interfaceName = entry.getKey();
                        JSONObject jsonObject = JSON.parseObject(entry.getValue());
                        String group = jsonObject.getString(GROUP);
                        String impl = jsonObject.getString(IMPL);
                        List<RemoteService> remoteServiceList = getRemoteService(interfaceName);
                        if(null == remoteServiceList) {
                            remoteServiceList = new ArrayList<>();
                        }
                        RemoteService remoteService = new RemoteService();
                        remoteService.setProviderName(service);
                        remoteService.setUrl(URL_PREFIX + instance.getIp() + URL_MIDFIX + instance.getPort());
                        remoteService.setGroup(group);
                        remoteService.setImpl(impl);
                        remoteServiceList.add(remoteService);
                        this.remoteServiceMap.put(interfaceName, remoteServiceList);
                    }
                }
            }
            services.getCount();
        } catch (Exception e) {
            logger.error("consumer init exception:{}", e);
            throw new ConsumerSyncException(e.getMessage());
        }
    }

    public List<RemoteService> getRemoteService(String name) {
        return this.remoteServiceMap.get(name);
    }

    /**
     * 更新本地缓存
     */
    private void syncRemoteService() {

    }
}
