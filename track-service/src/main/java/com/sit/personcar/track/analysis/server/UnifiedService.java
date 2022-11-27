package com.sit.personcar.track.analysis.server;

import com.sit.personcar.track.analysis.vo.RequestData;
import com.sit.personcar.track.analysis.vo.ResponseViewData;

/**
 * 统一调用查询服务接口
 * 1、转换结果集对象
 * 2、自定义封装主键数据
 */
public interface UnifiedService {

    /**
     * 统一查询服务接口调用
     * @param requestData 查询服务的请求对象
     * @return
     */
    public ResponseViewData GatewayMsgProxyInvoke(RequestData requestData);
}
