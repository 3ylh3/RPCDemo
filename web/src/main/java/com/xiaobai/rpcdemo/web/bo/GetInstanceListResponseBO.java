package com.xiaobai.rpcdemo.web.bo;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.Data;

import java.util.List;

/**
 * 获取实例详情返回BO
 *
 * @author yinzhaojing
 * @date 2021-05-13 15:01:06
 */
@Data
public class GetInstanceListResponseBO extends CommonResponseBO {
    private List<Instance> rows;
}
