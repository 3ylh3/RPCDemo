package com.xiaobai.rpcdemo.web.bo;

import com.xiaobai.rpcdemo.web.entity.Service;
import lombok.Data;

import java.util.List;

/**
 * 获取服务列表返回BO
 *
 * @author yinzhaojing
 * @date 2021-05-13 17:28:28
 */
@Data
public class GetServiceListResponseBO extends CommonPaginationResponseBO {
    private List<Service> rows;
}
