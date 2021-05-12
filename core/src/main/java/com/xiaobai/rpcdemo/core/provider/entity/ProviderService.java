package com.xiaobai.rpcdemo.core.provider.entity;

import lombok.Data;

/**
 * 提供者service实体类
 *
 * @author yinzhaojing
 * @date 2021-04-29 19:43:39
 */
@Data
public class ProviderService {
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
