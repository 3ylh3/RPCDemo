package com.xiaobai.rpcdemo.web.bo;

import lombok.Data;

/**
 * 公共分页请求BO
 *
 * @author yinzhaojing
 * @date 2021-04-12 19:09:33
 */
@Data
public class CommonPaginationRequestBO {
    /**
     * 偏移量
     */
    private Integer offset;
    /**
     * 每页记录数
     */
    private Integer limit;
}
