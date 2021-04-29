package com.xiaobai.rpcdemo.core.provider.config;

import com.xiaobai.rpcdemo.core.common.MetaInfo;
import com.xiaobai.rpcdemo.core.common.RpcProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * 提供者类
 *
 * @author yinzhaojing
 * @date 2021-04-29 19:52:07
 */
public class ProviderRegistry implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private ProviderServiceHolder providerServiceHolder;
    @Autowired
    private MetaInfo metaInfo;
    @Autowired
    private RpcProperties rpcProperties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        String prividerName = null == rpcProperties.getName() ? metaInfo.getName() : rpcProperties.getName();
        String url = metaInfo.getUrl();
    }
}
