package com.xiaobai.rpcdemo.web.service.impl;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.xiaobai.rpcdemo.web.bo.CommonResponseBO;
import com.xiaobai.rpcdemo.web.bo.GetApplicationListResponseBO;
import com.xiaobai.rpcdemo.web.bo.GetInstanceListResponseBO;
import com.xiaobai.rpcdemo.web.constants.CommonConstants;
import com.xiaobai.rpcdemo.web.entity.Application;
import com.xiaobai.rpcdemo.web.enums.ResponseCodeEnum;
import com.xiaobai.rpcdemo.web.service.ApplicationManageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用管理实现类
 *
 * @author yinzhaojing
 * @date 2021-05-13 11:20:22
 */
@Service
public class ApplicationManageImpl implements ApplicationManageService {
    @Autowired
    private NamingService namingService;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationManageImpl.class);

    /**
     * 获取应用列表
     * @param applicationName 应用名
     * @param offset 偏移量
     * @param limit 每页记录数
     * @return
     */
    @Override
    public GetApplicationListResponseBO getApplicationList(String applicationName, Integer offset, Integer limit) {
        GetApplicationListResponseBO responseBO = new GetApplicationListResponseBO();
        logger.info("get application list start");
        List<String> list = null;
        try {
            //获取所有应用
            int total = 0;
            if (StringUtils.isBlank(applicationName)) {
                //获取总数
                ListView<String> totalServices = namingService.getServicesOfServer(CommonConstants.PAGE_NO, CommonConstants.PAGE_SIZE);
                total = totalServices.getCount();
                ListView<String> services = namingService.getServicesOfServer(offset / limit + 1, limit);
                list = services.getData();
            } else {
                list = new ArrayList<>();
                list.add(applicationName);
                total = 1;
            }
            if(null == list || list.isEmpty()) {
                logger.error("get application list error,no data");
                responseBO.setResponseCode(ResponseCodeEnum.NO_DATA.getKey());
                responseBO.setResponseMessage(ResponseCodeEnum.NO_DATA.getDesc());
            } else {
                //查询应用实例信息
                List<Application> data = new ArrayList<>();
                for (String service : list) {
                    List<Instance> instances = namingService.getAllInstances(service);
                    Application application = new Application();
                    application.setApplicationName(service);
                    application.setInstanceNumber(instances.size());
                    int healthInstanceNumber = 0;
                    for(Instance instance : instances) {
                        if(instance.isHealthy()) {
                            healthInstanceNumber++;
                        }
                    }
                    application.setHealthInstanceNumber(healthInstanceNumber);
                    data.add(application);
                }
                logger.info("get application list success");
                responseBO.setRows(data);
                responseBO.setTotal(total);
                responseBO.setResponseCode(ResponseCodeEnum.SUCCESS.getKey());
                responseBO.setResponseMessage(ResponseCodeEnum.SUCCESS.getDesc());
            }
        } catch (Exception e) {
            logger.error("call nacos error:{}", e.getMessage());
            responseBO.setResponseCode(ResponseCodeEnum.ERROR.getKey());
            responseBO.setResponseMessage(e.getMessage());
        }
        return responseBO;
    }

    /**
     * 获取实例列表
     * @param applicationName 应用名
     * @return 实例列表
     */
    public GetInstanceListResponseBO getInstanceList(String applicationName) {
        GetInstanceListResponseBO responseBO = new GetInstanceListResponseBO();
        logger.info("get instance list start,applicationName:{}", applicationName);
        try {
            List<Instance> list = namingService.getAllInstances(applicationName);
            if(null == list || list.isEmpty()) {
                logger.error("get instance list error,no data");
                responseBO.setResponseCode(ResponseCodeEnum.NO_DATA.getKey());
                responseBO.setResponseMessage(ResponseCodeEnum.NO_DATA.getDesc());
            } else {
                logger.info("get instance list success");
                responseBO.setRows(list);
                responseBO.setResponseCode(ResponseCodeEnum.SUCCESS.getKey());
                responseBO.setResponseMessage(ResponseCodeEnum.SUCCESS.getDesc());
            }
        } catch (Exception e) {
            logger.error("call nacos error:{}", e.getMessage());
            responseBO.setResponseCode(ResponseCodeEnum.ERROR.getKey());
            responseBO.setResponseMessage(e.getMessage());
        }
        return responseBO;
    }
}
