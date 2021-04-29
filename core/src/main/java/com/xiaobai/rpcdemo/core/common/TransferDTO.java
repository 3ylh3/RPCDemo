package com.xiaobai.rpcdemo.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 传输dto
 *
 * @author yinzhaojing
 * @date 2021-04-28 19:23:29
 */
@Data
public class TransferDTO implements Serializable {

    private static final long serialVersionUID = 2057849377082198418L;
    /**
     * 消费者name
     */
    private String consumerName;
    /**
     * 接口全限定类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 参数
     */
    private Object[] params;
}
