package com.xiaobai.rpcdemo.web.bo;

import lombok.Data;

/**
 * 公共分页查询返回BO
 *
 * @author yinzhaojing
 * @date 2021-04-12 11:37:34
 */
@Data
public class CommonPaginationResponseBO extends CommonResponseBO {
    /**
     * 总记录数
     */
    private Integer total;
}
