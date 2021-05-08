package com.xiaobai.rpcdemo.core.exception;

/**
 * 服务注册exception
 *
 * @author yinzhaojing
 * @date 2021-05-08 16:14:50
 */
public class ServiceRegistryException extends RuntimeException {
    public ServiceRegistryException(String msg) {
        super(msg);
    }
}
