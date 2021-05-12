package com.xiaobai.rpcdemo.core.enums;

/**
 * 负载均衡策略枚举类
 *
 * @author yinzhaojing
 * @date 2021-05-12 17:22:02
 */
public enum LoadBalanceEnum {
    //轮询
    ROUND("round"),
    //随机
    RANDOM("random"),
    //按权重
    WEIGHT("weight");
    private String value;

    private LoadBalanceEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
