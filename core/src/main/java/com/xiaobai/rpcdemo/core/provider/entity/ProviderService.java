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
    private String impl;
    private String group;
}
