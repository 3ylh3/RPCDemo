package com.xiaobai.rpcdemo.core.enums;

/**
 * 返回码枚举
 *
 * @author yinzhaojing
 * @date 2021-04-29 11:21:26
 */
public enum MessageCodeEnum {
    SUCCESS(0),
    ERROR(1);

    private Integer code;

    private MessageCodeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
