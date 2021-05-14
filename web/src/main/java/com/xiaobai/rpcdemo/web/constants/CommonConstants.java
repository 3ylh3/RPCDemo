package com.xiaobai.rpcdemo.web.constants;

import lombok.Data;

/**
 * 公共常量类
 *
 * @author yinzhaojing
 * @date 2021-05-13 11:49:01
 */
@Data
public class CommonConstants {
    public static final int PAGE_NO = 0;
    public static final int PAGE_SIZE = 65536;
    public static final String GROUP = "group";
    public static final String IMPL = "impl";
    public static final String WEIGHT = "weight";
    public static final String URL_PREFIX = "http://";
    public static final String URL_MIDFIX = ":";
    public static final String UPDATE_INSTANCE = "/nacos/v1/ns/instance?";
    public static final String AND = "&";
    public static final String SERVICE_NAME = "serviceName=";
    public static final String IP = "ip=";
    public static final String PORT = "port=";
    public static final String META_DATA = "metadata={metadata}";
}
