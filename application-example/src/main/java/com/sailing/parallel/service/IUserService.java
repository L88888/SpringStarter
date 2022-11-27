package com.sailing.parallel.service;

import com.sailing.parallel.basedto.UserInfoDTO;
import com.sailing.parallel.requestvo.UserInfoParam;

/**
 * @program: spring-starter
 * @description: 用户服务
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-05-29 16:56:
 **/
public interface IUserService {

    /**
     * 查询用户相关信息
     * @param userInfoParam
     * @return
     */
    public UserInfoDTO queryUserInfo(UserInfoParam userInfoParam);
}