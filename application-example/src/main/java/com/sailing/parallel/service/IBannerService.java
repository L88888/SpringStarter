package com.sailing.parallel.service;

import com.sailing.parallel.basedto.BannerDTO;
import com.sailing.parallel.requestvo.BannerParam;

/**
 * @program: spring-starter
 * @description: 横幅服务
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-05-29 16:56:
 **/
public interface IBannerService {

    /**
     * 查询特殊横幅相关信息
     * @param bannerParam
     * @return
     */
    public BannerDTO queryBannerInfo(BannerParam bannerParam);
}