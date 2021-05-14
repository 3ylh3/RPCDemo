package com.xiaobai.rpcdemo.web.controller;

import com.xiaobai.rpcdemo.web.bo.GetApplicationListRequestBO;
import com.xiaobai.rpcdemo.web.bo.GetApplicationListResponseBO;
import com.xiaobai.rpcdemo.web.service.ApplicationManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询应用列表
 *
 * @author yinzhaojing
 * @date 2021-05-12 20:08:19
 */
@RestController
public class GetApplicationListController {
    @Autowired
    private ApplicationManageService applicationManageService;

    @RequestMapping("/getApplicationList")
    public GetApplicationListResponseBO getApplicationList(@RequestBody GetApplicationListRequestBO requestBO) {
        return applicationManageService.getApplicationList(requestBO.getApplicationName(), requestBO.getOffset(), requestBO.getLimit());
    }
}
