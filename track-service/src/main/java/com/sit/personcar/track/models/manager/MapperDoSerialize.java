package com.sit.personcar.track.models.manager;

import com.alibaba.fastjson.JSONObject;
import com.sit.personcar.track.analysis.entity.ResponseData;
import com.sit.personcar.track.analysis.vo.AppBody;
import com.sit.personcar.track.analysis.vo.AppHeader;
import com.sit.personcar.track.analysis.vo.ResponseViewData;
import com.sit.personcar.track.models.config.SystemConfig;
import com.sit.personcar.track.models.utils.SpringBean;
import com.sit.personcar.track.models.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * 异步处理响应报文
 * 主要处理字段转换、sid主键数据拼接
 */
public final class MapperDoSerialize {

    private static final Logger LOG = LoggerFactory.getLogger(MapperDoSerialize.class);

    private static final String SID = "sid";

    private static final String KEYSFG = "@";

    private static String[] RESDATA;

    static SystemConfig clickHouseData;
    public MapperDoSerialize(){
        // 初始化配置bean对象<clickHouseData>
        clickHouseData = SpringBean.getBean(SystemConfig.class);
    }

    /**
     * 结果集反序列化处理
     * @param <T>
     * @return
     */
    public static <T> Function<Receive, ResponseViewData> transformationData() {
        return res -> transformation(res);
    }

    /**
     * 处理数据结构转换与主键字段值自定义封装
     * @param resVal
     * @return
     */
    private static ResponseViewData transformation(Receive resVal){
        try {
            if (Objects.isNull(resVal) || resVal.getStatusCode() == -1){
                return new ResponseViewData(new AppHeader("-1", resVal.getBody()),
                        new AppBody(),
                        resVal.getServerId());
            }

            String serviceId = resVal.getServerId();
            ResponseData data = JSONObject.parseObject(resVal.getBody(), ResponseData.class);
            List<List<String>> responseDataBodys = data.getApp_body().getResult();

            // 响应报文结构重组
            if (Objects.isNull(responseDataBodys) || responseDataBodys.isEmpty()){
                return new ResponseViewData(new AppHeader("-1", resVal.getBody()),
                        new AppBody(),
                        serviceId);
            }

            // 获取字段名称集合
            List<String> filedNames = responseDataBodys.get(0);
            LOG.info("resVal is value :>>>{}", filedNames.toString());
            removeOneData(responseDataBodys);
            // 获取主键字段值
            String[] resData = getKeysFileds(serviceId);

            AppBody appBody = new AppBody();
            List<Map<String, String>> resultData = new ArrayList<>(2000);
            LOG.info("responseDataBodys.size:>>>{}", responseDataBodys.size());

            Map<String, String> vals = null;
            int filedNameSize = filedNames.size();
            for (List<String> filedVal : responseDataBodys){
                if (filedNameSize != filedVal.size()){
                    LOG.info("filedNameSize{}, filedValSize{}", filedNameSize, filedVal.size());
                    continue;
                }
                vals = new HashMap<>(300);
                // 遍历字段名称,给每一行数据拼接字段前缀
                for (int i = 0; i < filedNameSize;i++){
                    vals.put(filedNames.get(i), filedVal.get(i));
                }

                // 计算每行数据的sid(主键)值
                setSid(vals, resData);
                resultData.add(vals);
                vals = null;
            }

            appBody.setCount(data.getApp_body().getCount());
            appBody.setTotalCount(data.getApp_body().getTotalCount());
            appBody.setResult(resultData);
            resultData = null;

            AppHeader appHeader = new AppHeader();
            appHeader.setStatus(data.getApp_header().getStatus());
            appHeader.setStatusMsg(data.getApp_header().getStatusMsg());

            ResponseViewData responseViewData = new ResponseViewData();
            responseViewData.setApp_body(appBody);
            responseViewData.setServerId(serviceId);
            responseViewData.setApp_header(appHeader);
//        LOG.info("responseViewData:>>>{}", JSON.toJSONString(responseViewData));
            return responseViewData;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseViewData(new AppHeader("-1", e.getMessage()),
                    new AppBody(),
                    resVal.getServerId());
        }
    }

    /**
     * 第一行不参与数据重组
     * @param responseDataBodys
     */
    private static void removeOneData(List<List<String>> responseDataBodys){
        responseDataBodys.remove(0);
    }

    /**
     * 获取查询服务的主键keys集合
     * @param serviceId  查询服务ID
     * @return String[]
     */
    private static String[] getKeysFileds(String serviceId){
        clickHouseData = SpringBean.getBean(SystemConfig.class);
        List<SystemConfig.KeysConfigs> configs = clickHouseData.getConfigs();
        if (Objects.isNull(configs) || configs.isEmpty()){
            return RESDATA;
        }

        String[] fileds = {};
        for (SystemConfig.KeysConfigs config: configs){
            if (Tools.isEmpty(config.getServiceId())
                    || Tools.isEmpty(config.getKeyFileds())){
                continue;
            }

            if (config.getServiceId().equalsIgnoreCase(serviceId)){
                fileds = config.getKeyFileds().split(KEYSFG);
                break;
            }
        }
        LOG.info("查询服务:>>>{},对应的主键字段拼接值:>>>{}", serviceId, fileds);
        return fileds;
    }

    /**
     * 设置sid主键值，该值为业务字段的拼接值
     * @param vals
     * @param keysResData
     */
    private static void setSid(Map<String, String> vals,String[] keysResData){
        String sid = "";
        for (String key : keysResData){
            if (vals.containsKey(key)){
                sid = sid.concat(vals.get(key));
            }
        }
        vals.put(SID, sid);
    }
}