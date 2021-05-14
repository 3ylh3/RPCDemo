package com.xiaobai.rpcdemo.web.service;

import com.xiaobai.rpcdemo.web.bo.CommonResponseBO;
import com.xiaobai.rpcdemo.web.bo.GetServiceListResponseBO;

/**
 * 服务管理接口
 *
 * @author yinzhaojing
 * @date 2021-05-13 17:37:05
 */
public interface ServiceManageService {
    /**
     * 获取服务列表
     * @param serviceName 服务名
     * @param providerName 提供者名
     * @param group 所属组
     * @param offset 偏移量
     * @param limit 每页记录数
     * @return 服务列表
     */
    GetServiceListResponseBO getServiceList(String serviceName, String providerName, String group, Integer offset, Integer limit);

    /**
     * 设置服务权重
     * @param url 提供者url
     * @param serviceName 服务名
     * @param impl 实现类
     * @param weight 权重
     * @return
     */
    CommonResponseBO setServiceWeight(String providerName, String url, String serviceName, String impl, Double weight);
}
