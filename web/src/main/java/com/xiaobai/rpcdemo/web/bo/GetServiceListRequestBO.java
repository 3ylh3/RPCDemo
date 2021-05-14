package com.xiaobai.rpcdemo.web.bo;

import lombok.Data;

/**
 * 获取服务列表请求BO
 *
 * @author yinzhaojing
 * @date 2021-05-13 17:25:38
 */
@Data
public class GetServiceListRequestBO extends CommonPaginationRequestBO {
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 提供者名
     */
    private String providerName;
    /**
     * 组
     */
    private String group;
}
