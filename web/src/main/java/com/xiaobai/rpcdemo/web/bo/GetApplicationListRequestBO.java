package com.xiaobai.rpcdemo.web.bo;

import lombok.Data;

/**
 * 获取应用列表请求BO
 *
 * @author yinzhaojing
 * @date 2021-05-12 20:17:23
 */
@Data
public class GetApplicationListRequestBO extends CommonPaginationRequestBO {
    /**
     * 应用名称
     */
    private String applicationName;
}
