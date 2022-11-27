用于分析获取人员车辆的静态轨迹数据
实现功能
1、获取人员车辆的静态轨迹数据
2、按照22类数据查询服务，统一转换结果数据，结果数据转换为标准json格式
3、将每次获取到的结果数据异步生产至kafka消息队列中
4、后期由kettle自动消费kafka消息数据并同时生产至hive与es存储
5、hive中的数据用于计算人车标签、关系结构

外部接口请求采用异步AsyncHttpClient来实现

数据查询服务信息如下：
序号	数据来源	数据主域	数据子域	队列主题	文档名称
1	区厅铁路实名制信息	人员	铁路实名制实体	TLSMZST	请求方（C90-65000046）访问服务方（S90-65000292）接入指南.docx
2	区厅客运站售票信息	人员	客运站售票实体	KYZSPST	请求方（C90-65000046）访问服务方（S90-65000301）接入指南.docx
3	区厅常住人口基本信息	人员	常住人口实体	CZRKST	请求方（C90-65000046）访问服务方（S90-65000310）接入指南.docx
4	区厅出入境证件明细查询	人员	出入境证件明细实体	CRJZJMXST	请求方（C90-65000046）访问服务方（S90-65000311）接入指南.docx
5	区厅民航总局订座信息查询	人员	民航订座实体	MHDZST	请求方（C90-65000046）访问服务方（S90-65000313）接入指南.docx
6	区厅交通违法记录服务	车辆	交通违法实体	JTWFST	请求方（C90-65000046）访问服务方（S90-65000314）接入指南.docx
7	区厅交通违法强制措施查询	车辆	违法强制措施实体	WFQZCSST	请求方（C90-65000046）访问服务方（S90-65000315）接入指南.docx
8	区厅出入境记录(新疆)查询	人员	出入境记录（新疆）实体	CRJJLXJST	请求方（C90-65000046）访问服务方（S90-65000318）接入指南.docx
9	区厅婚姻登记信息查询	人员	婚姻登记实体	HYDJST	请求方（C90-65000046）访问服务方（S90-65000337）接入指南.docx
10	区厅网吧上网人员查询服务	人员	网吧上网人员实体	WBSWRYST	请求方（C90-65000046）访问服务方（S90-65000338）接入指南.docx
11	区厅流动人口信息查询服务	人员	流动人口实体	LDRKST	请求方（C90-65000046）访问服务方（S90-65000349）接入指南.docx
12	区厅警综精神病人员查询服务	人员	精神病人实体	JSBRST	请求方（C90-65000046）访问服务方（S90-65000350）接入指南.docx
13	区厅监管人员信息查询服务方(拘留所)	人员	监管人员（拘留所）实体	JGRYJLSST	请求方（C90-65000046）访问服务方（S90-65000369）接入指南.docx
14	区厅监管人员信息查询服务方(看守所)	人员	监管人员（看守所）实体	JGRYKSSST	请求方（C90-65000046）访问服务方（S90-65000371）接入指南.docx
15	区厅警综平台嫌疑人信息查询服务	人员	嫌疑人实体	XYRST	请求方（C90-65000046）访问服务方（S90-65000373）接入指南.docx
16	区厅旅馆基本信息查询服务	旅馆	旅馆实体	LGST	请求方（C90-65000046）访问服务方（S90-65000419）接入指南.docx
17	区厅机动车登记信息查询服务	车辆	车辆登记实体	CLDJST	请求方（C90-65000046）访问服务方（S90-65000480）接入指南.docx
18	公安厅旅馆业从业人员基本信息查询	人员	旅馆从业人员实体	LGCYRYST	请求方（C90-65000046）访问服务方（S90-65000491）接入指南.docx
19	区厅监管人员信息查询服务	人员	监管人员实体	JGRYST	请求方（C90-65000046）访问服务方（S90-65000523）接入指南.docx
20	区厅事故信息主表数据查询服务	案件	人、车、物、场景相关实体	SGXGST	请求方（C90-65000046）访问服务方（S90-65000533）接入指南.docx
21	区厅机动车行驶证信息数据查询	车辆	车辆行驶证实体	CLXSZST	请求方（C90-65000046）访问服务方（S90-65000548）接入指南.docx
22	国内旅客住宿信息查询	人员	酒店入住实体	JDRZST	请求方（C90-65000046）访问服务方（S90-65000321）接入指南.doc


数据查询服务接口如下：
请求方式：POST
媒体类型：application/json;charset=UTF-8
调用地址：http://104.4.6.54:8585/GatewayMsg/proxy/invoke

数据查询服务请求头信息如下：
参数名称	类型	必填	描述
sender_id	string	是	总线Id:请求方Id
service_id	string	是	总线Id:服务方Id
end_user.name	string	是	终端访问用户姓名，需要使用urlcode转换
end_user.id_card	string	是	终端访问用户身份标识号码
end_user.department	string	是	终端访问用户所在单位
end_user.certificate	string	是	终端访问用户数字证书编号
end_user.device_id	string	是	终端标识可以是pc的ip或者移动设备的mac
reason	string	否	调用理由，原因
timeout	string	否	单位秒，超时后返回task_id 后续获取结果0 ：立即返回   -1：系统最大值
timestamp	string	是	请求方发出请求的时间点格式为yyyyMMddHHmmssSSS

数据查询服务请求报文信息如下：
参数名称	类型	必填	描述
condition	string	是	zdzm,ccrxm,ccrq,zjh,cczm 查询条件，必填，字符串类型，参照Oracle SQL中Select语句之Where子句后的条件表达式，只允许使用以下操作符：=、<=、>=、>、<、like，只允许提交该类服务规定的条件字段
requiredItems	string[]	是	spsj,xwh,cxh,cc,zdzm,zjlxmc,ccrxm,czyh,xb,spck,ccrq,zjh,fcsj,spczmc,cczm 返回结果数据项集，非必填，一维数组类型，只允许提交该类服务规定的结果返回字段，如果为空则返回全部服务设定字段
sortItems	string[]	是	spsj,ybzj,bzgxsj,xwh,spczbm,bzrksj,cxh,pzmc,bz,sch,id,cc,sjzxsj,ybzhsjc,pzbm,zdzm,zjlxmc,ccrxm,czyh,sfz18,xb,spck,zjlxbm,ccrq,sjscbz,ph,zjh,fcsj,bid,spczmc,cczm,yscbz 排序项，必填，要求使用排序项对返回结果进行排序，只允许提交该类服务规定的排序项字段，按顺序进行升序排序
pageSize	string	是	每页结果记录数，服务将返回可返回的结果记录，不大于5000，需要填整数，范围1-5000； 每页结果记录数，服务将返回可返回的结果记录，不大于5000，需要填整数，范围1-5000；
pageNum	string	是	返回结果页码，必填，要求返回符合要求的结果数据集中第几页的数据记录，且需要填不小于1的整数； 返回结果页码，必填，要求返回符合要求的结果数据集中第几页的数据记录，且需要填不小于1的整数；
infoCodeMode	string	是	信息代码输出模式，非必填，字符串类型，0-只返回代码，1-只返回码值，2-返回代码和码值(用/分隔)，默认只返回代码；采用附录B信息代码输出模式 信息代码输出模式，非必填，字符串类型，0-只返回代码，1-只返回码值，2-返回代码和码值(用/分隔)，默认只返回代码；采用附录B信息代码输出模式

请求地址
http://127.0.0.1:12077/GatewayMsg/proxy/invoke/v2
请求类型
POST
请求报文
{
    "condition": "", // 查询条件，必填（一般都是身份证号码）
    "requiredItems": [""],// 返回字段,非必填默认返回该服务的全部字段数据
    "sortItems": [""],// 排序字段,非必填默认后台已经按照文档内置了排序字段
    "pageSize": "100",// 每页结果记录数，默认100
    "pageNum": "1",// 当前页码
    "infoCodeMode": "", // 请求描述，非必填
    "serviceId": "C90-65000046" // 请求服务ID，必填（参考文档）
}
响应报文
{
    "app_header": {
        "status": "000",
        "statusMsg": "操作成功"
    },
    "app_body": {
        "count": "20",
        "totalCount": "94",
        "result": [
            {
                "grp_ind": "N",
                "operatedate": "20181204",
                "air_seg_arrv_airpt_cd": "URC",
                "pn_seat": "1",
                "sub_cls_cd": "W",
                "sid": "698820181204654123198406082773",
                "pas_id_nbr": "654123198406082773",
                "ffp_id_nbr": "",
                "pas_id_type": "I",
                "vip_ind": "",
                "air_seg_arrv_dt_lcl": "1",
                "air_seg_flt_nbr": "6988",
                "air_seg_dpt_dt_lcl": "20181204",
                "grp_nm": "",
                "pas_id": "1",
                "air_seg_arrv_tm_lcl": "01:25:00",
                "flight_no": "CZ6988",
                "air_seg_dpt_airpt_cd": "KWE",
                "opr_stat_cd": "XX",
                "air_seg_dpt_tm_lcl": "21:15:00",
                "pas_fst_nm": "",
                "pas_chn_nm": "马宝全",
                "pas_lst_nm": "MABAOQUAN",
                "operatetime": "13:33:00"
            }
        ]
    },
    "serverId": "S90-65000313"
}

部署方式
未采用docker容器化部署
nohup java -jar /opt/track-service/personnelcar-tratrack-service-v1.0.jar > out.log 2>&1 &