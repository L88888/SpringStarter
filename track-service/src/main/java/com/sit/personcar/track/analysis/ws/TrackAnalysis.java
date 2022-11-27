package com.sit.personcar.track.analysis.ws;

import com.alibaba.fastjson.JSONObject;
import com.sit.personcar.track.analysis.vo.RequestData;
import com.sit.personcar.track.analysis.entity.ResponseData;
import com.sit.personcar.track.analysis.server.UnifiedService;
import com.sit.personcar.track.analysis.vo.ResponseViewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @program: spring-starter
 * @description: 人员车辆静态轨迹分析
 * @author: LIULEI-TGL
 * @create: 2021-06-27 18:29:
 **/
@RestController
public class TrackAnalysis {
    private static final Logger LOG = LoggerFactory.getLogger(TrackAnalysis.class);

    @Autowired
    private UnifiedService unifiedServerImpl;

    /**
     * http://127.0.0.1:10227/GatewayMsg/proxy/invoke/v1
     * @return
     */
    @RequestMapping(value = "GatewayMsg/proxy/invoke/v1",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseViewData proxyInvokeTo() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
        // C90-65000046

        RequestData requestData = new RequestData();
        return unifiedServerImpl.GatewayMsgProxyInvoke(requestData);
    }

    /**
     * http://127.0.0.1:10227/GatewayMsg/proxy/invoke/v2
     * @return
     */
    @RequestMapping(value = "GatewayMsg/proxy/invoke/v2",method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseViewData proxyInvokeOne(@RequestBody RequestData requestData) {
        LOG.info("requestData:>{}", requestData);
        if (Objects.isNull(requestData)){
            return new ResponseViewData();
        }

        return unifiedServerImpl.GatewayMsgProxyInvoke(requestData);
    }

    /**
     * http://127.0.0.1:10227/GatewayMsg/proxy/invoke
     * @return
     */
    @RequestMapping(value = "GatewayMsg/proxy/invoke",method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseData proxyInvoke(@RequestHeader Map<String,String> heads,@RequestBody Map<String,Object> requestData) {
        LOG.info("heads:>{},requestData:>{}", heads, requestData);
        // C90-65000046
        String responseData = "{\"app_header\":{\"status\":\"000\",\"statusMsg\":\"操作成功\"},\"app_body\":{\"count\":\"20\",\"totalCount\":\"94\",\"result\":[[\"opr_stat_cd\",\"grp_ind\",\"grp_nm\",\"sub_cls_cd\",\"air_seg_flt_nbr\",\"pas_fst_nm\",\"operatedate\",\"air_seg_dpt_dt_lcl\",\"pas_chn_nm\",\"pas_id_type\",\"pas_lst_nm\",\"air_seg_dpt_tm_lcl\",\"pas_id\",\"flight_no\",\"operatetime\",\"air_seg_arrv_tm_lcl\",\"air_seg_arrv_dt_lcl\",\"pas_id_nbr\",\"air_seg_arrv_airpt_cd\",\"air_seg_dpt_airpt_cd\",\"pn_seat\",\"ffp_id_nbr\",\"vip_ind\"],[\"XX\",\"N\",\"\",\"W\",\"6988\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"21:15:00\",\"1\",\"CZ6988\",\"13:33:00\",\"01:25:00\",\"1\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"\",\"\"],[\"UN\",\"N\",\"\",\"S\",\"6988\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"21:15:00\",\"1\",\"CZ6988\",\"22:24:00\",\"01:25:00\",\"1\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"RR\",\"N\",\"\",\"V\",\"3435\",\"\",\"20181203\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"17:40:00\",\"1\",\"CZ3435\",\"10:09:00\",\"21:55:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"XX\",\"N\",\"\",\"W\",\"6988\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"21:15:00\",\"1\",\"CZ6988\",\"13:33:00\",\"01:25:00\",\"1\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"\",\"\"],[\"UN\",\"N\",\"\",\"S\",\"6988\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"21:15:00\",\"1\",\"CZ6988\",\"22:24:00\",\"01:25:00\",\"1\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"RR\",\"N\",\"\",\"V\",\"3435\",\"\",\"20181203\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"17:40:00\",\"1\",\"CZ3435\",\"10:09:00\",\"21:55:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"UN\",\"N\",\"\",\"V\",\"3435\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"17:40:00\",\"1\",\"CZ3435\",\"13:05:00\",\"21:55:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"UN\",\"N\",\"\",\"V\",\"3435\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"17:40:00\",\"1\",\"CZ3435\",\"13:05:00\",\"21:55:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HK\",\"N\",\"\",\"S\",\"698\",\"\",\"20181204\",\"20181205\",\"马宝全\",\"I\",\"MABAOQUAN\",\"06:00:00\",\"1\",\"CZ698R\",\"22:24:00\",\"10:20:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HK\",\"N\",\"\",\"S\",\"698\",\"\",\"20181204\",\"20181205\",\"马宝全\",\"I\",\"MABAOQUAN\",\"06:00:00\",\"1\",\"CZ698R\",\"22:24:00\",\"10:20:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HK\",\"N\",\"\",\"S\",\"698\",\"\",\"20181204\",\"20181205\",\"马宝全\",\"I\",\"MABAOQUAN\",\"06:00:00\",\"1\",\"CZ698R\",\"22:24:00\",\"10:20:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HK\",\"N\",\"\",\"S\",\"698\",\"\",\"20181204\",\"20181205\",\"马宝全\",\"I\",\"MABAOQUAN\",\"06:00:00\",\"1\",\"CZ698R\",\"22:24:00\",\"10:20:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"RR\",\"N\",\"\",\"A\",\"7523\",\"\",\"20190406\",\"20190409\",\"马宝全\",\"I\",\"MABAOQUAN\",\"09:15:00\",\"1\",\"GS7523\",\"13:04:00\",\"13:15:00\",\"0\",\"654123198406082773\",\"KWE\",\"URC\",\"1\",\"HU9130108093\",\"\"],[\"RR\",\"N\",\"\",\"A\",\"7523\",\"\",\"20190406\",\"20190409\",\"马宝全\",\"I\",\"MABAOQUAN\",\"09:15:00\",\"1\",\"GS7523\",\"13:04:00\",\"13:15:00\",\"0\",\"654123198406082773\",\"KWE\",\"URC\",\"1\",\"HU9130108093\",\"\"],[\"RR\",\"N\",\"\",\"A\",\"7523\",\"\",\"20190406\",\"20190409\",\"马宝全\",\"I\",\"MABAOQUAN\",\"09:15:00\",\"1\",\"GS7523\",\"13:04:00\",\"13:15:00\",\"0\",\"654123198406082773\",\"KWE\",\"URC\",\"1\",\"HU9130108093\",\"\"],[\"RR\",\"N\",\"\",\"A\",\"7523\",\"\",\"20190406\",\"20190409\",\"马宝全\",\"I\",\"MABAOQUAN\",\"09:15:00\",\"1\",\"GS7523\",\"13:04:00\",\"13:15:00\",\"0\",\"654123198406082773\",\"KWE\",\"URC\",\"1\",\"HU9130108093\",\"\"],[\"RR\",\"N\",\"\",\"E\",\"8545\",\"\",\"20190416\",\"20190419\",\"马宝全\",\"I\",\"MABAOQUAN\",\"10:35:00\",\"1\",\"CZ8545\",\"15:56:00\",\"15:00:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"RR\",\"N\",\"\",\"E\",\"8545\",\"\",\"20190416\",\"20190419\",\"马宝全\",\"I\",\"MABAOQUAN\",\"10:35:00\",\"1\",\"CZ8545\",\"15:56:00\",\"15:00:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HX\",\"N\",\"\",\"E\",\"8545\",\"\",\"20190419\",\"20190419\",\"马宝全\",\"I\",\"MABAOQUAN\",\"10:35:00\",\"1\",\"CZ8545\",\"09:25:00\",\"15:00:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HK\",\"N\",\"\",\"S\",\"8545\",\"\",\"20190419\",\"20190419\",\"马宝全\",\"I\",\"MABAOQUAN\",\"10:35:00\",\"1\",\"CZ8545\",\"11:11:00\",\"15:00:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"]]}}";

        LOG.info("responseData:>>>{}", responseData);
        ResponseData data = JSONObject.parseObject(responseData, ResponseData.class);
        LOG.info("response data:>>>{}",data);
        return data;
    }
}