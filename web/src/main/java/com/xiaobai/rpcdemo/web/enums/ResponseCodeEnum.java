package com.xiaobai.rpcdemo.web.enums;

/**
 * 返回码枚举类
 *
 * @author yinzhaojing
 * @date 2021-04-12 14:27:49
 */
public enum ResponseCodeEnum {
    SUCCESS(0, "success"),
    ERROR(-1, "error"),
    NO_DATA(-2, "no data");

    private final int key;
    private final String desc;

    ResponseCodeEnum(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public int getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
