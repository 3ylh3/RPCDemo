package com.xiaobai.rpcdemo.web.bo;

import com.xiaobai.rpcdemo.web.entity.Application;
import lombok.Data;

import java.util.List;

/**
 * 获取应用列表返回BO
 *
 * @author yinzhaojing
 * @date 2021-05-12 20:12:58
 */
@Data
public class GetApplicationListResponseBO extends CommonPaginationResponseBO {
    List<Application> rows;
}
