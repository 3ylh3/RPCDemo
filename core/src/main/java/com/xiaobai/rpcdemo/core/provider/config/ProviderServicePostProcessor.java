package com.xiaobai.rpcdemo.core.provider.config;

import com.xiaobai.rpcdemo.core.provider.annotation.Service;
import com.xiaobai.rpcdemo.core.provider.entity.ProviderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供者service后置处理
 *
 * @author yinzhaojing
 * @date 2021-04-29 17:07:55
 */
public class ProviderServicePostProcessor implements BeanPostProcessor {
    @Autowired
    private ProviderServiceHolder providerServiceHolder;

    private static final Logger logger = LoggerFactory.getLogger(ProviderServicePostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        for(Annotation annotation : annotations) {
            //如果有@Service注解修饰的bean，则进行缓存，待所有bean加载完毕后向nacos中注册
            if(annotation instanceof Service) {
                logger.info("find provider service:{}", clazz.getName());
                Class<?>[] interfaces = clazz.getInterfaces();
                for(Class<?> interfaceClazz : interfaces) {
                    List<ProviderService> list = providerServiceHolder.getServiceList(interfaceClazz.getName());
                    if(null == list) {
                        list = new ArrayList<>();
                    }
                    ProviderService providerService = new ProviderService();
                    providerService.setImpl(clazz.getName());
                    if(!StringUtils.isBlank(((Service) annotation).group())){
                        providerService.setGroup(((Service) annotation).group());
                    }
                    providerService.setWeight(((Service) annotation).weight());
                    list.add(providerService);
                    providerServiceHolder.setServiceList(interfaceClazz.getName(), list);
                }
            }
        }

        return bean;
    }
}
