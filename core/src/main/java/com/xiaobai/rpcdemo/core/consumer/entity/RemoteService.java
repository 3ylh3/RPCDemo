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
    private String providerName;
    private String url;
    private String impl;
    private String group;
}
