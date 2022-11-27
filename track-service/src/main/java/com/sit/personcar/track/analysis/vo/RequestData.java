package com.sit.personcar.track.analysis.vo;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求报文对象
 *         {
 *           "app_header":{
 *
 *         },
 *           "app_body":{
 *             "method":"query",
 *             "parameters":{
 *                 "condition":"XM=张三",
 *                 "requiredItems":["XM","XB"],
 *                 "sortItems":["XM","XB"],
 *                 "pageSize":"10",
 * 			       "pageNum":"1",
 *                 "infoCodeMode":""
 *             }
 *         }
 *         }
 */
public class RequestData {
    private static final Logger LOG = LoggerFactory.getLogger(RequestData.class);

    private String condition;
    private String[] requiredItems;
    private String[] sortItems;
    private String pageSize;
    private String pageNum;
    private String infoCodeMode;
    private String serviceId;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String[] getRequiredItems() {
        return requiredItems;
    }

    public void setRequiredItems(String[] requiredItems) {
        this.requiredItems = requiredItems;
    }

    public String[] getSortItems() {
        return sortItems;
    }

    public void setSortItems(String[] sortItems) {
        this.sortItems = sortItems;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getInfoCodeMode() {
        return infoCodeMode;
    }

    public void setInfoCodeMode(String infoCodeMode) {
        this.infoCodeMode = infoCodeMode;
    }

    public String getServiceId() {
        return serviceId;
    }
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        // 封装请求报文
        Map requestData = new HashMap<>();
        Map appBodyData = new HashMap<>();
        Map parametersData = new HashMap<>();
        parametersData.put("condition", getCondition());
        parametersData.put("requiredItems", getRequiredItems());
        parametersData.put("sortItems", getSortItems());
        parametersData.put("pageSize", getPageSize());
        parametersData.put("pageNum", getPageNum());
        parametersData.put("infoCodeMode", getInfoCodeMode());
        appBodyData.put("parameters", parametersData);
        appBodyData.put("method","query");
        requestData.put("app_header", new HashMap<>());
        requestData.put("app_body", appBodyData);
        LOG.info("request data:>{}", requestData);

        return JSON.toJSONString(requestData);
    }
}
