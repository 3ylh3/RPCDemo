package com.xiaobai.rpcdemo.core.common;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 元数据信息
 *
 * @author yinzhaojing
 * @date 2021-04-29 15:56:57
 */
@Data
public class MetaInfo implements ApplicationListener<WebServerInitializedEvent>, ApplicationContextAware {
    private String name;
    private String ip;
    private Integer port;
    private ApplicationContext applicationContext;

    private static final String URL_PREFIX = "http://";
    private static final String URL_PORT_PREFIX = ":";

    private static final Logger logger = LoggerFactory.getLogger(MetaInfo.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        try {
            String ipAddress = "";
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress tmp = addresses.nextElement();
                    if (tmp instanceof Inet4Address && !tmp.isLoopbackAddress()
                            && !tmp.getHostAddress().contains(URL_PORT_PREFIX)){
                        ipAddress = tmp.getHostAddress();
                        break;
                    }
                }
            }
            int port = event.getWebServer().getPort();
            this.ip = ipAddress;
            this.port = port;
            this.name = this.applicationContext.getId();
        } catch (Exception e) {
            logger.error("init rpcdemo exception:{}", e.toString());
        }
    }
}
