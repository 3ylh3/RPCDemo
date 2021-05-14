package com.xiaobai.rpcdemo.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类
 *
 * @author yinzhaojing
 * @date 2021-01-15 15:44:04
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    @ConditionalOnMissingBean(ClientHttpRequestFactory.class)
    public ClientHttpRequestFactory initClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(120000);
        factory.setConnectTimeout(125000);
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate initRestTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }
}
