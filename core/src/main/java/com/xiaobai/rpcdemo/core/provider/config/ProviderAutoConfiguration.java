package com.xiaobai.rpcdemo.core.provider.config;

import com.xiaobai.rpcdemo.core.provider.controller.ProviderController;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * 提供者自动装配类
 *
 * @author yinzhaojing
 * @date 2021-04-29 16:17:29
 */
@Configuration
public class ProviderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ProviderController.class)
    public ProviderController initProviderController() {
        return new ProviderController();
    }

    @Bean("providerServiceHolder")
    @ConditionalOnMissingBean(ProviderServiceHolder.class)
    public ProviderServiceHolder initProviderServiceHolder() {
        return new ProviderServiceHolder();
    }

    @Bean("providerPostProcessor")
    @DependsOn({"metaInfo", "providerServiceHolder"})
    public BeanPostProcessor initBeanPostProcessor() {
        return new ProviderServicePostProcessor();
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initApplicationListener() {
        return new ProviderRegistry();
    }
}
