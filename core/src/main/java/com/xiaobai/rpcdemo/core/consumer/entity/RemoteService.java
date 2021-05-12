package com.xiaobai.rpcdemo.core.consumer.entity;

import lombok.Data;

/**
 * remote service实体
 *
 * @author yinzhaojing
 * @date 2021-04-28 17:24:24
 */
@Data
public class RemoteService {
    /**
     * 提供者name
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
