package com.xiaobai.rpcdemo.core.provider.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xiaobai.rpcdemo.core.common.MetaInfo;
import com.xiaobai.rpcdemo.core.common.RpcProperties;
import com.xiaobai.rpcdemo.core.constant.CommonConstant;
import com.xiaobai.rpcdemo.core.exception.ServiceRegistryException;
import com.xiaobai.rpcdemo.core.provider.entity.ProviderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供者注册实现类
 *
 * @author yinzhaojing
 * @date 2021-04-29 19:52:07
 */
public class ProviderRegistry implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private ProviderServiceHolder providerServiceHolder;
    @Autowired
    private MetaInfo metaInfo;
    @Autowired
    private RpcProperties rpcProperties;

    private static final Logger logger = LoggerFactory.getLogger(ProviderRegistry.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        logger.info("start registry service into nacos...");
        try {
            String nacosAddress = rpcProperties.getNacosAddress();
            if(StringUtils.isBlank(nacosAddress)) {
                throw new ServiceRegistryException("nacos address is null");
            }
            NamingService naming = NamingFactory.createNamingService(nacosAddress);
            Instance instance = new Instance();
            String providerName = null == rpcProperties.getName() ? metaInfo.getName() : rpcProperties.getName();
            instance.setIp(metaInfo.getIp());
            instance.setPort(metaInfo.getPort());
            Map<String, List<ProviderService>> map = providerServiceHolder.getServiceMap();
            Map<String, String> instanceMeta = new HashMap<>();
            for (Map.Entry<String, List<ProviderService>> entry : map.entrySet()) {
                String interfaceName = entry.getKey();
                List<ProviderService> list = entry.getValue();
                for (ProviderService providerService : list) {
                    String impl = providerService.getImpl();
                    String group = providerService.getGroup();
                    JSONArray jsonArray = JSON.parseArray(instanceMeta.get(interfaceName));
                    if(null == jsonArray) {
                        jsonArray = new JSONArray();
                    }
                    JSONObject jsonObject =  new JSONObject();
                    jsonObject.put(CommonConstant.GROUP, group);
                    jsonObject.put(CommonConstant.IMPL, impl);
                    jsonArray.add(jsonObject);
                    instanceMeta.put(interfaceName, jsonArray.toJSONString());
                }
            }
            instance.setMetadata(instanceMeta);
            naming.registerInstance(providerName, instance);
            logger.info("registry success");
        } catch (Exception e) {
            logger.error("registry error,exception:{}", e.getMessage());
            throw new ServiceRegistryException(e.getMessage());
        }
    }
}
