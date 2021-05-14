package com.xiaobai.rpcdemo.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.xiaobai.rpcdemo.web.bo.CommonResponseBO;
import com.xiaobai.rpcdemo.web.bo.GetServiceListResponseBO;
import com.xiaobai.rpcdemo.web.constants.CommonConstants;
import com.xiaobai.rpcdemo.web.enums.ResponseCodeEnum;
import com.xiaobai.rpcdemo.web.service.ServiceManageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务管理实现类
 *
 * @author yinzhaojing
 * @date 2021-05-13 17:38:42
 */
@Service
public class ServiceManageImpl implements ServiceManageService {
    @Autowired
    private NamingService namingService;
    @Value("${nacos.address}")
    private String nacosAddress;
    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ServiceManageImpl.class);

    @Override
    public GetServiceListResponseBO getServiceList(String serviceName, String providerName, String group, Integer offset, Integer limit) {
        GetServiceListResponseBO responseBO = new GetServiceListResponseBO();
        logger.info("get service list start,serviceName:{},providerName:{}.group:{}", serviceName, providerName, group);
        try{
            //获取所有应用
            ListView<String> providers = namingService.getServicesOfServer(CommonConstants.PAGE_NO, CommonConstants.PAGE_SIZE);
            List<String> providerList = providers.getData();
            List<com.xiaobai.rpcdemo.web.entity.Service> list = new ArrayList<>();
            for(String provider : providerList) {
                //获取所有健康实例
                if(StringUtils.isBlank(providerName) || StringUtils.equals(provider, providerName)) {
                    List<Instance> instances = namingService.selectInstances(provider, true);
                    for(Instance instance : instances) {
                        //获取元数据
                        Map<String, String> instanceMetaData = instance.getMetadata();
                        for(Map.Entry<String, String> entry : instanceMetaData.entrySet()) {
                            String interfaceName = entry.getKey();
                            if(StringUtils.isBlank(serviceName) || StringUtils.equals(serviceName, interfaceName)) {
                                JSONArray jsonArray = JSON.parseArray(entry.getValue());
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String groupName = jsonObject.getString(CommonConstants.GROUP);
                                    if(StringUtils.isBlank(group) || StringUtils.equals(groupName, group)) {
                                        String impl = jsonObject.getString(CommonConstants.IMPL);
                                        Double weight = jsonObject.getDouble(CommonConstants.WEIGHT);
                                        com.xiaobai.rpcdemo.web.entity.Service service = new com.xiaobai.rpcdemo.web.entity.Service();
                                        service.setServiceName(interfaceName);
                                        service.setProviderName(provider);
                                        service.setGroup(groupName);
                                        service.setImpl(impl);
                                        service.setUrl(CommonConstants.URL_PREFIX + instance.getIp() + CommonConstants.URL_MIDFIX + instance.getPort());
                                        service.setWeight(weight);
                                        list.add(service);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(list.isEmpty()) {
                logger.error("get service list error,no data");
                responseBO.setResponseCode(ResponseCodeEnum.NO_DATA.getKey());
                responseBO.setResponseMessage(ResponseCodeEnum.NO_DATA.getDesc());
            }
            List<com.xiaobai.rpcdemo.web.entity.Service> result = new ArrayList<>();
            //分页处理
            for(int i = offset; i < list.size() && result.size() < limit;i++) {
                result.add(list.get(i));
            }
            logger.info("get service list success");
            responseBO.setRows(result);
            responseBO.setTotal(list.size());
            responseBO.setResponseCode(ResponseCodeEnum.SUCCESS.getKey());
            responseBO.setResponseMessage(ResponseCodeEnum.SUCCESS.getDesc());
        } catch (Exception e) {
            logger.error("call nacos error:{}", e.getMessage());
            responseBO.setResponseCode(ResponseCodeEnum.ERROR.getKey());
            responseBO.setResponseMessage(e.getMessage());
        }
        return responseBO;
    }

    /**
     * 设置服务权重
     * @param url 提供者url
     * @param serviceName 服务名
     * @param impl 实现类
     * @param weight 权重
     * @return
     */
    @Override
    public CommonResponseBO setServiceWeight(String providerName, String url, String serviceName, String impl, Double weight) {
        CommonResponseBO responseBO = new CommonResponseBO();
        logger.info("set service weight start,providerName:{},url:{},serviceName:{},impl:{},weight:{}", providerName, url, serviceName, impl, weight);
        try {
            //获取对应实例
            List<Instance> list = namingService.getAllInstances(providerName);
            String ip = url.split(CommonConstants.URL_PREFIX)[1].split(CommonConstants.URL_MIDFIX)[0];
            int port = Integer.parseInt(url.split(CommonConstants.URL_PREFIX)[1].split(CommonConstants.URL_MIDFIX)[1]);
            for(Instance instance : list) {
                String instanceIp = instance.getIp();
                int instancePort = instance.getPort();
                if(StringUtils.equals(instanceIp, ip) && instancePort == port) {
                    Map<String, String> instanceMetaData = instance.getMetadata();
                    JSONArray jsonArray = new JSONArray();
                    for(Map.Entry<String, String> entry : instanceMetaData.entrySet()) {
                        if(StringUtils.equals(entry.getKey(), serviceName)) {
                            jsonArray = JSON.parseArray(entry.getValue());
                            for(int i = 0;i < jsonArray.size();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(StringUtils.equals(impl, jsonObject.getString(CommonConstants.IMPL))) {
                                    jsonObject.put(CommonConstants.WEIGHT, weight);
                                    jsonArray.set(i, jsonObject);
                                }
                            }
                        }
                    }
                    instanceMetaData.put(serviceName, jsonArray.toJSONString());
                    //调用nacos api修改实例元数据
                    String nacosUrl = CommonConstants.URL_PREFIX + nacosAddress + CommonConstants.UPDATE_INSTANCE + CommonConstants.SERVICE_NAME + providerName +
                            CommonConstants.AND + CommonConstants.IP + ip + CommonConstants.AND + CommonConstants.PORT + port +
                            CommonConstants.AND + CommonConstants.META_DATA;
                    restTemplate.put(nacosUrl, null, JSON.toJSONString(instanceMetaData));
                    responseBO.setResponseCode(ResponseCodeEnum.SUCCESS.getKey());
                    responseBO.setResponseMessage(ResponseCodeEnum.SUCCESS.getDesc());
                }
            }
        } catch (Exception e) {
            logger.error("call nacos error:{}", e.getMessage());
            responseBO.setResponseCode(ResponseCodeEnum.ERROR.getKey());
            responseBO.setResponseMessage(e.getMessage());
        }
        return responseBO;
    }
}
