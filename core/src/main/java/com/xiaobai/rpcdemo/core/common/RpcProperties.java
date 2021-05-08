package com.xiaobai.rpcdemo.core.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置属性类
 *
 * @author yinzhaojing
 * @date 2021-04-29 14:54:37
 */
@Data
@ConfigurationProperties("rpcdemo")
public class RpcProperties {
    private String name;
    private Integer readTimeout;
    private Integer connectTimeout;
    private String nacosAddress;
}
