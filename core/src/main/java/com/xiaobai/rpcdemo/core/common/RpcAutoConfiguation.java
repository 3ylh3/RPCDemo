package com.xiaobai.rpcdemo.core.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 自动装配类
 *
 * @author yinzhaojing
 * @date 2021-04-29 16:16:22
 */
@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RpcAutoConfiguation {

    @Autowired
    private RpcProperties rpcProperties;

    @Bean("metaInfo")
    @ConditionalOnMissingBean(MetaInfo.class)
    public MetaInfo initProjectInfo() {
        return new MetaInfo();
    }

    @Bean
    @ConditionalOnMissingBean(ClientHttpRequestFactory.class)
    public ClientHttpRequestFactory initClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        int readTimeout = null != rpcProperties.getReadTimeout() ? rpcProperties.getReadTimeout() : 5000;
        int connectTimeout = null != rpcProperties.getConnectTimeout() ? rpcProperties.getConnectTimeout() : 10000;
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectTimeout);
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate initRestTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }
}
