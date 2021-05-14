package com.xiaobai.rpcdemo.web.service;

import com.xiaobai.rpcdemo.web.bo.CommonResponseBO;
import com.xiaobai.rpcdemo.web.bo.GetApplicationListResponseBO;
import com.xiaobai.rpcdemo.web.bo.GetInstanceListResponseBO;

/**
 * 应用管理接口
 *
 * @author yinzhaojing
 * @date 2021-05-12 20:22:51
 */
public interface ApplicationManageService {
    /**
     * 获取应用列表
     * @param applicationName 应用名
     * @param offset 偏移量
     * @param limit 每页记录数
     * @return 应用列表
     */
    GetApplicationListResponseBO getApplicationList(String applicationName, Integer offset, Integer limit);

    /**
     * 获取实例列表
     * @param applicationName 应用名
     * @return 实例列表
     */
    GetInstanceListResponseBO getInstanceList(String applicationName);
}
