package com.xiaobai.rpcdemo.web.config;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配类
 *
 * @author yinzhaojing
 * @date 2021-05-13 11:26:17
 */
@Configuration
public class AutoConfiguration {
    @Value("${nacos.address}")
    private String nacosAddress;

    @Bean
    @ConditionalOnMissingBean(NamingService.class)
    public NamingService inidNamingService() throws Exception {
        return NamingFactory.createNamingService(nacosAddress);
    }
}
