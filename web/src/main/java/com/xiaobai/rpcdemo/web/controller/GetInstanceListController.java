package com.xiaobai.rpcdemo.web.controller;

import com.xiaobai.rpcdemo.web.bo.GetInstanceListResponseBO;
import com.xiaobai.rpcdemo.web.service.ApplicationManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取实例详情controller
 *
 * @author yinzhaojing
 * @date 2021-05-13 15:00:27
 */
@RestController
public class GetInstanceListController {
    @Autowired
    private ApplicationManageService applicationManageService;

    @RequestMapping("/getInstanceList")
    public GetInstanceListResponseBO getInstanceList(@RequestParam("applicationName") String applicationName) {
        return applicationManageService.getInstanceList(applicationName);
    }
}
