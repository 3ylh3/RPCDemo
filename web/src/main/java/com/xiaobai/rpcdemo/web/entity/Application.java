package com.xiaobai.rpcdemo.web.entity;

import lombok.Data;

/**
 * 应用实体类
 *
 * @author yinzhaojing
 * @date 2021-05-12 20:15:01
 */
@Data
public class Application {
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 实例数
     */
    private Integer instanceNumber;
    /**
     * 健康实例数
     */
    private Integer healthInstanceNumber;
}
