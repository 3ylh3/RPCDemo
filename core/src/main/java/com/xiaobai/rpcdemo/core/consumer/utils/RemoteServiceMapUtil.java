package com.xiaobai.rpcdemo.core.consumer.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xiaobai.rpcdemo.core.consumer.entity.RemoteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 本地service缓存map操作工具类
 *
 * @author yinzhaojing
 * @date 2021-05-11 17:16:09
 */
public class RemoteServiceMapUtil {
    private static final String GROUP = "group";
    private static final String IMPL = "impl";
    private static final String URL_PREFIX = "http://";
    private static final String URL_MIDFIX = ":";

    public static void updateRemoteServiceMap(String providerName, Map<String, List<RemoteService>> remoteServiceMap, List<Instance> instances) {
        for(Instance instance : instances) {
            //获取对应元数据
            Map<String, String> instanceMetaData = instance.getMetadata();
            //循环处理元数据，获取接口名、实现类以及group，更新本地缓存
            for(Map.Entry<String, String> entry : instanceMetaData.entrySet()) {
                String interfaceName = entry.getKey();
                JSONObject jsonObject = JSON.parseObject(entry.getValue());
                String group = jsonObject.getString(GROUP);
                String impl = jsonObject.getString(IMPL);
                List<RemoteService> remoteServiceList = remoteServiceMap.get(interfaceName);
                if(null == remoteServiceList) {
                    remoteServiceList = new ArrayList<>();
                }
                RemoteService remoteService = new RemoteService();
                remoteService.setProviderName(providerName);
                remoteService.setUrl(URL_PREFIX + instance.getIp() + URL_MIDFIX + instance.getPort());
                remoteService.setGroup(group);
                remoteService.setImpl(impl);
                remoteServiceList.add(remoteService);
                remoteServiceMap.put(interfaceName, remoteServiceList);
            }
        }
    }
}
