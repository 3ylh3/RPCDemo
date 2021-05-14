package com.xiaobai.rpcdemo.web.bo;

import lombok.Data;

/**
 * 公共返回BO
 *
 * @author yinzhaojing
 * @date 2021-03-17 10:09:38
 */
@Data
public class CommonResponseBO {
    private int responseCode;
    private String responseMessage;
}
