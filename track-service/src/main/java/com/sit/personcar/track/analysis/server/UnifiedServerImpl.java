package com.sit.personcar.track.analysis.server;

import com.sit.personcar.track.analysis.entity.RequestHeads;
import com.sit.personcar.track.analysis.vo.AppBody;
import com.sit.personcar.track.analysis.vo.AppHeader;
import com.sit.personcar.track.analysis.vo.RequestData;
import com.sit.personcar.track.analysis.vo.ResponseViewData;
import com.sit.personcar.track.models.config.SystemConfig;
import com.sit.personcar.track.models.manager.SasyncHttpClient;
import com.sit.personcar.track.models.utils.Tools;
import com.sit.personcar.track.sink.production.ResponseDataSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 封装22类人员静态轨迹数据
 */
@Service
public class UnifiedServerImpl implements UnifiedService {
    private static final Logger LOG = LoggerFactory.getLogger(UnifiedServerImpl.class);

    private static final String sortFiledfg = "-";

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private ResponseDataSink responseDataSink;

    @Override
    public ResponseViewData GatewayMsgProxyInvoke(RequestData requestData) {
        // 给请求查询服务动态添加排序字段
        setSortFiled(requestData);
        // 处理请求头数据
        RequestHeads requestHeads = new RequestHeads();
        requestHeads.setServiceId(requestData.getServiceId());
        requestHeads.setEndUserCertificate(systemConfig.getEndUserCertificate());
        requestHeads.setEndUserDeviceId(systemConfig.getEndUserDeviceId());
        requestHeads.setEndUserIdCard(systemConfig.getEndUserIdCard());
        requestHeads.setEndUserName(systemConfig.getEndUserName());
        requestHeads.setEndUserDepartment(systemConfig.getEndUserDepartment());
        requestHeads.setSenderId(systemConfig.getSenderId());
        LOG.info("请求头对象数据:>{}", requestHeads);

        try(SasyncHttpClient client = getConnect()){
            CompletableFuture<ResponseViewData> resDataTemp = client.post(requestData,requestHeads);
            ResponseViewData responseViewData = resDataTemp.get();
            responseDataSink.trackDataSinkKafka(responseViewData);
            return resDataTemp.get();
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseViewData(new AppHeader("-1", e.getMessage()),
                    new AppBody(),
                    requestData.getServiceId());
        }
    }

    /**
     * 根据服务ID查询并设置排序字段
     * @param requestData 请求对象
     */
    private void setSortFiled(RequestData requestData){
        List<SystemConfig.KeysConfigs> configs = systemConfig.getConfigs();
        if (Objects.isNull(configs) || configs.isEmpty()){
            return;
        }

        if (!Objects.isNull(requestData.getSortItems()) && requestData.getSortItems().length > 0){
            LOG.info("请求参数存在排序字段:>{}", requestData.getSortItems());
            return;
        }

        for (SystemConfig.KeysConfigs config: configs){
            if (Tools.isEmpty(config.getServiceId())
                    || Tools.isEmpty(config.getKeyFileds())){
                continue;
            }

            if (config.getServiceId().equalsIgnoreCase(requestData.getServiceId())
                    && !Tools.isEmpty(config.getKeyFileds())){
                requestData.setSortItems(new String[]{config.getKeyFileds().concat(sortFiledfg)});
                break;
            }
        }
    }

    /**
     * 创建http链接
     * @return
     */
    private SasyncHttpClient getConnect(){
        return new SasyncHttpClient(systemConfig.getEndpoint());
    }
}