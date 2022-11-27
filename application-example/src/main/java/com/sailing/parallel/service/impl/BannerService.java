package com.sailing.parallel.service.impl;

import com.sailing.parallel.basedto.BannerDTO;
import com.sailing.parallel.requestvo.BannerParam;
import com.sailing.parallel.service.IBannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: spring-starter
 * @description: 特殊横幅的服务实现
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-05-29 18:04:
 **/
@Service
@Slf4j
public class BannerService implements IBannerService {

    /**
     * 查询特殊横幅相关信息
     * @param bannerParam
     * @return
     */
    @Override
    public BannerDTO queryBannerInfo(BannerParam bannerParam) {
        log.info("查询特殊横幅相关信息:>{}", bannerParam);
        return new BannerDTO();
    }
}
