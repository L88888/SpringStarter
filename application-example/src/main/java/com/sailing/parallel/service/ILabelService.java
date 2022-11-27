package com.sailing.parallel.service;

import com.sailing.parallel.basedto.LabelDTO;
import com.sailing.parallel.requestvo.LabelParam;

/**
 * @program: spring-starter
 * @description: 标签服务
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-05-29 16:57:
 **/
public interface ILabelService {

    /**
     * 查询标签相关信息
     * @param labelParam
     * @return
     */
    public LabelDTO queryLabelInfo(LabelParam labelParam);
}