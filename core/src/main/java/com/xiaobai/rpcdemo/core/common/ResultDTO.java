package com.xiaobai.rpcdemo.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 调用结果dto
 *
 * @author yinzhaojing
 * @date 2021-04-29 11:19:53
 */
@Data
public class ResultDTO implements Serializable {
    private static final long serialVersionUID = 8584048079506064811L;
    private Integer messageCode;
    private String message;
    Object result;
}
