package com.xiaobai.rpcdemo.web.entity;

import lombok.Data;

/**
 * 服务实体类
 *
 * @author yinzhaojing
 * @date 2021-05-13 17:31:39
 */
@Data
public class Service {
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 提供者名称
     */
    private String providerName;
    /**
     * url
     */
    private String url;
    /**
     * 实现类名称
     */
    private String impl;
    /**
     * 组
     */
    private String group;
    /**
     * 权重
     */
    private Double weight;
}
