package com.sit.personcar.track.test.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sit.personcar.track.analysis.entity.ResponseData;
import com.sit.personcar.track.analysis.vo.AppBody;
import com.sit.personcar.track.analysis.vo.AppHeader;
import com.sit.personcar.track.analysis.vo.ResponseViewData;
import com.sit.personcar.track.models.config.SystemConfig;
import com.sit.personcar.track.models.utils.Tools;
import org.apache.kafka.common.utils.CopyOnWriteMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Test {
    private static final Logger LOG = LoggerFactory.getLogger(Test.class);

    private static String[] RESDATA;

    String serviceId = "S90-65000313";

    private static final String SID = "sid";

    private static final String KEYSFG = "@";

    /**
     * 请求头报文数据格式转换与封装
     */
    @org.junit.Test
    public void t1(){
        try {
            String url = "http://104.0.123.013/file/com/weqqwe/qwe@qwehkj23.jpg";
            String encodedUrl = URLEncoder.encode(url, "UTF-8");
            System.out.println(encodedUrl);

            String encoded = URLEncoder.encode("王辉", "UTF-8");
            System.out.println(encoded);

            String decodeStr = URLDecoder.decode(encoded,"UTF-8");
            System.out.println(decodeStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // %E5%88%98%E5%B0%8F%E6%B3%95
        }
    }

    /**
     * 时间格式化处理
     */
    @org.junit.Test
    public void getTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String resTime = localDateTime.format(dateTimeFormatter);
        System.out.println("now resTime:>>" + resTime);

        List<String> t1 = new ArrayList<>(1);
        t1.get(1);
    }

    /**
     * 响应报文反序列化处理
     * 将结果集数据进行二次封装
     */
    @org.junit.Test
    public void transformation(){
        String responseData = "{\"app_header\":{\"status\":\"000\",\"statusMsg\":\"操作成功\"},\"app_body\":{\"count\":\"20\",\"totalCount\":\"94\",\"result\":[[\"opr_stat_cd\",\"grp_ind\",\"grp_nm\",\"sub_cls_cd\",\"air_seg_flt_nbr\",\"pas_fst_nm\",\"operatedate\",\"air_seg_dpt_dt_lcl\",\"pas_chn_nm\",\"pas_id_type\",\"pas_lst_nm\",\"air_seg_dpt_tm_lcl\",\"pas_id\",\"flight_no\",\"operatetime\",\"air_seg_arrv_tm_lcl\",\"air_seg_arrv_dt_lcl\",\"pas_id_nbr\",\"air_seg_arrv_airpt_cd\",\"air_seg_dpt_airpt_cd\",\"pn_seat\",\"ffp_id_nbr\",\"vip_ind\"],[\"XX\",\"N\",\"\",\"W\",\"6988\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"21:15:00\",\"1\",\"CZ6988\",\"13:33:00\",\"01:25:00\",\"1\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"\",\"\"],[\"UN\",\"N\",\"\",\"S\",\"6988\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"21:15:00\",\"1\",\"CZ6988\",\"22:24:00\",\"01:25:00\",\"1\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"RR\",\"N\",\"\",\"V\",\"3435\",\"\",\"20181203\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"17:40:00\",\"1\",\"CZ3435\",\"10:09:00\",\"21:55:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"XX\",\"N\",\"\",\"W\",\"6988\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"21:15:00\",\"1\",\"CZ6988\",\"13:33:00\",\"01:25:00\",\"1\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"\",\"\"],[\"UN\",\"N\",\"\",\"S\",\"6988\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"21:15:00\",\"1\",\"CZ6988\",\"22:24:00\",\"01:25:00\",\"1\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"RR\",\"N\",\"\",\"V\",\"3435\",\"\",\"20181203\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"17:40:00\",\"1\",\"CZ3435\",\"10:09:00\",\"21:55:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"UN\",\"N\",\"\",\"V\",\"3435\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"17:40:00\",\"1\",\"CZ3435\",\"13:05:00\",\"21:55:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"UN\",\"N\",\"\",\"V\",\"3435\",\"\",\"20181204\",\"20181204\",\"马宝全\",\"I\",\"MABAOQUAN\",\"17:40:00\",\"1\",\"CZ3435\",\"13:05:00\",\"21:55:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HK\",\"N\",\"\",\"S\",\"698\",\"\",\"20181204\",\"20181205\",\"马宝全\",\"I\",\"MABAOQUAN\",\"06:00:00\",\"1\",\"CZ698R\",\"22:24:00\",\"10:20:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HK\",\"N\",\"\",\"S\",\"698\",\"\",\"20181204\",\"20181205\",\"马宝全\",\"I\",\"MABAOQUAN\",\"06:00:00\",\"1\",\"CZ698R\",\"22:24:00\",\"10:20:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HK\",\"N\",\"\",\"S\",\"698\",\"\",\"20181204\",\"20181205\",\"马宝全\",\"I\",\"MABAOQUAN\",\"06:00:00\",\"1\",\"CZ698R\",\"22:24:00\",\"10:20:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HK\",\"N\",\"\",\"S\",\"698\",\"\",\"20181204\",\"20181205\",\"马宝全\",\"I\",\"MABAOQUAN\",\"06:00:00\",\"1\",\"CZ698R\",\"22:24:00\",\"10:20:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"RR\",\"N\",\"\",\"A\",\"7523\",\"\",\"20190406\",\"20190409\",\"马宝全\",\"I\",\"MABAOQUAN\",\"09:15:00\",\"1\",\"GS7523\",\"13:04:00\",\"13:15:00\",\"0\",\"654123198406082773\",\"KWE\",\"URC\",\"1\",\"HU9130108093\",\"\"],[\"RR\",\"N\",\"\",\"A\",\"7523\",\"\",\"20190406\",\"20190409\",\"马宝全\",\"I\",\"MABAOQUAN\",\"09:15:00\",\"1\",\"GS7523\",\"13:04:00\",\"13:15:00\",\"0\",\"654123198406082773\",\"KWE\",\"URC\",\"1\",\"HU9130108093\",\"\"],[\"RR\",\"N\",\"\",\"A\",\"7523\",\"\",\"20190406\",\"20190409\",\"马宝全\",\"I\",\"MABAOQUAN\",\"09:15:00\",\"1\",\"GS7523\",\"13:04:00\",\"13:15:00\",\"0\",\"654123198406082773\",\"KWE\",\"URC\",\"1\",\"HU9130108093\",\"\"],[\"RR\",\"N\",\"\",\"A\",\"7523\",\"\",\"20190406\",\"20190409\",\"马宝全\",\"I\",\"MABAOQUAN\",\"09:15:00\",\"1\",\"GS7523\",\"13:04:00\",\"13:15:00\",\"0\",\"654123198406082773\",\"KWE\",\"URC\",\"1\",\"HU9130108093\",\"\"],[\"RR\",\"N\",\"\",\"E\",\"8545\",\"\",\"20190416\",\"20190419\",\"马宝全\",\"I\",\"MABAOQUAN\",\"10:35:00\",\"1\",\"CZ8545\",\"15:56:00\",\"15:00:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"RR\",\"N\",\"\",\"E\",\"8545\",\"\",\"20190416\",\"20190419\",\"马宝全\",\"I\",\"MABAOQUAN\",\"10:35:00\",\"1\",\"CZ8545\",\"15:56:00\",\"15:00:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HX\",\"N\",\"\",\"E\",\"8545\",\"\",\"20190419\",\"20190419\",\"马宝全\",\"I\",\"MABAOQUAN\",\"10:35:00\",\"1\",\"CZ8545\",\"09:25:00\",\"15:00:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"],[\"HK\",\"N\",\"\",\"S\",\"8545\",\"\",\"20190419\",\"20190419\",\"马宝全\",\"I\",\"MABAOQUAN\",\"10:35:00\",\"1\",\"CZ8545\",\"11:11:00\",\"15:00:00\",\"0\",\"654123198406082773\",\"URC\",\"KWE\",\"1\",\"CZ113265130780\",\"\"]]}}";

        LOG.info("responseData:>>>{}", responseData);
        ResponseData data = JSONObject.parseObject(responseData, ResponseData.class);
        LOG.info("response data:>>>{}",data);
        LOG.info("response data getCount:>>>{}",data.getApp_body().getCount());
        LOG.info("response data getTotalCount:>>>{}",data.getApp_body().getTotalCount());
        LOG.info("response data getResult:>>>{}",data.getApp_body().getResult());

        List<List<String>> responseDataBodys = data.getApp_body().getResult();

        // 响应报文结构重组
        if (Objects.isNull(responseDataBodys) || responseDataBodys.isEmpty()){
            return;
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

            // 单独计算每行数据的sid(主键)值
            setSid(vals, resData);
            resultData.add(vals);
        }

        appBody.setCount(data.getApp_body().getCount());
        appBody.setTotalCount(data.getApp_body().getTotalCount());
        appBody.setResult(resultData);
        ResponseViewData responseViewData = new ResponseViewData();
        responseViewData.setApp_body(appBody);
        AppHeader appHeader = new AppHeader();
        appHeader.setStatus(data.getApp_header().getStatus());
        appHeader.setStatusMsg(data.getApp_header().getStatusMsg());
        responseViewData.setServerId(serviceId);
        responseViewData.setApp_header(appHeader);
        LOG.info("responseViewData:>>>{}", responseViewData);
        LOG.info("responseViewData:>>>{}", JSON.toJSONString(responseViewData));
    }

    /**
     * 第一行不参与数据重组
     * @param responseDataBodys
     */
    private void removeOneData(List<List<String>> responseDataBodys){
        responseDataBodys.remove(0);
    }

    /**
     * mock 查询服务的主键配置数据
     * @return
     */
    private SystemConfig encapsulationData(){
        SystemConfig systemConfig = new SystemConfig();
        List<SystemConfig.KeysConfigs> keysConfigs = new ArrayList<>();
        SystemConfig.KeysConfigs keysConfigs1 = new SystemConfig.KeysConfigs();
        keysConfigs1.setKeyFileds("air_seg_flt_nbr@air_seg_dpt_dt_lcl@pas_id_nbr@operatetime");
//        keysConfigs1.setKeyFileds("");
//        keysConfigs1.setKeyFileds("air_seg_flt_nbr@");
//        keysConfigs1.setKeyFileds("air_seg_flt_nbr");
        keysConfigs1.setServiceId(serviceId);

        SystemConfig.KeysConfigs keysConfigs2 = new SystemConfig.KeysConfigs();
        keysConfigs2.setKeyFileds("air_seg_flt_nbr@air_seg_dpt_dt_lcl@pas_id_nbr@operasadasdtetime");
//        keysConfigs2.setKeyFileds("");
//        keysConfigs2.setKeyFileds("air_seg_flt_nbr@");
//        keysConfigs2.setKeyFileds("air_seg_flt_nbr");
        keysConfigs2.setServiceId(serviceId);
        keysConfigs.add(keysConfigs1);
        keysConfigs.add(keysConfigs2);
        systemConfig.setConfigs(keysConfigs);
        return systemConfig;
    }

    /**
     * 获取查询服务的主键keys集合
     * @param serviceId  查询服务ID
     * @return String[]
     */
    private String[] getKeysFileds(String serviceId){
        SystemConfig systemConfig = encapsulationData();
        List<SystemConfig.KeysConfigs> configs = systemConfig.getConfigs();
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
    private void setSid(Map<String, String> vals,String[] keysResData){
        String sid = "";
        for (String key : keysResData){
            if (vals.containsKey(key)){
                sid = sid.concat(vals.get(key));
            }
        }
        vals.put(SID, sid);
    }

    /**
     * 陕AQ9U93 ==> 陕A??U93,陕A?9?93,陕A?9U?3,陕A?9U9?
     * 陕AQ9U93 ==> 陕AQ??93,陕AQ?U?3,陕AQ?U9?
     * 陕AQ9U93 ==> 陕AQ9??3,陕AQ9?9?
     * 陕AQ9U93 ==> 陕AQ9U??
     * 陕AQ9U934
     * @param planNo
     */
    private void f1(String planNo){
        if (Tools.isEmpty(planNo)){
            return;
        }

        if (planNo.length() > 8){
            return;
        }

        List resData = new ArrayList(50);
        char placeholder = '?';
        int len = planNo.toCharArray().length;
        for (int i = 0;i < len;i++){
            // 横纵解析
            for (int y = i+1;y < len;y++){
                char[] valt1 = planNo.toCharArray();
                valt1[i] = placeholder;
                valt1[y] = placeholder;
                resData.add(analysis(valt1));
            }
        }
    }

    private String analysis(char[] arge){
        return String.valueOf(arge);
    }

    @org.junit.Test
    public void f2(){
        String planNo = "陕AQ9U93";
        planNo = "Q9U93";
        f1(planNo);

        Map t1 = new HashMap();
    }

    public String[] table;

    private void initVal(){
        int valLen = 5;
        table = new String[valLen];

        table[0] = "123";
        table[1] = "123";
        table[2] = "123";
        table[3] = "123";
        table[4] = "123";

        CopyOnWriteMap copyOnWriteMap;

        CopyOnWriteArrayList copy;

        HashMap hashMap = new HashMap();
        hashMap.put(null,"T11111");
        hashMap.put(1,"T11111");
        hashMap.put("asda","T11111");
        hashMap.put(345,"T11111");

        ConcurrentHashMap temp;

        TreeMap<String,String> treeMap = new TreeMap<String,String>(new Comparator(){
            // 自定义key值的比较模式
            @Override
            public int compare(Object o1, Object o2) {
                return ((String)o1).compareTo((String)o1);
            }
        });
        treeMap.put("12","ttt1");
        treeMap.put("2","ttt2");
        treeMap.put("3h","ttt3");
        treeMap.put("4","ttt231");
        treeMap.put("25","ttt4");
        treeMap.put("6","ttt123w");

        System.out.println("table:>" + hashMap.toString() + ",value:>" + treeMap.toString());

        System.out.println("迭代器遍历");
        Iterator<String> strItr = treeMap.keySet().iterator();
        while(strItr.hasNext()){
            System.out.println(treeMap.get(strItr.next()));
        }
    }

    @org.junit.Test
    public void t2TableClear(){
        initVal();
        String[] tableTemp;
        tableTemp = table;
        if (tableTemp != null) {
            for (int i = 0; i < tableTemp.length; ++i)
                tableTemp[i] = null;
        }

        System.out.println("tableTemp:>" + tableTemp.length + ",value:>" + Arrays.toString(tableTemp));
        System.out.println("table:>" + table.length + ",value:>" + Arrays.toString(table));
    }
}