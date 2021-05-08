package com.xiaobai.rpcdemo.core.consumer.config;

import com.xiaobai.rpcdemo.core.common.MetaInfo;
import com.xiaobai.rpcdemo.core.common.ResultDTO;
import com.xiaobai.rpcdemo.core.common.RpcProperties;
import com.xiaobai.rpcdemo.core.common.TransferDTO;
import com.xiaobai.rpcdemo.core.consumer.annotation.Remote;
import com.xiaobai.rpcdemo.core.consumer.entity.RemoteService;
import com.xiaobai.rpcdemo.core.enums.MessageCodeEnum;
import com.xiaobai.rpcdemo.core.exception.RemoteCallException;
import org.nustaq.serialization.FSTConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 远程service前置处理
 *
 * @author yinzhaojing
 * @date 2021-04-28 16:12:55
 */
public class ConsumerServicePostProcessor implements BeanPostProcessor {
    @Autowired
    private RemoteServiceHolder remoteServiceHolder;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RpcProperties rpcProperties;
    @Autowired
    private MetaInfo metaInfo;

    private static final String PROVIDER_CONTROLLER = "/provider";

    private static final Logger logger = LoggerFactory.getLogger(ConsumerServicePostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Enhancer enhancer = new Enhancer();
        for(Field field : fields) {
            //如果有@Remote注解修饰的变量，则用cglib动态生成代理对象
            if(null != field.getAnnotation(Remote.class)) {
                try {
                    enhancer.setSuperclass(field.getType());
                    enhancer.setCallback(new MethodInterceptor() {
                        @Override
                        public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                            List<RemoteService> list = remoteServiceHolder.getRemoteService(field.getType().getName());
                            double random = Math.random() * list.size();
                            RemoteService remoteService = list.get((int)random);
                            TransferDTO transferDTO = new TransferDTO();
                            String consumerName = null == rpcProperties.getName() ? metaInfo.getName() : rpcProperties.getName();
                            transferDTO.setConsumerName(consumerName);
                            transferDTO.setClassName(remoteService.getImpl());
                            transferDTO.setMethodName(method.getName());
                            transferDTO.setParameterTypes(method.getParameterTypes());
                            transferDTO.setParams(objects);
                            try {
                                //使用fst序列化传输对象
                                FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
                                byte[] bytes = conf.asByteArray(transferDTO);
                                BASE64Encoder base64Encoder = new BASE64Encoder();
                                String request = base64Encoder.encode(bytes);
                                String url = remoteService.getUrl();
                                String requestUrl = url + PROVIDER_CONTROLLER;
                                logger.info("----------------------------------");
                                logger.info("call remote service:{}-{}.{}", remoteService.getProviderName(), remoteService.getImpl(),
                                        method.getName());
                                String resultStr = restTemplate.postForObject(requestUrl, request, String.class);
                                //反序列化返回对象
                                BASE64Decoder base64Decoder = new BASE64Decoder();
                                ResultDTO resultDTO = (ResultDTO) conf.asObject(base64Decoder.decodeBuffer(resultStr));
                                if (MessageCodeEnum.SUCCESS.getCode().equals(resultDTO.getMessageCode())) {
                                    logger.info("call success");
                                    return resultDTO.getResult();
                                } else {
                                    logger.error("call remote service exception:{}", resultDTO.getMessageCode());
                                    throw new RemoteCallException(resultDTO.getMessage());
                                }
                            } catch (Exception e) {
                                logger.error("call remote service exception:{}", e.toString());
                                throw new RemoteCallException(e.getMessage());
                            }
                        }
                    });
                    field.setAccessible(true);
                    field.set(bean, enhancer.create());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return bean;
    }
}
