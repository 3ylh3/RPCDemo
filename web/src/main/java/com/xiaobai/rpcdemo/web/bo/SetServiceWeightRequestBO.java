package com.xiaobai.rpcdemo.web.bo;

import lombok.Data;

/**
 * 设置服务权重请求BO
 *
 * @author yinzhaojing
 * @date 2021-05-14 14:21:13
 */
@Data
public class SetServiceWeightRequestBO {
    private String providerName;
    private String url;
    private String serviceName;
    private String impl;
    private Double weight;
}
