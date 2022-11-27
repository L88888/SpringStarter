package com.sailing.parallel.service.impl;

import com.sailing.parallel.basedto.UserInfoDTO;
import com.sailing.parallel.requestvo.UserInfoParam;
import com.sailing.parallel.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-05-29 17:56:
 **/
@Service
@Slf4j
public class UserService implements IUserService {

    /**
     * 查询用户相关信息
     * @param userInfoParam
     * @return
     */
    @Override
    public UserInfoDTO queryUserInfo(UserInfoParam userInfoParam) {
        log.info("查询用户相关信息:>{}", userInfoParam);
        return new UserInfoDTO();
    }
}
