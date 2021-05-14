package com.xiaobai.rpcdemo.web.controller;

import com.xiaobai.rpcdemo.web.bo.CommonResponseBO;
import com.xiaobai.rpcdemo.web.bo.SetServiceWeightRequestBO;
import com.xiaobai.rpcdemo.web.service.ServiceManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设置服务权重controller
 *
 * @author yinzhaojing
 * @date 2021-05-14 14:20:23
 */
@RestController
public class SetServiceWeightController {
    @Autowired
    private ServiceManageService serviceManageService;

    @RequestMapping("/setServiceWeight")
    public CommonResponseBO setServiceWeight(@RequestBody SetServiceWeightRequestBO requestBO) {
        return serviceManageService.setServiceWeight(requestBO.getProviderName(), requestBO.getUrl(), requestBO.getServiceName(), requestBO.getImpl(), requestBO.getWeight());
    }
}
