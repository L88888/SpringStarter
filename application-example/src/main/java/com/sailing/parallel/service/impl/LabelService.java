package com.sailing.parallel.service.impl;

import com.sailing.parallel.basedto.LabelDTO;
import com.sailing.parallel.requestvo.LabelParam;
import com.sailing.parallel.service.ILabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: spring-starter
 * @description: 标签服务实现
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-05-29 18:01:
 **/
@Service
@Slf4j
public class LabelService implements ILabelService {

    /**
     * 查询标签相关信息
     * @param labelParam
     * @return
     */
    @Override
    public LabelDTO queryLabelInfo(LabelParam labelParam) {
        log.info("查询标签信息:>{}", labelParam);
        return new LabelDTO();
    }
}
