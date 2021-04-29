package com.xiaobai.rpcdemo.core.consumer.config;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * 消费者自动配置类
 *
 * @author yinzhaojing
 * @date 2021-04-28 16:40:11
 */
@Configuration
public class ConsumerAutoConfiguration {

    @Bean("remoteServiceHolder")
    public RemoteServiceHolder initRemoteServiceHolder(){
        RemoteServiceHolder remoteServiceHolder = new RemoteServiceHolder();
        remoteServiceHolder.init();
        return remoteServiceHolder;
    }

    @Bean("consumerPostProcessor")
    @DependsOn({"remoteServiceHolder"})
    public BeanPostProcessor initBeanPostProcessor() {
        return new ConsumerServicePostProcessor();
    }
}
