package com.xiaobai.rpcdemo.web.controller;

import com.xiaobai.rpcdemo.web.bo.GetServiceListRequestBO;
import com.xiaobai.rpcdemo.web.bo.GetServiceListResponseBO;
import com.xiaobai.rpcdemo.web.service.ServiceManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取服务列表controller
 *
 * @author yinzhaojing
 * @date 2021-05-13 17:22:28
 */
@RestController
public class GetServiceListController {
    @Autowired
    private ServiceManageService serviceManageService;
    @RequestMapping("getServiceList")
    public GetServiceListResponseBO getServiceList(@RequestBody GetServiceListRequestBO requestBO) {
        return serviceManageService.getServiceList(requestBO.getServiceName(), requestBO.getProviderName(), requestBO.getGroup(),
                requestBO.getOffset(), requestBO.getLimit());
    }
}
