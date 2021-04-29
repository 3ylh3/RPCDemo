package com.xiaobai.rpcdemo.core.exception;

/**
 * 远程调用异常
 *
 * @author yinzhaojing
 * @date 2021-04-29 11:43:34
 */
public class RemoteCallException extends RuntimeException {
    public RemoteCallException(String msg) {
        super(msg);
    }
}
